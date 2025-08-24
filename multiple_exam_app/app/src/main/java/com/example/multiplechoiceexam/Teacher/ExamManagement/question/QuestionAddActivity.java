package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.answer.AnswerRequest;
import com.example.multiplechoiceexam.dto.question.MultiQuestionRequest;
import com.example.multiplechoiceexam.dto.question.QuestionLevelEnum;
import com.example.multiplechoiceexam.dto.question.SingleQuestionRequest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAddActivity extends AppCompatActivity {

    private List<SingleQuestionRequest> questionDataList = new ArrayList<>();
    private QuestionAddAdapter questionAddAdapter;
    private ListView questionListView;
    Button btnAdd, submitQuestionsButton;
    ApiService apiService;
    String code;
    Long chapterNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);

        apiService = RetrofitClient.getApiService(getApplicationContext());

        btnAdd = findViewById(R.id.addQuestionButton);
        questionAddAdapter = new QuestionAddAdapter(this, questionDataList);
        questionListView = findViewById(R.id.questionListView);
        questionListView.setAdapter(questionAddAdapter);
        submitQuestionsButton = findViewById(R.id.submitQuestionsButton);
        code = getIntent().getStringExtra("codeSubject");
        chapterNo = getIntent().getLongExtra("chapterNo",-1);

        submitQuestionsButton.setOnClickListener(view -> sendQuestionsToServer(questionDataList));

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(QuestionAddActivity.this,QuestionAddItemActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void sendQuestionsToServer(List<SingleQuestionRequest> questionDataList) {
        MultiQuestionRequest multipleQuestionRequest = new MultiQuestionRequest();
        multipleQuestionRequest.setLstQuestion(questionDataList);
        multipleQuestionRequest.setChapterId(chapterNo);

        Call<Void> call = apiService.createQuestion(multipleQuestionRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    {
                        Toast.makeText(QuestionAddActivity.this, "thanh cong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuestionAddActivity.this, "Có lỗi xảy ra khi gửi câu hỏi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(QuestionAddActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Lấy dữ liệu từ Intent
            String question = data.getStringExtra("question");
            List<AnswerRequest> answers = data.getParcelableArrayListExtra("answers");
            Integer level = data.getIntExtra("level",-1);
            QuestionLevelEnum questionLevel = (level != -1) ? QuestionLevelEnum.values()[level.intValue()] : null;
            SingleQuestionRequest questionData = new SingleQuestionRequest(question, questionLevel, answers);
            updateListView(questionData);

        }
    }
    public void updateListView(SingleQuestionRequest questionData) {
        questionDataList.add(questionData);
        questionAddAdapter.notifyDataSetChanged();
    }
}