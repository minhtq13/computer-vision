package com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.test.TestClassResponse;

import java.util.List;

public class TessClassExamIdAdapter extends RecyclerView.Adapter<TessClassExamIdAdapter.TessClassExamIdViewHolder> {

    private final List<TestClassResponse.TestItem> classResponses;
    private OnItemClickListener onItemClickListener;

    Context context;

    public TessClassExamIdAdapter(List<TestClassResponse.TestItem> classResponses, Context context) {
        this.classResponses = classResponses;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(TestClassResponse.TestItem classResponseTest);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public TessClassExamIdAdapter.TessClassExamIdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_class_id, parent, false);
        return new TessClassExamIdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TessClassExamIdAdapter.TessClassExamIdViewHolder holder, int position) {
        TestClassResponse.TestItem classResponse = classResponses.get(position);
        holder.classTitle.setText(classResponse.getName());
        holder.classCode.setText(classResponse.getSubjectName());

    }

    @Override
    public int getItemCount() {
        if (classResponses != null) {
            return classResponses.size();
        } else {
            return 0;
        }
    }

    public class TessClassExamIdViewHolder extends RecyclerView.ViewHolder {

        TextView classTitle, classCode;
        public TessClassExamIdViewHolder(@NonNull View itemView) {
            super(itemView);
            classTitle = itemView.findViewById(R.id.textViewMonHoc);
            classCode = itemView.findViewById(R.id.textViewMonHocCode);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(classResponses.get(position));
                }
            });

        }
    }
}
