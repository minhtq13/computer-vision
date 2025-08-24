package com.example.multiplechoiceexam.Teacher.ExamsOffline.student;

import androidx.annotation.NonNull;
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
import com.example.multiplechoiceexam.dto.student.StudentRequest;
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAddActivity extends AppCompatActivity {

    TextInputEditText txtUsername, txtPassWord, txtName, txtEmail,txtCourse,txtMSSV,txtBirthday, txtPhone;

    String selectGender;
    RadioGroup radioGroupGender;

    Button btnStudentCreate;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);

        apiService = RetrofitClient.getApiService(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        anhXa();

        radioGroupGender.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checkedRadioButton = findViewById(i);
            selectGender = checkedRadioButton.getText().toString();
            Toast.makeText(StudentAddActivity.this, selectGender, Toast.LENGTH_SHORT).show();

        });

        txtBirthday.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(StudentAddActivity.this, (datePicker, i, i1, i2) -> {

                int month1 = i1 + 1;
                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month1);
                @SuppressLint("DefaultLocale") String formattedDay= String.format("%02d", i2); // Định dạng giờ thành số không đằng trước
                String date = i + "-" + formattedMonth + "-" + formattedDay;
                txtBirthday.setText(date);
            },year,month,day);
            datePickerDialog1.show();
        });

        btnStudentCreate.setOnClickListener(view -> {
            StudentRequest studentRequest = new StudentRequest();
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

            int k = Integer.parseInt(txtCourse.getText().toString().trim());
            if(k < 50){
                txtCourse.setError("Kiểm tra lại");
            }
            studentRequest.setUsername(Objects.requireNonNull(txtUsername.getText()).toString());
            studentRequest.setPassword(Objects.requireNonNull(txtPassWord.getText()).toString());
            studentRequest.setFullName(Objects.requireNonNull(txtName.getText()).toString());
            studentRequest.setCode(Objects.requireNonNull(txtMSSV.getText()).toString());
            studentRequest.setCourse(Integer.parseInt(Objects.requireNonNull(txtCourse.getText()).toString()));
            studentRequest.setEmail(Objects.requireNonNull(txtEmail.getText()).toString());
            studentRequest.setBirthday(Objects.requireNonNull(txtBirthday.getText()).toString());
            studentRequest.setPhoneNumber(Objects.requireNonNull(txtPhone.getText()).toString());
            if(selectGender == null || selectGender.equals("Nam")){
                studentRequest.setGender("MALE");
            }else {
                studentRequest.setGender("FEMALE");
            }

            addStudent(studentRequest);
        });

    }

    private void addStudent(StudentRequest studentRequest) {
        apiService.addStudent(studentRequest).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMessage> call, @NonNull Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StudentAddActivity.this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StudentAddActivity.this, "Thêm sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMessage> call, @NonNull Throwable t) {
                Toast.makeText(StudentAddActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void anhXa() {
        txtUsername = findViewById(R.id.txt_student_username);
        txtPassWord = findViewById(R.id.txt_student_pass);
        txtName = findViewById(R.id.txt_student_name);
        txtEmail = findViewById(R.id.txt_student_gmail);
        txtCourse = findViewById(R.id.txt_student_course);
        txtMSSV = findViewById(R.id.txt_student_mssv);
        txtBirthday = findViewById(R.id.txt_student_birthday);
        txtPhone = findViewById(R.id.txt_student_phone);
        btnStudentCreate = findViewById(R.id.btn_student_save);
        radioGroupGender =findViewById(R.id.operatorRadioGroupGender);
    }
}