package com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclass;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.teacher.TeacherResponse;

import java.util.ArrayList;
import java.util.List;

public class TeacherTestClassExamAdapter extends RecyclerView.Adapter<TeacherTestClassExamAdapter.TeacherTestClassExamViewHolder>{

    private final List<TeacherResponse> teacherResponses;
    private final List<Long> selectedTeacherIds;

    public TeacherTestClassExamAdapter(List<TeacherResponse> teacherResponses) {
        this.teacherResponses = teacherResponses;
        this.selectedTeacherIds = new ArrayList<>();
    }

    @NonNull
    @Override
    public TeacherTestClassExamAdapter.TeacherTestClassExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_student_test, parent, false);
        return new TeacherTestClassExamViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TeacherTestClassExamAdapter.TeacherTestClassExamViewHolder holder, int position) {
        TeacherResponse teacher = teacherResponses.get(position);
        holder.textViewTeacherName.setText(teacher.getLastName() + " " +teacher.getFirstName());
        holder.textViewTeacherId.setText(teacher.getCode());

        holder.checkBoxTeacher.setOnCheckedChangeListener(null);
        holder.checkBoxTeacher.setChecked(selectedTeacherIds.contains(teacher.getId()));
        holder.checkBoxTeacher.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                selectedTeacherIds.add(teacher.getId());
            } else {
                selectedTeacherIds.remove(teacher.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (teacherResponses != null) {
            return teacherResponses.size();
        } else {
            return 0;
        }
    }
    public List<Long> getSelectedTeacherIds() {
        return selectedTeacherIds;
    }

    public class TeacherTestClassExamViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxTeacher;
        TextView textViewTeacherName;
        TextView textViewTeacherId;
        public TeacherTestClassExamViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTeacher = itemView.findViewById(R.id.checkBoxStudent);
            textViewTeacherName = itemView.findViewById(R.id.student_name_item_add);
            textViewTeacherId = itemView.findViewById(R.id.student_mssv_add);
        }
    }
}
