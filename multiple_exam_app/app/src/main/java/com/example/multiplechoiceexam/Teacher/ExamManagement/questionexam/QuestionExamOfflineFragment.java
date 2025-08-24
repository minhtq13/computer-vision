package com.example.multiplechoiceexam.Teacher.ExamManagement.questionexam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.HtmlUtils;
import com.example.multiplechoiceexam.dto.question.QuestionListDTO;

import java.util.HashMap;
import java.util.Map;

public class QuestionExamOfflineFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private QuestionListDTO question;
    private OnAnswerSelectedListener answerSelectedListener;
    private TextView questionContentTextView;
    private Button submitButton;
    private LinearLayout checkBoxGroupOffline;
    private String selectedAnswer;
    private Map<Integer, String> selectedAnswersMap = new HashMap<>();
    private Integer totalQuestions;

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    private int questionNumber;

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionNo, String selectedAnswer);
    }

    public void setAnswerSelectedListener(OnAnswerSelectedListener listener) {
        this.answerSelectedListener = listener;
    }

    public void setQuestion(QuestionListDTO question) {
        this.question = question;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
        updateUI();
    }

    private void updateUI() {
        StringBuilder selectedAnswersText = new StringBuilder("Đã chọn: ");
        if (selectedAnswersMap.containsKey(questionNumber)) {
            String selectedAnswers = selectedAnswersMap.get(questionNumber);
            selectedAnswersText.append(selectedAnswers);
        } else {
            selectedAnswersText.append("Chưa chọn đáp án nào");
        }
        Log.d("UpdateUI", selectedAnswersText.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_exam_offline, container, false);
        initializeViews(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initializeViews(View view) {
        questionContentTextView = view.findViewById(R.id.questionTextOffline);
        checkBoxGroupOffline = view.findViewById(R.id.checkBoxGroupOffline);
        submitButton = view.findViewById(R.id.submitButtonOffline);

        populateQuestionContent();
        populateAnswerOptions();
        updateUI();
        if (questionNumber == (totalQuestions - 1)) {
            submitButton.setText("Hoàn thành");
        }
        submitButton.setOnClickListener(v -> submitAnswer());
    }

    private void populateQuestionContent() {
        if (question != null) {
            HtmlUtils.setHtmlTextWithImage(questionContentTextView, question.getContent());
        }
    }

    private void populateAnswerOptions() {
        checkBoxGroupOffline.removeAllViews();

        if (question != null && question.getLstAnswer() != null) {
            for (int i = 0; i < question.getLstAnswer().size(); i++) {
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setId(i);
                String htmlTransferAnswer = question.getLstAnswer().get(i).getContent();
                HtmlUtils.setHtmlTextWithImage(checkBox, htmlTransferAnswer);

                checkBoxGroupOffline.addView(checkBox);
            }
        }

    }

    private void submitAnswer() {
        if (answerSelectedListener != null) {
            StringBuilder selectedAnswers = new StringBuilder();

            for (int i = 0; i < checkBoxGroupOffline.getChildCount(); i++) {
                View checkBoxView = checkBoxGroupOffline.getChildAt(i);
                if (checkBoxView instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) checkBoxView;
                    if (checkBox.isChecked()) {
                        String selectedAnswer = checkBox.getText().toString();
                        selectedAnswers.append(selectedAnswer).append(", ");
                    }
                }
            }

            if (selectedAnswers.length() > 0) {
                selectedAnswers.delete(selectedAnswers.length() - 2, selectedAnswers.length());
            }

            selectedAnswersMap.put(questionNumber, selectedAnswers.toString());
            answerSelectedListener.onAnswerSelected(question.getId().intValue(), selectedAnswers.toString());

        }
    }
}
