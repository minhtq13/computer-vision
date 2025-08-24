package com.example.multiplechoiceexam.SignIn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.SharedPreferences.AuthPreferences;
import com.example.multiplechoiceexam.SignUp.SignUpActivity;
import com.example.multiplechoiceexam.Teacher.TeacherMainActivity;
import com.example.multiplechoiceexam.Utils.MemoryData;
import com.example.multiplechoiceexam.Utils.Utility;
import com.example.multiplechoiceexam.Utils.ValidateInputData;
import com.example.multiplechoiceexam.dto.auth.AuthenticationRequest;
import com.example.multiplechoiceexam.dto.auth.AuthenticationResponse;
import com.example.multiplechoiceexam.dto.auth.ProfileUserDTO;
import com.example.multiplechoiceexam.viewmodel.TeacherProfileViewModel;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private EditText usernamEditText;
    private EditText passwordEditText;
    private AuthPreferences authPreferences;
    private Button loginButton;
    LinearLayout linearLayout;
    private ProgressBar progressBar_login;
    private TextInputLayout passLayout;
    private ImageView fingerprintOptionTextView;

    private ApiService apiService;
    private TeacherProfileViewModel teacherProfileViewModel;
    private AccountSharedPreferences accountSharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_activty);
        apiService = RetrofitClient.getApiService(SignInActivity.this);
        authPreferences = new AuthPreferences(SignInActivity.this);
        teacherProfileViewModel = new ViewModelProvider(this).get(TeacherProfileViewModel.class);
        usernamEditText = findViewById(R.id.signIn_userName_edt);
        passwordEditText = findViewById(R.id.signIn_password_edt);
        linearLayout = findViewById(R.id.linear1);
        progressBar_login = findViewById(R.id.signIn_progressBar);
        loginButton = findViewById(R.id.signIn_button);
        passLayout = findViewById(R.id.signIn_passLayout);
        fingerprintOptionTextView = findViewById(R.id.fingerprintOptionTextView);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Có thể sử dụng tính năng vân tay
                showFingerprintLoginOption(); // Hiển thị tùy chọn đăng nhập bằng vân tay
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Thiết bị không hỗ trợ vân tay hoặc người dùng chưa đăng ký vân tay
                hideFingerprintLoginOption(); // Ẩn tùy chọn đăng nhập bằng vân tay
                break;
        }

        passLayout.setHelperText("Độ dài mật khẩu >4");

        usernamEditText.setText(getIntent().getStringExtra("username"));
        passwordEditText.setText(getIntent().getStringExtra("password"));

        loginButton.setOnClickListener(v -> {
            changInProgress(true);
            String username = usernamEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // kiểm tra tên đăng nhập mật khẩu
            if (!validateData(username, password)) {
                changInProgress(false);
                return;
            }
                performRegularLogin(username, password);

        });

        teacherProfileViewModel.getTeacherProfileLiveData().observe(this, new Observer<ProfileUserDTO>() {
            @Override
            public void onChanged(ProfileUserDTO profileUserDTO) {
                if (profileUserDTO != null) {
                    String username = profileUserDTO.getName();
                    String roleResult = profileUserDTO.getRole();
                    String role = "Giảng viên";

                    if ("ROLE_STUDENT".equals(roleResult)) {
                        role = "Sinh viên";
                    }
                    saveUserInfo(username, role);
                } else {
                    Toast.makeText(SignInActivity.this, "Lỗi khi lấy thông tin giảng viên", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void performRegularLogin(String username, String password) {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);

        apiService.authenticateUser(authenticationRequest).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(@NotNull Call<AuthenticationResponse> call, @NotNull Response<AuthenticationResponse> response) {
                changInProgress(false);
                if (response.isSuccessful()) {
                    AuthenticationResponse authenticationResponse = response.body();
                    accountSharedPreferences = new AccountSharedPreferences(SignInActivity.this);
                    assert authenticationResponse != null;
                    accountSharedPreferences.setIssuedAt(authenticationResponse.getIssuedAt());
                    accountSharedPreferences.setRoles(authenticationResponse.getRoles());
                    accountSharedPreferences.setAccessToken(authenticationResponse.getAccessToken());
                    accountSharedPreferences.setRefreshToken(authenticationResponse.getRefreshToken());
                    authPreferences.setUsername(username);
                    authPreferences.setPassword(password);
                    MemoryData.saveCurrentAccount(accountSharedPreferences, SignInActivity.this);
                    Log.d("AccessToken", authenticationResponse.getAccessToken());
                    MemoryData.saveAccessToken(authenticationResponse.getAccessToken(), SignInActivity.this);
                    startActivity(new Intent(SignInActivity.this, TeacherMainActivity.class));
                    Utility.showToast(SignInActivity.this, "Đăng nhập thành công!");
                    teacherProfileViewModel.getTeacherProfile();
                    finish();
                } else {
                    handleAuthenticationError(response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<AuthenticationResponse> call,@NotNull Throwable t) {
                Utility.showToast(SignInActivity.this, "Error: " + t.getMessage());
                changInProgress(false);
            }
        });
    }

    private void changInProgress(boolean inProgress) {
        if (inProgress) {
            linearLayout.setVisibility(View.GONE);
            progressBar_login.setVisibility(View.VISIBLE);
        } else {
            progressBar_login.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showFingerprintLoginOption() {
        fingerprintOptionTextView.setVisibility(View.VISIBLE);
        fingerprintOptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBiometricPrompt();
            }
        });
    }

    private void hideFingerprintLoginOption() {
        fingerprintOptionTextView.setVisibility(View.GONE);
    }

    private void showBiometricPrompt() {
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, new Executor() {
            @Override
            public void execute(Runnable command) {
                new Handler(Looper.getMainLooper()).post(command);
            }
        }, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                handleFingerprintAuthentication();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                handleAuthenticationError(errorCode);
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setDescription("Use your fingerprint to log in")
                .setNegativeButtonText("Use username and password")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void handleFingerprintAuthentication() {
        String fingerprintUsername = authPreferences.getFingerprintUsername();
        String fingerprintPassword = authPreferences.getFingerprintPassword();
        if(fingerprintPassword == "" || fingerprintUsername == ""){
            Toast.makeText(this, "Chưa cài đặt Touch Id", Toast.LENGTH_SHORT).show();
        }else {
            performRegularLogin(fingerprintUsername, fingerprintPassword);
        }
    }

    private void handleAuthenticationError(int errorCode) {
        if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
            // Người dùng đã hủy xác thực vân tay
            Utility.showToast(SignInActivity.this, "Fingerprint authentication canceled");
        } else {
            // Xử lý lỗi xác thực vân tay hoặc các lỗi khác
            Utility.showToast(SignInActivity.this, "Authentication error: " + errorCode);
        }
    }


    private boolean validateData(String username, String password) {
        if (username.length() < 4 || ValidateInputData.isUnicode(username)) {
            usernamEditText.setError("Username is incorrect!");
            return false;
        }
        if (password.length() < 4) {
            passLayout.setHelperText("Password length must be >4");
            passLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            return false;
        }
        return true;
    }

    public void startSignUpActivity(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    private void saveUserInfo(String username, String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("role", role);
        editor.apply();
    }
}
