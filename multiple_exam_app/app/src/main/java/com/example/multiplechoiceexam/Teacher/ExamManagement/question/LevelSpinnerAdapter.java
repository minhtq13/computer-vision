package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.question.QuestionLevelEnum;

import java.util.List;

public class LevelSpinnerAdapter extends ArrayAdapter<QuestionLevelEnum> {
    public LevelSpinnerAdapter(@NonNull Context context, @NonNull List<QuestionLevelEnum> levels) {
        super(context, 0, levels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.spiner_level_item, parent, false);
        }

        QuestionLevelEnum level = getItem(position);
        if (level != null) {
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(level.getLevelName());

            if (position == 1) {
                textView.setTextColor(Color.GREEN);
            } else if (position == 2) {
                textView.setTextColor(Color.YELLOW);
            } else if (position == 3) {
                textView.setTextColor(Color.RED);
            }
        }

        return view;
    }
}

