package com.example.multiplechoiceexam.Teacher.ExamsOffline.student;

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
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentUpdateActivity extends AppCompatActivity {

    TextInputEditText  txtNameFirst,txtNameLast, txtEmail,txtCourse,txtBirthday, txtPhone,txtCode;
    String selectGender;
    Long studentId, departmentId;
    RadioGroup radioGroupGender;
    Button btnStudentCreate;

    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);

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
        });

        txtBirthday.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(StudentUpdateActivity.this, (datePicker, i, i1, i2) -> {

                int month1 = i1 + 1;
                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month1);
                @SuppressLint("DefaultLocale") String formattedDay= String.format("%02d", i2); // Định dạng giờ thành số không đằng trước
                String date = formattedDay +"/"+formattedMonth+"/"+i;
                txtBirthday.setText(date);
            },year,month,day);
            datePickerDialog1.show();
        });

        btnStudentCreate.setOnClickListener(view -> {
            StudentUpdateRequest studentRequest = new StudentUpdateRequest();
            studentRequest.setFirstName(Objects.requireNonNull(txtNameFirst.getText()).toString());
            studentRequest.setLastName(Objects.requireNonNull(txtNameLast.getText()).toString());
            studentRequest.setCode(Objects.requireNonNull(txtCode.getText()).toString());
            studentRequest.setEmail(Objects.requireNonNull(txtEmail.getText()).toString());
            studentRequest.setBirthday(Objects.requireNonNull(txtBirthday.getText()).toString());
            studentRequest.setPhoneNumber(Objects.requireNonNull(txtPhone.getText()).toString());
            studentRequest.setDepartmentId(departmentId);
            if(selectGender == null || selectGender.equals("Nam")){
                studentRequest.setGenderType("1");
            }else {
                studentRequest.setGenderType("0");
            }

            updateStudent(studentId,studentRequest);
        });
    }

    private void updateStudent(Long studentId, StudentUpdateRequest studentRequest) {
        apiService.updateStudentById(studentId,studentRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StudentUpdateActivity.this, "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(StudentUpdateActivity.this, "Cập nhật sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(StudentUpdateActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intent() {
        Intent intent = getIntent();
        studentId = intent.getLongExtra("student_id",-1L);
        txtNameFirst.setText(intent.getStringExtra("student_name_first"));
        txtNameLast.setText(intent.getStringExtra("student_name_last"));
        txtBirthday.setText(intent.getStringExtra("student_birth"));
        txtEmail.setText(intent.getStringExtra("student_email"));
        txtPhone.setText(intent.getStringExtra("student_phone"));
        departmentId = intent.getLongExtra("departmentId", -1L);
        txtCode.setText(intent.getStringExtra("student_code"));
    }

    private void anhXa() {
        txtNameFirst = findViewById(R.id.txt_student_update_first_name);
        txtNameLast = findViewById(R.id.txt_student_update_last_name);
        txtEmail = findViewById(R.id.txt_student_update_gmail);
        txtCourse = findViewById(R.id.txt_student_update_course);
        txtCode = findViewById(R.id.txt_student_update_code);
        txtBirthday = findViewById(R.id.txt_student_update_birthday);
        txtPhone = findViewById(R.id.txt_student_update_phone);
        btnStudentCreate = findViewById(R.id.btn_student_update_save);
        radioGroupGender =findViewById(R.id.operatorRadioGroupGenderUpdate);
    }
}