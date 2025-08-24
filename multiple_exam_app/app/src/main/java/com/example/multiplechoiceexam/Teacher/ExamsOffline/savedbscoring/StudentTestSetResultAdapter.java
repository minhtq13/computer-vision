package com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.studentTest.StudentTestSetResultDTO;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentTestSetResultAdapter extends RecyclerView.Adapter<StudentTestSetResultAdapter.ViewHolder> {
    private final List<StudentTestSetResultDTO> resultList;
    private final Context context;


    public StudentTestSetResultAdapter(List<StudentTestSetResultDTO> resultList,Context context) {
        this.resultList = resultList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_student_result, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentTestSetResultDTO result = resultList.get(position);

        // Bind data to ViewHolder views
        holder.studentNameTextView.setText(result.getStudentName());
        holder.examClassCodeTextView.setText(result.getExamClassCode());
        holder.testSetCodeTextView.setText(result.getTestSetCode());
        holder.codeStudentTextView.setText(result.getStudentCode());
        holder.numCorrectAnswersTextView.setText(String.valueOf(result.getNumCorrectAnswers()));
        holder.totalPointsTextView.setText(String.format("%.2f", result.getTotalPoints()));
        holder.numMarkedAnswersTextView.setText(String.valueOf(result.getNumTestSetQuestions()));
        holder.totalQuestion.setText(String.valueOf(result.getNumTestSetQuestions()));
        holder.totalQuestionStudent.setText(String.valueOf("/" +result.getNumTestSetQuestions()));

//        // Load image using Glide
//        Glide.with(holder.itemView.getContext())
//                .load(result.getHandledSheetImg())
//                .into(holder.handledSheetImageView);
        if(result.getHandledSheetImg() == null){
            Integer imageUrl = R.drawable.image_handle;
            holder.handledSheetImageView.setOnClickListener(view -> showImageDialog(imageUrl, holder.handledSheetImageView.getContext()));
        }else {
            Picasso.get().load(result.getHandledSheetImg()).into(holder.handledSheetImageView);
            String imageUrl1 = result.getHandledSheetImg();
            holder.handledSheetImageView.setOnClickListener(view -> showImageDialog1(imageUrl1, holder.handledSheetImageView.getContext()));
        }

    }

    private void showImageDialog1(String imageUrl1, Context context) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        // Load the image using Picasso
        Picasso.get().load(imageUrl1).into(dialogImageView);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
    private void showImageDialog(Integer imageUrl, Context context) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        // Load the image using Picasso
        Picasso.get().load(imageUrl).into(dialogImageView);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        TextView examClassCodeTextView;
        TextView testSetCodeTextView;
        TextView numCorrectAnswersTextView, totalQuestion, totalQuestionStudent;
        TextView totalPointsTextView;
        TextView numMarkedAnswersTextView,codeStudentTextView;
        ImageView handledSheetImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.scoring_name_db);
            examClassCodeTextView = itemView.findViewById(R.id.scoring_exam_class_test_code_db);
            testSetCodeTextView = itemView.findViewById(R.id.scoring_test_set_db);
            numCorrectAnswersTextView = itemView.findViewById(R.id.scoring_correct_answer_db);
            totalPointsTextView = itemView.findViewById(R.id.scoring_total_point_db);
            numMarkedAnswersTextView = itemView.findViewById(R.id.scoring_mark_answer_db);
            codeStudentTextView = itemView.findViewById(R.id.scoring_mssv_db);
            handledSheetImageView = itemView.findViewById(R.id.image_scoring);
            totalQuestion = itemView.findViewById(R.id.scoring_handle_scoring_db);
            totalQuestionStudent = itemView.findViewById(R.id.total_ques_student);
        }
    }
}
