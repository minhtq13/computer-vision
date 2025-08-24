package com.example.multiplechoiceexam.Teacher.ExamClassManagement.studentTest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.HtmlUtils;
import com.example.multiplechoiceexam.dto.test.ClassTestInfo;
import com.example.multiplechoiceexam.dto.test.TestQuestionAnswerResDTO;
import com.example.multiplechoiceexam.dto.test.TestSetDetailResponse;
import com.example.multiplechoiceexam.dto.test.TestSetSearchReqDTO;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTestDetailExamActivity extends AppCompatActivity implements StudentTestQuestionDetailFragment.OnAnswerSelectedListener{

    private int currentQuestionIndex = 0;
    private int totalQuestions;
    private TextView questionInfoTextView, subjectTest, testName, testCode;
    private CountDownTimer countDownTimer;
    private Integer correctAnswersCount = 0;
    private long timeRemainingMillis;
    private Button buttonNext,buttonPrevious,buttonFinsher,buttonReview;
    private final Map<Integer, String> selectedAnswersMap = new HashMap<>();
    private TestSetDetailResponse testSetDetailResponse;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_test_detail_exam);
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());

        subjectTest = findViewById(R.id.textViewTestInfo);
        testName = findViewById(R.id.textViewTestTitle);
        testCode = findViewById(R.id.textViewTestInfoDate);
        testSetDetailResponse = new TestSetDetailResponse();

        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonPrevious.setOnClickListener(view -> showPreviousQuestion());

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(view -> showNextQuestion());
        ClassTestInfo testInfo = (ClassTestInfo) getIntent().getSerializableExtra("testInfo");
        TestSetSearchReqDTO testSetSearchReqDTO = new TestSetSearchReqDTO();
        assert testInfo != null;
        testSetSearchReqDTO.setTestId(testInfo.getTestId());
        testSetSearchReqDTO.setCode(testInfo.getTestCode());

        buttonFinsher = findViewById(R.id.button_finsher);
        buttonFinsher.setOnClickListener(view -> finish());
        buttonReview = findViewById(R.id.buttonReview);
        buttonReview.setOnClickListener(view -> showReviewDialog());

        Call<TestSetDetailResponse> call = apiService.getTestSetDetail(testSetSearchReqDTO);
        call.enqueue(new Callback<TestSetDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TestSetDetailResponse> call, @NonNull Response<TestSetDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    testSetDetailResponse = response.body();
                    timeRemainingMillis = testSetDetailResponse.getTestSet().getDuration() * 60 * 1000;
                    showQuestionFragment(testSetDetailResponse.getLstQuestion().get(currentQuestionIndex));
                    questionInfoTextView = findViewById(R.id.questionInfoTextView);
                    subjectTest.setText(testSetDetailResponse.getTestSet().getSubjectTitle());
                    testName.setText(testSetDetailResponse.getTestSet().getTestName());
                    testCode.setText(testSetDetailResponse.getTestSet().getTestSetCode());
                    totalQuestions = testSetDetailResponse.getLstQuestion().size() ;
                    updateQuestionInfo();

                    startCountDownTimer();
                    Toast.makeText(StudentTestDetailExamActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StudentTestDetailExamActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TestSetDetailResponse> call, @NonNull Throwable t) {
                Toast.makeText(StudentTestDetailExamActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateQuestionInfo() {
        int currentQuestionNumber = currentQuestionIndex +1;
        String infoText = "Câu " + currentQuestionNumber + " / " + totalQuestions;
        questionInfoTextView.setText(infoText);

    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeRemainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                // Xử lý khi thời gian kết thúc
                Toast.makeText(StudentTestDetailExamActivity.this, "thời gian đã hết", Toast.LENGTH_SHORT).show();
                finishTest();
            }
        }.start();
    }
    private void updateTimerDisplay() {
        long minutes = timeRemainingMillis / (60 * 1000);
        long seconds = (timeRemainingMillis % (60 * 1000)) / 1000;

        TextView timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }
    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestionFragment(testSetDetailResponse.getLstQuestion().get(currentQuestionIndex));
            updateQuestionInfo();
        } else {
            Toast.makeText(this, "Bạn đang ở câu hỏi đầu tiên", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < totalQuestions -1) {
            currentQuestionIndex++;
            showQuestionFragment(testSetDetailResponse.getLstQuestion().get(currentQuestionIndex));
            updateQuestionInfo();
        } else {
            Toast.makeText(this, "Bạn đang ở câu hỏi cuối cùng", Toast.LENGTH_SHORT).show();
        }
    }
    private void showQuestionFragment(TestQuestionAnswerResDTO question) {
        String selectedAnswer = selectedAnswersMap.get(currentQuestionIndex);
        StudentTestQuestionDetailFragment fragment = new StudentTestQuestionDetailFragment();
        fragment.setQuestion(question);
        fragment.setTotalQuestions(totalQuestions);
        fragment.setQuestionNumber(currentQuestionIndex);
        fragment.setAnswerSelectedListener(this);
        if (selectedAnswer != null) {
            fragment.setSelectedAnswer(selectedAnswer);
        }

        if (!isFinishing() && !isDestroyed()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onAnswerSelected(int questionNo, String selectedAnswer) {
        TestQuestionAnswerResDTO currentQuestion = testSetDetailResponse.getLstQuestion().get(currentQuestionIndex);
        String correctAnswer = currentQuestion.getCorrectAnswersString();

        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            correctAnswersCount++;
        }
        selectedAnswersMap.put(currentQuestionIndex, selectedAnswer);
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++;
            showQuestionFragment(testSetDetailResponse.getLstQuestion().get(currentQuestionIndex));
            updateQuestionInfo();
        } else {
            showConfirmationDialog();

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

    private void showReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kiểm tra kết quả");
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < totalQuestions; i++) {
            TestQuestionAnswerResDTO question = testSetDetailResponse.getLstQuestion().get(i);
            String selectedAnswer = selectedAnswersMap.get(i);
            String correctAnswer = question.getCorrectAnswersString();

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
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private LinearLayout.LayoutParams getLayoutParamsWithSpacing() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int spacing = getResources().getDimensionPixelSize(R.dimen.your_spacing_dimension);
        layoutParams.setMargins(0, 0, 0, spacing);
        return layoutParams;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @SuppressLint("SetTextI18n")
    private void finishTest() {
        getSupportFragmentManager().beginTransaction()
                .remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer)))
                .commitAllowingStateLoss();

        // Stop the countdown timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        buttonReview.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonPrevious.setVisibility(View.INVISIBLE);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        ImageView imageWin = findViewById(R.id.image_win);
        ImageView imageFail = findViewById(R.id.image_fail);

        buttonFinsher.setVisibility(View.VISIBLE);
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
}