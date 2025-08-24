package com.example.multiplechoiceexam.Teacher.ProfileUser.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;

import com.example.multiplechoiceexam.dto.student.StudentProfileFragment;



public class ProfileStudentFragment extends Fragment {

    TextView studentName, studentEmail,studentMSSV, studentSDT,
            studentCourse,studentBirthday,studentGender,studentRole;
    Button buttonStudentProfile;
    StudentProfileFragment studentProfileFragment;
    ApiService apiService;



    public ProfileStudentFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile_student, container, false);

        apiService = RetrofitClient.getApiService(getContext());
    //    imageBackStudent =root.findViewById(R.id.image_back_student_profile);
        studentName = root.findViewById(R.id.tv_name_profile_student);
        studentMSSV = root.findViewById(R.id.student_mssv_profile_1);
        studentBirthday = root.findViewById(R.id.student_profile_birthday_profile1);
        studentCourse = root.findViewById(R.id.student_profile_course_profile);
        studentGender = root.findViewById(R.id.student_profile_gender_profile1);
        studentEmail = root.findViewById(R.id.student_email_profile1);
        studentSDT = root.findViewById(R.id.student_number_phone_profile1);
        studentRole = root.findViewById(R.id.student_role_profile1);
        buttonStudentProfile = root.findViewById(R.id.btn_student_profile);
        getProfileStudent();
//
//        imageBackStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (savedInstanceState == null) {
//                    ExamsOfflineFragment defaultFragment = new ExamsOfflineFragment();
//                // Lấy FragmentManager
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.teacherMode_frame, defaultFragment)
//                        .addToBackStack(null)
//                        .commit();
//                if (getActivity() instanceof BottomNavigationVisibilityListener) {
//                    ((BottomNavigationVisibilityListener) getActivity()).setBottomNavigationVisibility(View.VISIBLE);
//                }
//            }
//            }
//        });
        buttonStudentProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), StudentProfileUpdateActivity.class);
            intent.putExtra("student_name", studentProfileFragment.getFullName());
            intent.putExtra("student_birth", studentProfileFragment.getBirthday());
            intent.putExtra("student_course", studentProfileFragment.getCourse());
            intent.putExtra("student_email", studentProfileFragment.getEmail());
//        intent.putExtra("student_gender", studentResponse.getGender());
            intent.putExtra("student_phone", studentProfileFragment.getPhoneNumber());
            startActivity(intent);
        });


        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        getProfileStudent();
    }
    private void getProfileStudent() {
//        apiService.userProfile().enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    ResponseBody responseBody = response.body();
//                    if (responseBody != null) {
//                        try {
//                            String jsonString = responseBody.string();
//                            Gson gson = new Gson();
//                            studentProfileFragment = gson.fromJson(jsonString, StudentProfileFragment.class);
//                            studentName.setText(studentProfileFragment.getFullName());
//                            studentBirthday.setText(studentProfileFragment.getBirthday());
//                            studentMSSV.setText(studentProfileFragment.getCode());
//                            studentEmail.setText(studentProfileFragment.getEmail());
//                            studentCourse.setText(String.valueOf(studentProfileFragment.getCourse()));
//                            studentSDT.setText(studentProfileFragment.getPhoneNumber());
//                            String studentGenderTmp = studentProfileFragment.getGender();
//                            if(studentGenderTmp.equals("MALE")){
//                                studentGender.setText("Nam");
//                            }else {
//                                studentGender.setText("Nữ");
//                            }
//                            List<String> rolesList = studentProfileFragment.getRoles();
//                            if (rolesList != null && !rolesList.isEmpty()) {
//                                for (String role : rolesList) {
//                                    if ("ROLE_TEACHER".equals(role)) {
//                                        studentRole.setText("Giảng viên");
//                                        break;
//                                    } else if ("ROLE_STUDENT".equals(role)) {
//                                        studentRole.setText("Sinh viên");
//                                        break;
//                                    }
//                                }
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    Toast.makeText(getContext(), "Lấy profile thất bại!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<ResponseBody> call,@NotNull Throwable t) {
//                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}