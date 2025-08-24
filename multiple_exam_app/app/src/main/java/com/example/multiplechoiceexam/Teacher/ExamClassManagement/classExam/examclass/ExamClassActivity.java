package com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.dto.examClass.ClassResponse;
import com.example.multiplechoiceexam.dto.examClass.ExamClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamClassActivity extends AppCompatActivity {

    private RecyclerView classRecyclerview;
    private ApiService apiService;
    private ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_class);

        apiService= RetrofitClient.getApiService(getApplicationContext());

        classRecyclerview =findViewById(R.id.recycler_exam_class);
        FloatingActionButton buttonAdd = findViewById(R.id.btn_add_exam_class);
        progressBar = findViewById(R.id.progressbar_examClass_loading);
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {
            buttonAdd.setVisibility(View.GONE);

        }
        findViewById(R.id.imageView_examClass_back).setOnClickListener(v -> finish());
        buttonAdd.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ExamClassAddActivity.class)));
        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString();
                searchExamClassByCode(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Call<ClassResponse> callRetrofit = apiService.getListExamClass(
                "",
                -1L,
                -1L,
                -1L,
                0,
                10000,
                "id"
        );
        callRetrofit.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClassResponse> call, @NonNull Response<ClassResponse> response) {
                if(response.isSuccessful()) {
                    ClassResponse classResponses = response.body();
                    assert classResponses != null;
                    List<ExamClass> examClass = classResponses.getContent();
                    getAllExamClass(examClass);
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(ExamClassActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                Toast.makeText(ExamClassActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void searchExamClassByCode(String searchText) {
        Call<ClassResponse> callRetrofit = apiService.getListExamClass(
                searchText,
                -1L,
                -1L,
                -1L,
                0,
                10000,
                "id"
        );
        callRetrofit.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClassResponse> call, @NonNull Response<ClassResponse> response) {
                if(response.isSuccessful()) {
                    ClassResponse classResponses = response.body();
                    List<ExamClass> examClass = Objects.requireNonNull(classResponses).getContent();
                    getAllExamClass(examClass);
                    progressBar.setVisibility(View.GONE);

                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ExamClassActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ExamClassActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllExamClass(List<ExamClass> examClass){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        classRecyclerview.setLayoutManager(layoutManager);
        ExamClassAdapter examClassAdapter = new ExamClassAdapter(this, examClass);
        classRecyclerview.setAdapter(examClassAdapter);
        examClassAdapter.notifyDataSetChanged();
    }
}