package com.example.multiplechoiceexam.Teacher.ExamManagement.subjectexamtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectQuestionActivity extends AppCompatActivity {

    RecyclerView subjectRecyclerView;
    SubjectQuestionAdapter subjectQuestionAdapter;
    List<SubjectResponse.SubjectItem> subjectResponses;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_question);

        subjectResponses = new ArrayList<>();
        apiService = RetrofitClient.getApiService(getApplicationContext());

        subjectRecyclerView = findViewById(R.id.recycler_subject_question);
        subjectRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        subjectQuestionAdapter = new SubjectQuestionAdapter(this, subjectResponses);
        subjectRecyclerView.setAdapter(subjectQuestionAdapter);
        getAllSubjectQuestion();
    }

    private void getAllSubjectQuestion() {
        Call<SubjectResponse> call = apiService.getListSubject(
                "",
                -1L, // departmentId
                "",
                0, // page
                10000, // size
                "code" // sort
        );

        call.enqueue(new Callback<SubjectResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<SubjectResponse> call,@NonNull Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    SubjectResponse subjectListResponse = response.body();
                    assert subjectListResponse != null;
                    List<SubjectResponse.SubjectItem> subjects = subjectListResponse.getContent();
                    subjectResponses.clear();
                    subjectResponses.addAll(subjects);
                    subjectQuestionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubjectResponse> call,@NonNull Throwable t) {
                // Xử lý thất bại
            }
        });

    }
}