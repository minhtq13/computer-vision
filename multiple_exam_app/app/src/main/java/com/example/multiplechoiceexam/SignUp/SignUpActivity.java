package com.example.multiplechoiceexam.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.SignIn.SignInActivity;
import com.example.multiplechoiceexam.Utils.Utility;
import com.example.multiplechoiceexam.dto.auth.AuthenticationResponse;
import com.example.multiplechoiceexam.dto.auth.RegisterRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText email_edt, password_edt, confirmPassword_edt,username_edt;
    private Button createAccount_btn;
    private ProgressBar progressBar;
    private TextInputLayout emailLayout,passLayout,confirmLayout,usernameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailLayout = findViewById(R.id.signUp_email_layout);
        passLayout = findViewById(R.id.signUp_pass_layout);
        confirmLayout = findViewById(R.id.signUp_confirm_layout);
        usernameLayout = findViewById(R.id.signUp_Username_layout);

        email_edt = findViewById(R.id.signUp_email_edt);
        password_edt = findViewById(R.id.signUp_password_edt);
        confirmPassword_edt = findViewById(R.id.signUp_confirmPassword_edt);
        createAccount_btn = findViewById(R.id.signUp_button);
        progressBar = findViewById(R.id.signUp_progressBar);
        username_edt= findViewById(R.id.signUp_Username_edt);


              /*
       Kiểm tra dữ liệu đầu vào trong lúc người dùng đang nhập thông tin và đưa ra các thông báo phù hợp
        */
        addOnChangedListener();


        // set onclick cho nút tạo tài khoản
        createAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changInProgress(true);
                // lấy thông tin tài khoản do người dùng nhập
                String username = username_edt.getText().toString().trim();
                String email = email_edt.getText().toString().trim();
                String password = confirmPassword_edt.getText().toString().trim();

       /*     // lưu mật khẩu người dùng vào bộ nhớ ứng dụng sử dụng sharePreference
            SharedPreferences sharedPreferences = getSharedPreferences("MyPass", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CurrentPassword",password);
            editor.apply();
*/
                RegisterRequest registerRequest = new RegisterRequest(username,email,password);
                // call api register
                ApiService apiService  = RetrofitClient.getApiService(getApplicationContext());

                Call<AuthenticationResponse> call = apiService.registerUser(registerRequest);
                call.enqueue(new Callback<AuthenticationResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<AuthenticationResponse> call, @NotNull Response<AuthenticationResponse> response) {
                        changInProgress(false);
                        if(response.isSuccessful()){

                            Toast.makeText(SignUpActivity.this,"Sign Up Successfully. Please verify your email to activate your account !",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                            intent.putExtra("username",username);
                            intent.putExtra("password",password);
                            startActivity(intent);

                        }
                        else {
                            int statusCode = response.code();
                            if(statusCode == 409){
                                Utility.showToast(SignUpActivity.this,"Username or email already taken !");
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<AuthenticationResponse> call,@NotNull Throwable t) {
                        changInProgress(false);
                        Utility.showToast(SignUpActivity.this,"Error: " + t.getMessage());
                        Log.e("API Error", t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        });
    }


    /*
  Hàm kiểm tra dữ liệu đầu vào trong lúc người dùng đang nhập thông tin và đưa ra các thông báo phù hợp
   */
    private void addOnChangedListener(){
        email_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailLayout.setHelperText("Check your email again!");
                    emailLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    emailLayout.setError("");
                }
                else {
                    emailLayout.setHelperText("Valid email.");
                    emailLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if(password.length() <6){
                    passLayout.setHelperText("Password length must be at least 6 characters!");
                    passLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    passLayout.setError("");
                }
                else {
                    passLayout.setHelperText("Valid password.");
                    passLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPassword_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = password_edt.getText().toString().trim();
                String confirmPass = s.toString();
                if(!confirmPass.equals(password)){
                    confirmLayout.setHelperText("Passwords don't match!");
                    confirmLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    confirmLayout.setError("");
                }
                else {
                    confirmLayout.setHelperText("Ok");
                    confirmLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        username_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String username = s.toString();
                if(username.length()<6 || isValidInput(username)){

                    usernameLayout.setHelperText("Username must not contain accented letters and the length must be greater than 6 and must not contain spaces!");
                    usernameLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    usernameLayout.setError("");
                }
                else {
                    usernameLayout.setHelperText("Ok");
                    usernameLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void changInProgress(boolean inProgress){
        if(inProgress){
            createAccount_btn.setVisibility(View.GONE);  // đang trong tiến trình tạo account thì button biến mất
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
            createAccount_btn.setVisibility(View.VISIBLE);
        }
    }
    public void startLogin(View view) {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
    }

    public boolean isValidInput(String input) {
        // Biểu thức chính quy để kiểm tra chữ cái tiếng Việt có dấu và không chứa khoảng trắng ở giữa
        String regex = "^[^\\s]*[áàảãạăắằẳẵặâấầẩẫậđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ][^\\s]*$";

        // Kiểm tra so khớp biểu thức chính quy với chuỗi đầu vào
        return input.trim().matches(regex);
    }


}