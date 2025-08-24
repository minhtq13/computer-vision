package com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.examClass.ICommonIdCode;

import java.util.List;

public class ExamClassCodeSearchAdapter extends RecyclerView.Adapter<ExamClassCodeSearchAdapter.SemesterViewHolder> {
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private final List<ICommonIdCode> iCommonIdCodeNames;

    public ExamClassCodeSearchAdapter(Context context, List<ICommonIdCode> iCommonIdCodeNames) {
        this.context = context;
        this.iCommonIdCodeNames = iCommonIdCodeNames;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(ICommonIdCode semesterLists);
    }
    @NonNull
    @Override
    public ExamClassCodeSearchAdapter.SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_semester, parent, false);
        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamClassCodeSearchAdapter.SemesterViewHolder holder, int position) {
        ICommonIdCode iCommonIdCodeName = iCommonIdCodeNames.get(position);
        holder.semesterId.setText(iCommonIdCodeName.getCode());
    }

    @Override
    public int getItemCount() {
        if(iCommonIdCodeNames != null){
            return iCommonIdCodeNames.size();
        }
        return 0;
    }

    public class SemesterViewHolder extends RecyclerView.ViewHolder {
        TextView semesterId;
        public SemesterViewHolder(@NonNull View itemView) {
            super(itemView);
            semesterId = itemView.findViewById(R.id.exam_class_code_id);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(iCommonIdCodeNames.get(position));
                }
            });
        }

    }
}
