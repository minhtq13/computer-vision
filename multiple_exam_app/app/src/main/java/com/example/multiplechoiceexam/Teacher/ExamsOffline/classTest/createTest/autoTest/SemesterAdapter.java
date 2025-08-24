package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.test.semeter.SemesterBox;

import java.util.List;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.ViewHolder> {
    private List<SemesterBox> semesterList;
    private OnItemClickListener onItemClickListener;
    Context context;
    public SemesterAdapter(List<SemesterBox> semesterList,Context context) {
        this.semesterList = semesterList;
        this.context = context;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(SemesterBox semesterLists);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_semester, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SemesterBox semesterBox = semesterList.get(position);
        holder.textViewSemesterName.setText(semesterBox.getName());
  //      holder.textViewSemesterCode.setText(semesterBox.getCode());
    }

    @Override
    public int getItemCount() {
        return semesterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSemesterName;

        ViewHolder(View itemView) {
            super(itemView);
            textViewSemesterName = itemView.findViewById(R.id.textViewSemesterName);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(semesterList.get(position));
                }
            });
        }
    }
}
