package com.example.multiplechoiceexam.Teacher.ExamManagement.questionexam;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.HtmlUtils;
import com.example.multiplechoiceexam.dto.answer.AnswerResponse;
import com.example.multiplechoiceexam.dto.question.QuestionLevelEnum;
import com.example.multiplechoiceexam.dto.question.QuestionListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionExamOfflineActivity extends AppCompatActivity implements QuestionExamOfflineFragment.OnAnswerSelectedListener {

    private int currentQuestionIndex = 0;
    private int totalQuestions;
    private TextView questionInfoTextView, subjectTest, testName, testCode;
    private Button buttonNext, buttonPrevious, buttonFinish, buttonReview;
    private ApiService apiService;
    private List<QuestionListDTO> questionList;
    private Integer correctAnswersCount = 0;
    private Map<Integer, String> selectedAnswersMap = new HashMap<>();
    private Long subjectId;
    private String code;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_exam_offline);

        apiService = RetrofitClient.getApiService(getApplicationContext());

        subjectId = getIntent().getLongExtra("subjectId", -1L);
        code = getIntent().getStringExtra("code");

        subjectTest = findViewById(R.id.textViewTestInfoOffline);
        testName = findViewById(R.id.textViewTestTitleOffline);
        testCode = findViewById(R.id.textViewTestInfoDateOffline);

        buttonPrevious = findViewById(R.id.buttonPreviousOffline);
        buttonPrevious.setOnClickListener(view -> showPreviousQuestion());

        buttonNext = findViewById(R.id.buttonNextOffline);
        buttonNext.setOnClickListener(view -> showNextQuestion());

        buttonFinish = findViewById(R.id.button_finsherOffline);
        buttonFinish.setOnClickListener(view -> finish());

        buttonReview = findViewById(R.id.buttonReviewOffline);
        buttonReview.setOnClickListener(view -> showReviewDialog());

        questionInfoTextView = findViewById(R.id.questionInfoTextViewOffline);

        loadTestData(); // Hàm này cần được gọi để tải dữ liệu câu hỏi từ server
    }

    private void loadTestData() {
        Call<List<QuestionListDTO>> call = apiService.getListQuestion(subjectId, code, "", -1L, QuestionLevelEnum.ALL);

        call.enqueue(new Callback<List<QuestionListDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuestionListDTO>> call, @NonNull Response<List<QuestionListDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionList = response.body();
                    totalQuestions = questionList.size();

                    // Hiển thị câu hỏi đầu tiên
                    showQuestionFragment(questionList.get(currentQuestionIndex));
                    updateQuestionInfo();
                } else {
                    Toast.makeText(QuestionExamOfflineActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<QuestionListDTO>> call, @NonNull Throwable t) {
                if (!isFinishing()) {
                    Toast.makeText(QuestionExamOfflineActivity.this, "Error loading questions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateQuestionInfo() {
        int currentQuestionNumber = currentQuestionIndex + 1;
        String infoText = "Câu " + currentQuestionNumber + " / " + totalQuestions;
        questionInfoTextView.setText(infoText);
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++;
            showQuestionFragment(questionList.get(currentQuestionIndex));
            updateQuestionInfo();
        } else {
            Toast.makeText(this, "You are on the last question", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestionFragment(questionList.get(currentQuestionIndex));
            updateQuestionInfo();
        } else {
            Toast.makeText(this, "You are on the first question", Toast.LENGTH_SHORT).show();
        }
    }
    private void showQuestionFragment(QuestionListDTO question) {
        String selectedAnswer = selectedAnswersMap.get(currentQuestionIndex);
        QuestionExamOfflineFragment fragment = new QuestionExamOfflineFragment();
        fragment.setQuestion(question);
        fragment.setTotalQuestions(totalQuestions);
        fragment.setQuestionNumber(currentQuestionIndex);
        fragment.setAnswerSelectedListener(this);
        if (selectedAnswer != null) {
            fragment.setSelectedAnswer(selectedAnswer);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerOffline, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onAnswerSelected(int questionNo, String selectedAnswer) {
        QuestionListDTO currentQuestion = questionList.get(currentQuestionIndex);
        String correctAnswer = getCorrectAnswersString(currentQuestion);

        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            correctAnswersCount++;
        }
        selectedAnswersMap.put(currentQuestionIndex, selectedAnswer);
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++;
            showQuestionFragment(questionList.get(currentQuestionIndex));
            updateQuestionInfo();
        } else {
            if (!isFinishing()) {
                showConfirmationDialog();
            }
        }
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận nộp bài");
        builder.setMessage("Bạn có chắc chắn muốn nộp bài?");
        builder.setPositiveButton("Có", (dialog, which) -> finishTest());
        builder.setNegativeButton("Không", (dialog, which) -> {
            // Đóng dialog
            dialog.dismiss();
        });

        builder.create().show();
    }
    @SuppressLint("SetTextI18n")
    private void finishTest() {
        getSupportFragmentManager().beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.fragmentContainerOffline))
                .commitAllowingStateLoss();


        buttonReview.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonPrevious.setVisibility(View.INVISIBLE);
        TextView scoreTextView = findViewById(R.id.scoreTextViewOffline);
        ImageView imageWin = findViewById(R.id.image_win_question);
        ImageView imageFail = findViewById(R.id.image_fail_question);

        buttonFinish.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        scoreTextView.setText("Điểm: " + correctAnswersCount);

        if (correctAnswersCount >= totalQuestions / 2) {
            // Hiển thị hình ảnh win
            imageWin.setVisibility(View.VISIBLE);
            imageFail.setVisibility(View.GONE);
        } else {
            // Hiển thị hình ảnh fail
            imageWin.setVisibility(View.GONE);
            imageFail.setVisibility(View.VISIBLE);
        }
    }
    private List<String> getCorrectAnswers(QuestionListDTO question) {
        List<String> correctAnswers = new ArrayList<>();
        List<AnswerResponse> answers = question.getLstAnswer();

        for (AnswerResponse answer : answers) {
            if (answer.getCorrected()) {
                correctAnswers.add(cleanHtmlTags(answer.getContent()));
            }
        }

        return correctAnswers;
    }

    private String getCorrectAnswersString(QuestionListDTO question) {
        List<String> correctAnswers = getCorrectAnswers(question);
        return TextUtils.join(", ", correctAnswers);
    }


    private String cleanHtmlTags(String content) {
        return HtmlUtils.getTextFromHtml(content);
    }

    private void showReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kiểm tra kết quả");

        ScrollView scrollView = new ScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < totalQuestions; i++) {
            QuestionListDTO question = questionList.get(i);
            String selectedAnswer = selectedAnswersMap.get(i);
            String correctAnswer = getCorrectAnswersString(question);

            // Create TextView for question, selected answer, and correct answer
            TextView questionTextView = new TextView(this);
            HtmlUtils.setHtmlTextWithImage(questionTextView, "Câu " + (i + 1) + ": " + question.getContent());
            layout.addView(questionTextView, getLayoutParamsWithSpacing());

            TextView selectedAnswerTextView = new TextView(this);
            HtmlUtils.setHtmlTextWithImage(selectedAnswerTextView, "Đáp án chọn: " + selectedAnswer);
            layout.addView(selectedAnswerTextView, getLayoutParamsWithSpacing());

            TextView correctAnswerTextView = new TextView(this);
            HtmlUtils.setHtmlTextWithImage(correctAnswerTextView, "Đáp án đúng: " + correctAnswer);
            layout.addView(correctAnswerTextView, getLayoutParamsWithSpacing());

            // Add some spacing between questions
            layout.addView(new Space(this), getLayoutParamsWithSpacing());
        }

        scrollView.addView(layout);
        builder.setView(scrollView);
        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private LinearLayout.LayoutParams getLayoutParamsWithSpacing() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int spacing = getResources().getDimensionPixelSize(R.dimen.your_spacing_dimension);
        layoutParams.setMargins(20, 0, 0, spacing);
        return layoutParams;
    }




}
