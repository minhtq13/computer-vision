package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.question.SingleQuestionRequest;

import java.util.List;

public class QuestionAddAdapter extends ArrayAdapter<SingleQuestionRequest> {

    public QuestionAddAdapter(Context context, List<SingleQuestionRequest> questionDataList) {
        super(context, 0, questionDataList);
    }

    static class ViewHolder {
        TextView questionTextView;
        TextView levelTextView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.question_add_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.questionTextView = convertView.findViewById(R.id.questionTextView);
            viewHolder.levelTextView = convertView.findViewById(R.id.levelTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SingleQuestionRequest questionData = getItem(position);

        if (questionData != null) {
            viewHolder.questionTextView.setText("Câu");
            viewHolder.levelTextView.setText("Level: " + questionData.getLevel());
        }

        return convertView;
    }
}

