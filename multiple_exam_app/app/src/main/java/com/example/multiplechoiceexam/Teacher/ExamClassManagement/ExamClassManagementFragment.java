package com.example.multiplechoiceexam.Teacher.ExamClassManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.student.StudentAddActivity;

public class ExamClassManagementFragment extends Fragment {

    ImageView  imageViewStudent;
    public ExamClassManagementFragment() {
        // Required empty public constructor
    }




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_exam_class_management, container, false);


        imageViewStudent = root.findViewById(R.id.student_class);

        imageViewStudent.setOnClickListener(view -> startActivity(new Intent(getContext(), StudentAddActivity.class)));
        return root;
    }
}