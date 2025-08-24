package com.example.multiplechoiceexam.Teacher.ProfileUser.teacher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.dto.auth.ProfileUserDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileTeacherFragment extends Fragment {

    TextView teacherName, teacherEmail,teacherMSSV, teacherSDT,
            teacherBirthday,teacherGender, teacherRole;
    FloatingActionButton buttonTeacherUpdate;
    Context context;
    ApiService apiService;
    ProfileUserDTO teacherProfileFragment;


    public ProfileTeacherFragment() {
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile_user, container, false);
        apiService = RetrofitClient.getApiService(getContext());
        context = getContext();
        teacherName = root.findViewById(R.id.tv_name_profile_teacher);
        teacherMSSV = root.findViewById(R.id.teacher_mssv_profile);
        teacherBirthday = root.findViewById(R.id.teacher_birthday_profile);
        teacherGender = root.findViewById(R.id.teacher_gender_profile);
        teacherEmail = root.findViewById(R.id.teacher_email_profile);
        teacherSDT = root.findViewById(R.id.teacher_number_phone_profile);
        teacherRole =root.findViewById(R.id.teacher_role_profile);
        buttonTeacherUpdate = root.findViewById(R.id.btn_teacher_profile);
        getTeacherProfile();

        buttonTeacherUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), TeacherProfileUpdateActivity.class);
            intent.putExtra("teacher_name", teacherProfileFragment.getName());
            intent.putExtra("teacher_birth", teacherProfileFragment.getBirthDate());
            intent.putExtra("teacher_email", teacherProfileFragment.getEmail());
            intent.putExtra("teacher_gender", teacherProfileFragment.getIdentityType());
            intent.putExtra("teacher_phone", teacherProfileFragment.getPhoneNumber());
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        teacherSDT.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            String phone = teacherSDT.getText().toString();
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        });
        teacherEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            String email = teacherEmail.getText().toString();
            emailIntent.setData(Uri.parse("mailto:" + email));
            startActivity(emailIntent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getTeacherProfile();
    }
    private void getTeacherProfile() {
        apiService.userProfile().enqueue(new Callback<ProfileUserDTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<ProfileUserDTO> call,@NotNull Response<ProfileUserDTO> response) {
                if (response.isSuccessful()) {
                    teacherProfileFragment = response.body();

                    assert teacherProfileFragment != null;
                    teacherName.setText(teacherProfileFragment.getName());
                    teacherBirthday.setText(teacherProfileFragment.getBirthDate());
                    teacherMSSV.setText(teacherProfileFragment.getCode());

                    String email = teacherProfileFragment.getEmail();
                    SpannableString emailUnderLine = new SpannableString(email);
                    emailUnderLine.setSpan(new UnderlineSpan(), 0, email.length(), 0);
                    teacherEmail.setText(emailUnderLine);

                    String phone = teacherProfileFragment.getPhoneNumber();
                    SpannableString phoneUnderLine = new SpannableString(phone);
                    phoneUnderLine.setSpan(new UnderlineSpan(), 0, phone.length(), 0);
                    teacherSDT.setText(phoneUnderLine);
                    teacherGender.setText("Nam");

//                    teacherGender.setText(teacherProfileFragment.getIdentityType());
//                    String genderTmp = teacherProfileFragment.ge();
//                    if(genderTmp.equals("MALE")){
//                        teacherGender.setText("Nam");
//                    }else {
//                        teacherGender.setText("Nữ");
//                    }
                    AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(getContext());
                    List<String> role = accountSharedPreferences.getRoles();
                    if ("ROLE_SUPER_ADMIN".equals(role) || role == null) {
                        teacherRole.setText("Quản trị viên");
                    }else if ("ROLE_TEACHER".equals(role)) {
                        teacherRole.setText("Giảng viên");
                    } else if ("ROLE_STUDENT".equals(role)) {
                        teacherRole.setText("Sinh viên");
                    }
                } else {
                    if (context != null) {
                        Toast.makeText(context, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProfileUserDTO> call,@NotNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
