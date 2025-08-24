package com.example.multiplechoiceexam.Teacher.ExamManagement.subjectexamtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.ExamManagement.question.QuestionActivity;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;

import java.util.List;

public class SubjectQuestionAdapter extends RecyclerView.Adapter<SubjectQuestionAdapter.SubjectQuestionViewHolder> {

    Context context;
    List<SubjectResponse.SubjectItem> subjectResponseList;



    public SubjectQuestionAdapter(Context context, List<SubjectResponse.SubjectItem> subjectResponseList) {
        this.context = context;
        this.subjectResponseList = subjectResponseList;
    }

    @NonNull
    @Override
    public SubjectQuestionAdapter.SubjectQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_item ,parent, false);
        return new SubjectQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectQuestionAdapter.SubjectQuestionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SubjectResponse.SubjectItem subjectItem = subjectResponseList.get(position);
        holder.subQuestionCode.setText(subjectItem.getCode());
        holder.subQuestionTitle.setText(subjectItem.getTitle());
        holder.itemView.setOnClickListener(view -> {
            String code = subjectResponseList.get(position).getCode();
            Intent intent = new Intent(context, QuestionActivity.class);
            intent.putExtra("code", code);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (subjectResponseList != null) {
            return subjectResponseList.size();
        } else {
            return 0;
        }
    }

    public class SubjectQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView subQuestionCode, subQuestionTitle;
        public SubjectQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            subQuestionCode = itemView.findViewById(R.id.subject1_code_question);
            subQuestionTitle = itemView.findViewById(R.id.subject1_title_question);
        }
    }
}
