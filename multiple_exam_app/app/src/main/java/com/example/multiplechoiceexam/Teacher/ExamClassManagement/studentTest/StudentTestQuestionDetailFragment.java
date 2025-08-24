package com.example.multiplechoiceexam.Teacher.ExamClassManagement.studentTest;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.HtmlUtils;
import com.example.multiplechoiceexam.dto.test.TestQuestionAnswerResDTO;
import com.example.multiplechoiceexam.dto.test.TestSetAnswerResDTO;


import java.util.HashMap;
import java.util.Map;


public class StudentTestQuestionDetailFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private TestQuestionAnswerResDTO question;
    private OnAnswerSelectedListener answerSelectedListener;
    private LinearLayout checkBoxGroupOfflineExam;
    private String selectedAnswer;
    private int questionNumber;

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
        updateUI();
    }
    private Integer totalQuestions;

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionNo, String selectedAnswer);
    }

    public void setQuestion(TestQuestionAnswerResDTO question) {
        this.question = question;
    }

    public void setAnswerSelectedListener(OnAnswerSelectedListener listener) {
        this.answerSelectedListener = listener;
    }

    public StudentTestQuestionDetailFragment() {
        // Required empty public constructor
    }
    private final Map<Integer, String> selectedAnswersMap = new HashMap<>();


    @SuppressLint({"MissingInflatedId", "ObsoleteSdkInt", "UseRequireInsteadOfGet", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_test_question_detail, container, false);

        TextView questionText = view.findViewById(R.id.questionText);
        checkBoxGroupOfflineExam = view.findViewById(R.id.checkBoxGroupOfflineExam);
        Button submitButton = view.findViewById(R.id.submitButton);

        String htmlTransfer = question.getContent();
        HtmlUtils.setHtmlTextWithImage(questionText, htmlTransfer);

        for (TestSetAnswerResDTO answer : question.getAnswers()) {
            CheckBox checkBox = new CheckBox(getContext());
            String htmlTransferAnswer = answer.getContent();
            HtmlUtils.setHtmlTextWithImage(checkBox, htmlTransferAnswer);

            checkBox.setOnCheckedChangeListener(this);

            checkBoxGroupOfflineExam.addView(checkBox);
        }
        if (questionNumber == (totalQuestions - 1)) {
            submitButton.setText("Hoàn thành");
        }
        submitButton.setOnClickListener(v -> submitAnswer());
        if (selectedAnswer != null) {
            updateUI();
        }

        return view;
    }

    private void submitAnswer() {
        StringBuilder selectedAnswers = new StringBuilder();

        for (int i = 0; i < checkBoxGroupOfflineExam.getChildCount(); i++) {
            View checkBoxView = checkBoxGroupOfflineExam.getChildAt(i);
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
        if (answerSelectedListener != null) {
            answerSelectedListener.onAnswerSelected(question.getQuestionNo(), selectedAnswers.toString());
        }
    }

    private void updateUI() {
        if (checkBoxGroupOfflineExam != null) {
            for (int i = 0; i < checkBoxGroupOfflineExam.getChildCount(); i++) {
                View checkBoxView = checkBoxGroupOfflineExam.getChildAt(i);
                if (checkBoxView instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) checkBoxView;
                    if (checkBox.getText().toString().equals(selectedAnswer)) {
                        checkBox.setChecked(true);
                    }
                }
            }
        }
    }

}