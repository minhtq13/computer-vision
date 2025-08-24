package com.example.multiplechoiceexam.Teacher.ExamsOffline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclass.ExamClassActivity;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.ClassTestActivity;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring.PreviewScoringActivity;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring.StudentSaveScoringDBActivity;
import com.example.multiplechoiceexam.dto.auth.ProfileUserDTO;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExamsOfflineFragment extends Fragment {
    private TextView userName;
    ProfileUserDTO teacherProfileFragment;

    public ExamsOfflineFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_exams_offline, container, false);
        ApiService apiService = RetrofitClient.getApiService(getContext());
        ImageView export = root.findViewById(R.id.offline_export);
        ImageView mark = root.findViewById(R.id.offline_mark);
        //   ImageView uploadImg = root.findViewById(R.id.offline_upload);
        ImageView previewExam = root.findViewById(R.id.offline_preview);
        ImageView imageClassExam = root.findViewById(R.id.exam_class_image);
        LinearLayout setVisiableStudent = root.findViewById(R.id.student_role_invisible);
        userName = root.findViewById(R.id.username1);
        TextView role = root.findViewById(R.id.role1);
        @SuppressLint("UseRequireInsteadOfGet") AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(Objects.requireNonNull(getContext()));
        List<String> roles = accountSharedPreferences.getRoles();
        if (roles == null || roles.isEmpty()) {
            role.setText("Quản trị viên");
        } else if (roles.contains("ROLE_SUPER_ADMIN")) {
            role.setText("Quản trị viên");
        } else if (roles.contains("ROLE_TEACHER")) {
            role.setText("Giảng viên");
        } else if (roles.contains("ROLE_STUDENT")) {
            role.setText("Sinh viên");
            setVisiableStudent.setVisibility(View.GONE);
        } else {
            role.setText("Giảng viên");
        }
        apiService.userProfile().enqueue(new Callback<ProfileUserDTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ProfileUserDTO> call, @NonNull Response<ProfileUserDTO> response) {
                if (response.isSuccessful()) {
                    teacherProfileFragment = response.body();
                    assert teacherProfileFragment != null;
                    userName.setText(teacherProfileFragment.getName());
                }
            }
            @Override
            public void onFailure(@NonNull Call<ProfileUserDTO> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
        //       userName.setText((CharSequence) accountSharedPreferences.getRoles());

        export.setOnClickListener(view -> startActivity(new Intent(getContext(),ClassTestActivity.class)));
        mark.setOnClickListener(view -> {
            if (!isStudentRole()) {
                startActivity(new Intent(getContext(), PreviewScoringActivity.class));
            } else {
                Toast.makeText(getContext(), "Bạn không có quyền truy cập!", Toast.LENGTH_SHORT).show();
            }
        });

//        uploadImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), UploadImageActivity.class));
//            }
//        });

        previewExam.setOnClickListener(view -> startActivity(new Intent(getContext(), StudentSaveScoringDBActivity.class)));
        imageClassExam.setOnClickListener(view -> startActivity(new Intent(getContext(), ExamClassActivity.class)));

        return root;
    }

    private boolean isStudentRole() {
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(requireContext());
        List<String> roles = accountSharedPreferences.getRoles();
        return roles != null && roles.contains("ROLE_STUDENT");
    }

}