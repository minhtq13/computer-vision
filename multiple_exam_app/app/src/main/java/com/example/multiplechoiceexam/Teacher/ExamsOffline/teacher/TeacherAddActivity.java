package com.example.multiplechoiceexam.Teacher.ExamsOffline.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.ValidateInputData;
import com.example.multiplechoiceexam.dto.teacher.TeacherRequest;
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherAddActivity extends AppCompatActivity {

    TextInputEditText txtUsername, txtPassWord, txtName, txtEmail,txtMSSV,txtBirthday, txtPhone;

    String selectGender;
    RadioGroup radioGroupGender;

    Button btnTeacherCreate;

    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add);
        apiService = RetrofitClient.getApiService(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        anhXa();

        radioGroupGender.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checkedRadioButton = findViewById(i);
            selectGender = checkedRadioButton.getText().toString();
            Toast.makeText(TeacherAddActivity.this, selectGender, Toast.LENGTH_SHORT).show();

        });

        txtBirthday.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(TeacherAddActivity.this, (datePicker, i, i1, i2) -> {

                int month1 = i1 + 1;
                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month1);
                @SuppressLint("DefaultLocale") String formattedDay= String.format("%02d", i2); // Định dạng giờ thành số không đằng trước
                String date = i + "-" + formattedMonth + "-" + formattedDay;
                txtBirthday.setText(date);
            },year,month,day);
            datePickerDialog1.show();
        });

        btnTeacherCreate.setOnClickListener(view -> {
            TeacherRequest teacherRequest = new TeacherRequest();
            String username = txtUsername.getText().toString().trim();
            if (!ValidateInputData.stringNotNull(username, txtUsername)) {
                return;
            }

            String password = txtPassWord.getText().toString().trim();
            if (!ValidateInputData.stringNotNull(password, txtPassWord)) {
                return;
            }

            String fullName = txtUsername.getText().toString().trim();
            if (!ValidateInputData.stringNotNull(fullName, txtUsername)) {
                return;
            }
            String code = txtMSSV.getText().toString().trim();
            if (!ValidateInputData.stringNotNull(code, txtMSSV)) {
                return;
            }
            String email = txtEmail.getText().toString().trim();
            if (!ValidateInputData.email(email, txtEmail)) {
                return;
            }
            String phone = txtPhone.getText().toString().trim();
            if(!phone.isEmpty() && phone.length() < 9) {
                txtPhone.setError("Kiểm tra lại số điện thoại");
            }
            teacherRequest.setUsername(Objects.requireNonNull(txtUsername.getText()).toString());
            teacherRequest.setPassword(Objects.requireNonNull(txtPassWord.getText()).toString());
            teacherRequest.setFullName(Objects.requireNonNull(txtName.getText()).toString());
            teacherRequest.setCode(Objects.requireNonNull(txtMSSV.getText()).toString());
            teacherRequest.setEmail(Objects.requireNonNull(txtEmail.getText()).toString());
            teacherRequest.setBirthday(Objects.requireNonNull(txtBirthday.getText()).toString());
            teacherRequest.setPhoneNumber(Objects.requireNonNull(txtPhone.getText()).toString());
            if(selectGender == null || selectGender.equals("Nam")){
                teacherRequest.setGender("MALE");
            }else {
                teacherRequest.setGender("FEMALE");
            }

            addTeacher(teacherRequest);
        });
    }

    private void addTeacher(TeacherRequest teacherRequest) {
        apiService.addTeacher(teacherRequest).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TeacherAddActivity.this, "Thêm giảng viên thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeacherAddActivity.this, "Thêm giảng viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseMessage> call,@NotNull Throwable t) {
                Toast.makeText(TeacherAddActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void anhXa() {
        txtUsername = findViewById(R.id.txt_teacher_username);
        txtPassWord = findViewById(R.id.txt_teacher_pass);
        txtName = findViewById(R.id.txt_teacher_name);
        txtEmail = findViewById(R.id.txt_teacher_gmail);
        txtMSSV = findViewById(R.id.txt_teacher_mssv);
        txtBirthday = findViewById(R.id.txt_teacher_birthday);
        txtPhone = findViewById(R.id.txt_teacher_phone);
        btnTeacherCreate = findViewById(R.id.btn_teacher_save);
        radioGroupGender =findViewById(R.id.operatorRadioGroupGenderTeacher);
    }
}