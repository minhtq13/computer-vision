package com.example.multiplechoiceexam.Teacher.ExamManagement.subject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.subject.SubjectRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectUpdateActivity extends AppCompatActivity {

    EditText editTextCode, editTextTitle, editTextDepartment,editTextCredit,editTextDescription;

    Long subjectId;
    Integer subjectCredit;
    Button updateSubjectBtn;
    String subjectTitle,subjectCode;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_update);

        apiService = RetrofitClient.getApiService(getApplicationContext());

        initCustom();
        loadData();

        updateSubjectBtn.setOnClickListener(view -> updateSubject(subjectId));

    }

    private void updateSubject(Long subjectId) {
        SubjectRequest subjectRequest = new SubjectRequest();
        subjectRequest.setCode(editTextCode.getText().toString());
        subjectRequest.setTitle(editTextTitle.getText().toString());
        subjectRequest.setDescription(editTextDescription.getText().toString());
        subjectRequest.setCredit(Integer.valueOf(editTextCredit.getText().toString()));

        apiService.updateSubject(subjectId, subjectRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,@NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    finish();
                    Toast.makeText(SubjectUpdateActivity.this, "Câp nhật môn học thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                    Toast.makeText(SubjectUpdateActivity.this, "Câp nhật môn học thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,@NonNull Throwable t) {
                Toast.makeText(SubjectUpdateActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadData() {
        Intent intent = getIntent();
        subjectId =intent.getLongExtra("subjectId",-1L);
        subjectCredit = intent.getIntExtra("subjectCredit", -1);
        subjectCode = intent.getStringExtra("subjectCode");
        subjectTitle = intent.getStringExtra("subjectTitle");
        editTextCode.setText(subjectCode);
        editTextCredit.setText(String.valueOf(subjectCredit));
        editTextTitle.setText(subjectTitle);
        editTextDescription.setText(subjectTitle);
    }

    private void initCustom() {
        editTextCode = findViewById(R.id.editTextCodeUpdate);
        editTextTitle = findViewById(R.id.editTextTitleUpdate);
        editTextDepartment = findViewById(R.id.editTextDepartmentUpdate);
        editTextCredit = findViewById(R.id.editTextCreditUpdate);
        updateSubjectBtn = findViewById(R.id.confirmButtonUpdate);
        editTextDescription = findViewById(R.id.editTextDescriptionUpdate);
    }
}