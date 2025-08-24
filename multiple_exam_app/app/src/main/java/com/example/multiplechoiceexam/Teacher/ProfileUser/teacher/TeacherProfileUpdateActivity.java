package com.example.multiplechoiceexam.Teacher.ProfileUser.teacher;

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
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherProfileUpdateActivity extends AppCompatActivity {

    TextInputEditText txtName, txtEmail,txtBirthday, txtPhone;
    String selectGender;
    Integer teacherId;
    RadioGroup radioGroupGender;

    Button btnTeacherCreate;

    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile_update);
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
            Toast.makeText(TeacherProfileUpdateActivity.this, selectGender, Toast.LENGTH_SHORT).show();

        });

        txtBirthday.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(TeacherProfileUpdateActivity.this, (datePicker, i, i1, i2) -> {

                int month1 = i1 + 1;
                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month1);
                @SuppressLint("DefaultLocale") String formattedDay= String.format("%02d", i2); // Định dạng giờ thành số không đằng trước
                String date = formattedDay +"/"+ formattedMonth+"/"+i;
                txtBirthday.setText(date);
            },year,month,day);
            datePickerDialog1.show();
        });
        btnTeacherCreate.setOnClickListener(view -> {
            TeacherUpdateRequest teacherUpdateRequest = new TeacherUpdateRequest();
            teacherUpdateRequest.setFirstName(Objects.requireNonNull(txtName.getText()).toString());
            teacherUpdateRequest.setEmail(Objects.requireNonNull(txtEmail.getText()).toString());
            teacherUpdateRequest.setBirthDate(Objects.requireNonNull(txtBirthday.getText()).toString());
            teacherUpdateRequest.setPhoneNumber(Objects.requireNonNull(txtPhone.getText()).toString());
            if(selectGender == null || selectGender.equals("Nam")){
                teacherUpdateRequest.setGenderType("1");
            }else {
                teacherUpdateRequest.setGenderType("0");
            }

            updateProfileUser(teacherUpdateRequest);
        });
    }

    private void updateProfileUser(TeacherUpdateRequest teacherUpdateRequest) {
        apiService.updateTeacherProfile(teacherUpdateRequest).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TeacherProfileUpdateActivity.this, "Cập nhật giảng viên thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TeacherProfileUpdateActivity.this, "Cập nhật giảng viên thành công 1!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseMessage> call,@NotNull Throwable t) {
                Toast.makeText(TeacherProfileUpdateActivity.this, "Cập nhật giảng viên thành công 2", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void intent() {
        Intent intent = getIntent();
        teacherId = intent.getIntExtra("teacher_id",-1);
        txtName.setText(intent.getStringExtra("teacher_name"));
        txtBirthday.setText(intent.getStringExtra("teacher_birth"));
        txtEmail.setText(intent.getStringExtra("teacher_email"));
        txtPhone.setText(intent.getStringExtra("teacher_phone"));
    }

    private void anhXa() {
        txtName = findViewById(R.id.txt_teacher_profile_update_name);
        txtEmail = findViewById(R.id.txt_teacher_profile_update_gmail);
        txtBirthday = findViewById(R.id.txt_teacher_profile_update_birthday);
        txtPhone = findViewById(R.id.txt_teacher_profile_update_phone);
        btnTeacherCreate = findViewById(R.id.btn_teacher_profile_update_save);
        radioGroupGender =findViewById(R.id.operatorRadioGroupGenderUpdateProfile);

    }
}