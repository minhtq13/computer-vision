package com.example.multiplechoiceexam.Teacher.ProfileUser.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.student.StudentUpdateRequest;
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileUpdateActivity extends AppCompatActivity {

    TextInputEditText txtName, txtEmail,txtCourse,txtBirthday, txtPhone;
    String selectGender;
    RadioGroup radioGroupGender;
    Button btnStudentCreate;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_update);
        apiService = RetrofitClient.getApiService(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        anhXa();
        intent();

        radioGroupGender.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checkedRadioButton = findViewById(i);
            selectGender = checkedRadioButton.getText().toString();
            Toast.makeText(StudentProfileUpdateActivity.this, selectGender, Toast.LENGTH_SHORT).show();

        });

        txtBirthday.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(StudentProfileUpdateActivity.this, (datePicker, i, i1, i2) -> {

                int month1 = i1 + 1;
                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month1);
                @SuppressLint("DefaultLocale") String formattedDay= String.format("%02d", i2); // Định dạng giờ thành số không đằng trước
                String date = i + "-" + formattedMonth + "-" + formattedDay;
                txtBirthday.setText(date);
            },year,month,day);
            datePickerDialog1.show();
        });

        btnStudentCreate.setOnClickListener(view -> {
            StudentUpdateRequest studentRequest = new StudentUpdateRequest();
            studentRequest.setFirstName(Objects.requireNonNull(txtName.getText()).toString());
            studentRequest.setCode(Objects.requireNonNull(txtCourse.getText()).toString());
            studentRequest.setEmail(Objects.requireNonNull(txtEmail.getText()).toString());
            studentRequest.setBirthday(Objects.requireNonNull(txtBirthday.getText()).toString());
            studentRequest.setPhoneNumber(Objects.requireNonNull(txtPhone.getText()).toString());
            if(selectGender == null || selectGender.equals("Nam")){
                studentRequest.setGenderType("MALE");
            }else {
                studentRequest.setGenderType("FEMALE");
            }

            updateProfileStudent(studentRequest);
        });
    }

    private void intent() {
        Intent intent = getIntent();
        txtName.setText(intent.getStringExtra("student_name"));
        txtBirthday.setText(intent.getStringExtra("student_birth"));
        txtEmail.setText(intent.getStringExtra("student_email"));
        txtPhone.setText(intent.getStringExtra("student_phone"));
        txtCourse.setText(String.valueOf(intent.getIntExtra("student_course",0)));
    }

    private void anhXa() {
        txtName = findViewById(R.id.txt_student_profile_update_1_name);
        txtEmail = findViewById(R.id.txt_student_profile_update_1_gmail);
        txtCourse = findViewById(R.id.txt_student_profile_update_1_course);
        txtBirthday = findViewById(R.id.txt_student_profile_update_1_birthday);
        txtPhone = findViewById(R.id.txt_student_profile_update_1_phone);
        btnStudentCreate = findViewById(R.id.btn_student_profile_update_1_save);
        radioGroupGender =findViewById(R.id.operatorRadioGroupGenderUpdate1);
    }

    private void updateProfileStudent(StudentUpdateRequest studentRequest) {
        apiService.updateStudentProfile(studentRequest).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StudentProfileUpdateActivity.this, "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(StudentProfileUpdateActivity.this, "Cập nhật sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseMessage> call,@NotNull Throwable t) {
                Toast.makeText(StudentProfileUpdateActivity.this, "Lỗi call!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}