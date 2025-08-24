package com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.BaseUrlUtils;
import com.example.multiplechoiceexam.dto.upload.ScoringPreviewItemDTO;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ScoringPreviewAdapter extends RecyclerView.Adapter<ScoringPreviewAdapter.ScoringPreviewViewHolder> {

    private final List<ScoringPreviewItemDTO> scoringPreviewItemDTOList;
    private List<ScoringPreviewItemDTO> filteredItemList;
    private final Context context;

    public ScoringPreviewAdapter(List<ScoringPreviewItemDTO> scoringPreviewItemDTOList, Context context) {
        this.scoringPreviewItemDTOList = scoringPreviewItemDTOList;
        this.filteredItemList = new ArrayList<>(scoringPreviewItemDTOList);
        this.context = context;
    }
    public void filterList(List<ScoringPreviewItemDTO> filteredList) {
        filteredItemList.clear();
        filteredItemList.addAll(filteredList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ScoringPreviewAdapter.ScoringPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scoring,parent,false);
        return new ScoringPreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoringPreviewAdapter.ScoringPreviewViewHolder holder, int position) {
        ScoringPreviewItemDTO scoringPreviewItemDTO = scoringPreviewItemDTOList.get(position);
        holder.scoringCode.setText(scoringPreviewItemDTO.getStudentCode());
        holder.scoringClassCode.setText(scoringPreviewItemDTO.getExamClassCode());
        holder.scoringTestCode.setText(scoringPreviewItemDTO.getTestSetCode());
        holder.scoringNumMark.setText(String.valueOf(scoringPreviewItemDTO.getNumTestSetQuestions()));
        holder.scoringNumCorrect.setText(String.valueOf(scoringPreviewItemDTO.getNumCorrectAnswers()));
        holder.scoringTotalScore.setText(String.format("%.2f",scoringPreviewItemDTO.getTotalScore()));
        holder.scoringNumberWrong.setText(String.valueOf(scoringPreviewItemDTO.getNumWrongAnswers()));
        holder.totalQuestionStudent.setText(String.valueOf("/" + scoringPreviewItemDTO.getNumTestSetQuestions()));
        holder.scoringPreviewAnswerAdapter.setData(scoringPreviewItemDTO.getDetails());
        String imageUrl = scoringPreviewItemDTO.getHandledScoredImg();
        String imageUrlOrigin =scoringPreviewItemDTO.getOriginalImg();
        String updatedUrl = imageUrlOrigin.replace(BaseUrlUtils.BaseUrl.URL_LOCAL, BaseUrlUtils.BaseUrl.URL_ORIGIN);
        holder.imageOrigin.setOnClickListener(view -> showImageDialog(updatedUrl, holder.imageOrigin.getContext()));
        Picasso.get().load(imageUrl).into(holder.imageScoring);
        holder.imageScoring.setOnClickListener(view -> showImageDialog(imageUrl, holder.imageScoring.getContext()));
    }

    private void showImageDialog(String imageUrl, Context context) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        // Load the image using Picasso
        Picasso.get().load(imageUrl).into(dialogImageView);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return scoringPreviewItemDTOList.size();
    }

    public class ScoringPreviewViewHolder extends RecyclerView.ViewHolder {

        TextView scoringCode, scoringClassCode,scoringTestCode, scoringNumberWrong,
                scoringNumMark,scoringNumCorrect,scoringTotalScore,imageOrigin, totalQuestionStudent;
        ImageView imageScoring;
        RecyclerView recyclerView;
        ScoringPreviewAnswerAdapter scoringPreviewAnswerAdapter;
        @SuppressLint("NotifyDataSetChanged")
        public ScoringPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            scoringCode =itemView.findViewById(R.id.scoring_mssv);
            scoringClassCode =itemView.findViewById(R.id.scoring_exam_class_test_code);
            scoringTestCode =itemView.findViewById(R.id.scoring_test_set);
            scoringNumberWrong =itemView.findViewById(R.id.scoring_handle_scoring_wrong);
            scoringNumMark =itemView.findViewById(R.id.scoring_mark_answer);
            scoringNumCorrect =itemView.findViewById(R.id.scoring_correct_answer);
            scoringTotalScore =itemView.findViewById(R.id.scoring_total_point);
            imageOrigin = itemView.findViewById(R.id.image_origin);
            imageScoring = itemView.findViewById(R.id.image_scoring);
            totalQuestionStudent = itemView.findViewById(R.id.total_student_ques_test);
            recyclerView = itemView.findViewById(R.id.recyclerView);


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            scoringPreviewAnswerAdapter = new ScoringPreviewAnswerAdapter(new ArrayList<>());
            recyclerView.setAdapter(scoringPreviewAnswerAdapter);
            scoringPreviewAnswerAdapter.notifyDataSetChanged();
        }
    }

}
