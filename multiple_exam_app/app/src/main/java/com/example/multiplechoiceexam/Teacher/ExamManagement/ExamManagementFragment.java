package com.example.multiplechoiceexam.Teacher.ExamManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.ExamManagement.subject.SubjectActivity;
import com.example.multiplechoiceexam.Teacher.ExamManagement.subjectexamtest.SubjectQuestionActivity;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.student.StudentActivity;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.teacher.TeacherSearchActivity;


public class ExamManagementFragment extends Fragment {

    public ExamManagementFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View root = inflater.inflate(R.layout.fragment_exam_management, container, false);
        ImageView subject = root.findViewById(R.id.offline_subject);
        ImageView test = root.findViewById(R.id.offline_test);
        ImageView imageViewStudentClass = root.findViewById(R.id.student_list_class);
        ImageView imageViewTeacher = root.findViewById(R.id.teacher_class);


        subject.setOnClickListener(view -> startActivity(new Intent(getContext(), SubjectActivity.class)));

        test.setOnClickListener(view -> startActivity(new Intent(getContext(), SubjectQuestionActivity.class)));

        imageViewTeacher.setOnClickListener(view -> startActivity(new Intent(getContext(), TeacherSearchActivity.class)));

        imageViewStudentClass.setOnClickListener(view -> startActivity(new Intent(getContext(), StudentActivity.class)));

        return root;
    }
}