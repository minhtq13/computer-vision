package com.example.multiplechoiceexam.Teacher.ExamsOffline.teacher;

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
import com.example.multiplechoiceexam.dto.teacher.TeacherUpdateRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherUpdateActivity extends AppCompatActivity {

    TextInputEditText txtNameFirst,txtNameLast, txtEmail,txtBirthday, txtPhone;
    String selectGender,code;
    Long teacherId,departmentId;
    RadioGroup radioGroupGender;

    Button btnTeacherCreate;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_update);

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
            Toast.makeText(TeacherUpdateActivity.this, selectGender, Toast.LENGTH_SHORT).show();

        });

        txtBirthday.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(TeacherUpdateActivity.this, (datePicker, i, i1, i2) -> {

                int month1 = i1 + 1;
                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month1);
                @SuppressLint("DefaultLocale") String formattedDay= String.format("%02d", i2); // Định dạng giờ thành số không đằng trước
                String date = formattedDay +"/"+formattedMonth+"/"+i;
                txtBirthday.setText(date);
            },year,month,day);
            datePickerDialog1.show();
        });
        btnTeacherCreate.setOnClickListener(view -> {
            TeacherUpdateRequest teacherUpdateRequest = new TeacherUpdateRequest();
            teacherUpdateRequest.setFirstName(Objects.requireNonNull(txtNameFirst.getText()).toString());
            teacherUpdateRequest.setLastName(Objects.requireNonNull(txtNameLast.getText()).toString());
            teacherUpdateRequest.setEmail(Objects.requireNonNull(txtEmail.getText()).toString());
            teacherUpdateRequest.setCode(code);
            teacherUpdateRequest.setDepartmentId(departmentId);
            teacherUpdateRequest.setBirthDate(Objects.requireNonNull(txtBirthday.getText()).toString());
            teacherUpdateRequest.setPhoneNumber(Objects.requireNonNull(txtPhone.getText()).toString());
            if(selectGender == null || selectGender.equals("Nam")){
                teacherUpdateRequest.setGenderType("1");
            }else {
                teacherUpdateRequest.setGenderType("0");
            }

            updateTeacher(teacherId,teacherUpdateRequest);
        });
    }

    private void updateTeacher(Long teacherId, TeacherUpdateRequest teacherUpdateRequest) {
        apiService.updateTeacherById(teacherId,teacherUpdateRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TeacherUpdateActivity.this, "Cập nhật giảng viên thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TeacherUpdateActivity.this, "Cập nhật giảng viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(TeacherUpdateActivity.this, "Lỗi call!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intent() {
        Intent intent = getIntent();
        teacherId = intent.getLongExtra("teacher_id",-1L);
        txtNameFirst.setText(intent.getStringExtra("teacher_name_first"));
        txtNameLast.setText(intent.getStringExtra("teacher_name_last"));
        txtBirthday.setText(intent.getStringExtra("teacher_birth"));
        txtEmail.setText(intent.getStringExtra("teacher_email"));
        txtPhone.setText(intent.getStringExtra("teacher_phone"));
        code = intent.getStringExtra("teacher_code");
        departmentId = intent.getLongExtra("departmentId", -1L);
    }

    private void anhXa() {
        txtNameFirst = findViewById(R.id.txt_teacher_update_first_name);
        txtNameLast = findViewById(R.id.txt_teacher_update_last_name);
        txtEmail = findViewById(R.id.txt_teacher_update_gmail);
        txtBirthday = findViewById(R.id.txt_teacher_update_birthday);
        txtPhone = findViewById(R.id.txt_teacher_update_phone);
        btnTeacherCreate = findViewById(R.id.btn_teacher_update_save);
        radioGroupGender =findViewById(R.id.operatorRadioGroupGenderUpdate);
    }
}