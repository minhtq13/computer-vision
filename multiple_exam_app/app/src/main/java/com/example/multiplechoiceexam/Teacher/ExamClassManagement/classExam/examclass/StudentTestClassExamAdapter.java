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
import com.example.multiplechoiceexam.dto.student.StudentResponse;

import java.util.ArrayList;
import java.util.List;

public class StudentTestClassExamAdapter extends RecyclerView.Adapter<StudentTestClassExamAdapter.StudentTestClassExamViewHolder> {

    private final List<StudentResponse> studentList;
    private final List<Long> selectedStudentIds;


    public StudentTestClassExamAdapter(List<StudentResponse> studentList) {
        this.studentList = studentList;
        this.selectedStudentIds = new ArrayList<>();
    }
    @NonNull
    @Override
    public StudentTestClassExamAdapter.StudentTestClassExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_student_test, parent, false);
        return new StudentTestClassExamViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StudentTestClassExamAdapter.StudentTestClassExamViewHolder holder, int position) {
        StudentResponse student = studentList.get(position);
        holder.textViewStudentName.setText(student.getLastName() + " " +student.getFirstName());
        holder.textViewStudentId.setText(student.getCode());

        holder.checkBoxStudent.setOnCheckedChangeListener(null); // Tránh gọi callback nhiều lần
        holder.checkBoxStudent.setChecked(selectedStudentIds.contains(student.getId()));
        holder.checkBoxStudent.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                selectedStudentIds.add(student.getId());
            } else {
                selectedStudentIds.remove(student.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (studentList != null) {
            return studentList.size();
        } else {
            return 0;
        }
    }
    public List<Long> getSelectedStudentIds() {
        return selectedStudentIds;
    }
    public class StudentTestClassExamViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxStudent;
        TextView textViewStudentName;
        TextView textViewStudentId;
        public StudentTestClassExamViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxStudent = itemView.findViewById(R.id.checkBoxStudent);
            textViewStudentName = itemView.findViewById(R.id.student_name_item_add);
            textViewStudentId = itemView.findViewById(R.id.student_mssv_add);
        }
    }
}
