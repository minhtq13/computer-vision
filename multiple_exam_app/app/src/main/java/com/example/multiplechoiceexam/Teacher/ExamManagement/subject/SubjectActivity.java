package com.example.multiplechoiceexam.Teacher.ExamManagement.subject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.dto.subject.SubjectRequest;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectActivity extends AppCompatActivity {
    RecyclerView subjectRecyclerView;
    SubjectAdapter subjectAdapter;
    List<SubjectResponse.SubjectItem> subjectResponses;
    FloatingActionButton fabAdd;
    ApiService apiService;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        subjectResponses = new ArrayList<>();
        apiService = RetrofitClient.getApiService(getApplicationContext());
        progressBar = findViewById(R.id.progressbar_subject_loading);
        subjectRecyclerView = findViewById(R.id.recycler_subject);
        subjectRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        fabAdd = findViewById(R.id.fab_add);
        subjectAdapter = new SubjectAdapter(this, subjectResponses);
        subjectRecyclerView.setAdapter(subjectAdapter);
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {
            // If the user is a student, hide the buttons
            fabAdd.setVisibility(View.GONE);
        }
        getAllSubjects();


        fabAdd.setOnClickListener(view -> showAddSubjectPopup());

        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý trước sự thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Xử lý khi có sự thay đổi trong nội dung EditText
                String searchText = charSequence.toString();
                searchSubjectByCode(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý sau sự thay đổi
            }
        });
    }

    private void showAddSubjectPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.layout_popup_subject, null);
        Drawable background = new ColorDrawable(Color.WHITE);
        popupView.setBackgroundDrawable(background);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Tạo màn mờ nền
        View backgroundView = new View(SubjectActivity.this);
        backgroundView.setBackgroundColor(Color.parseColor("#80000000")); // Màu đen với độ trong suốt
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addContentView(backgroundView, params);

        int height = getResources().getDisplayMetrics().heightPixels;


        TranslateAnimation slideUp = new TranslateAnimation(0, 0, height, 0);
        slideUp.setDuration(500);
        popupView.startAnimation(slideUp);
        backgroundView.setVisibility(View.VISIBLE);

        popupWindow.setOnDismissListener(() -> {
            backgroundView.setVisibility(View.GONE); // Ẩn lớp mờ khi PopupWindow được đóng
        });

        // Khởi tạo các trường EditText trong PopupWindow
        EditText editTextTitle = popupView.findViewById(R.id.editTextTitle);
        EditText editTextCode = popupView.findViewById(R.id.editTextCode);
        EditText editTextDescription = popupView.findViewById(R.id.editTextDescription);
        EditText editTextCredit = popupView.findViewById(R.id.editTextCredit);
        EditText editTextDepartment = popupView.findViewById(R.id.editTextDepartment);
        Button buttonSave = popupView.findViewById(R.id.confirmButton);

        buttonSave.setOnClickListener(v -> {

            String title = editTextTitle.getText().toString();
            if(title.isEmpty()){
                editTextTitle.setError("Thông tin này không thể bỏ trống");
                return;
            }
            String code = editTextCode.getText().toString();
            if(code.isEmpty()){
                editTextCode.setError("Thông tin này không thể bỏ trống");
                return;
            }
            String description = editTextDescription.getText().toString();
            int credit = 0;

            if (!editTextCredit.getText().toString().isEmpty()) {
                credit = Integer.parseInt(editTextCredit.getText().toString());
            }
            long department = 0;
            if (!editTextDepartment.getText().toString().isEmpty()) {
                department = Integer.parseInt(editTextDepartment.getText().toString());
            }


            SubjectRequest subjectRequest = new SubjectRequest();
            subjectRequest.setTitle(title);
            subjectRequest.setCode(code);
            subjectRequest.setDescription(description);
            subjectRequest.setCredit(credit);
            subjectRequest.setDepartmentId(department);

            addSubject(subjectRequest);

            popupWindow.dismiss();
        });

        popupWindow.showAtLocation(subjectRecyclerView, Gravity.CENTER, 0, 0);
    }

    private void addSubject(SubjectRequest subjectRequest) {
        apiService.addSubject(subjectRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,@NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    getAllSubjects();
                    showUiWhenCallApiSuccess();
                    Toast.makeText(SubjectActivity.this, "Thêm môn học thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    showUiWhenCallApiFalse();
                    Toast.makeText(SubjectActivity.this, "Thêm môn học thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,@NonNull Throwable t) {
                Toast.makeText(SubjectActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllSubjects() {
        Call<SubjectResponse> call = apiService.getListSubject(
                "",
                -1L, // departmentId
                "",
                0, // page
                10000, // size
                "code" // sort
        );

        call.enqueue(new Callback<SubjectResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<SubjectResponse> call,@NonNull Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    SubjectResponse subjectListResponse = response.body();
                    assert subjectListResponse != null;
                    List<SubjectResponse.SubjectItem> subjects = subjectListResponse.getContent();
                    subjectResponses.clear();
                    subjectResponses.addAll(subjects);
                    subjectAdapter.notifyDataSetChanged();
                    showUiWhenCallApiSuccess();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubjectResponse> call,@NonNull Throwable t) {
                // Xử lý thất bại
            }
        });
    }

    private void searchSubjectByCode(String search) {
        Call<SubjectResponse> call = apiService.getListSubject(
                search,
                -1L, // departmentId
                "",
                0, // page
                10000, // size
                "code" // sort
        );

        call.enqueue(new Callback<SubjectResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<SubjectResponse> call,@NonNull Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    SubjectResponse subjectListResponse = response.body();
                    assert subjectListResponse != null;
                    List<SubjectResponse.SubjectItem> subjects = subjectListResponse.getContent();
                    subjectResponses.clear();
                    subjectResponses.addAll(subjects);
                    subjectAdapter.notifyDataSetChanged();
                    showUiWhenCallApiSuccess();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubjectResponse> call,@NonNull Throwable t) {
                // Xử lý thất bại
            }
        });
    }
    private void showUiWhenCallApiSuccess() {
        progressBar.setVisibility(View.GONE);
        subjectRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showUiWhenCallApiFalse() {
        progressBar.setVisibility(View.VISIBLE);
        subjectRecyclerView.setVisibility(View.GONE);
        Toast.makeText(SubjectActivity.this, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show();
    }
}
