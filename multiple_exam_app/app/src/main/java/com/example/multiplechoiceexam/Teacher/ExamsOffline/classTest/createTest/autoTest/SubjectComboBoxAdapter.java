package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;

import java.util.List;

public class SubjectComboBoxAdapter extends RecyclerView.Adapter<SubjectComboBoxAdapter.ViewHolder> {

    List<SubjectResponse.SubjectItem> subjects;
    Context context;
    private OnItemClickListener onItemClickListener;

    public SubjectComboBoxAdapter(Context context, List<SubjectResponse.SubjectItem> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(SubjectResponse.SubjectItem subjectItem);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_combox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectResponse.SubjectItem subject = subjects.get(position);
        holder.textViewSubjectName.setText(subject.getTitle());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSubjectName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubjectName = itemView.findViewById(R.id.textViewSubjectName);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(subjects.get(position));
                }
            });
        }
    }
}
