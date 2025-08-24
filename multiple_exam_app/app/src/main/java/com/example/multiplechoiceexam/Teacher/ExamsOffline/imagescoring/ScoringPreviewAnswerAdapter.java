package com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.upload.HandledAnswerDTO;

import java.util.List;

public class ScoringPreviewAnswerAdapter extends RecyclerView.Adapter<ScoringPreviewAnswerAdapter.ViewHolder> {

    private List<HandledAnswerDTO> handledAnswerDTOS;

    public ScoringPreviewAnswerAdapter(List<HandledAnswerDTO> handledAnswerDTOS) {
        this.handledAnswerDTOS = handledAnswerDTOS;
    }
    public void setData(List<HandledAnswerDTO> handledAnswerDTOS) {
        this.handledAnswerDTOS = handledAnswerDTOS;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_scoring_qa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HandledAnswerDTO handledAnswerDTO = handledAnswerDTOS.get(position);

        // Bind data to the views
        holder.questionNumberTextView.setText(String.valueOf(handledAnswerDTO.getQuestionNo()));
        holder.selectedAnswerTextView.setText(handledAnswerDTO.getSelectedAnswers());
        holder.correctAnswer.setText(handledAnswerDTO.getCorrectAnswers());
    }

    @Override
    public int getItemCount() {
        return handledAnswerDTOS.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumberTextView;
        TextView selectedAnswerTextView ,correctAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberTextView = itemView.findViewById(R.id.questionNumber);
            selectedAnswerTextView = itemView.findViewById(R.id.selectedAnswer);
            correctAnswer =itemView.findViewById(R.id.correctAnswer);
        }
    }
}
