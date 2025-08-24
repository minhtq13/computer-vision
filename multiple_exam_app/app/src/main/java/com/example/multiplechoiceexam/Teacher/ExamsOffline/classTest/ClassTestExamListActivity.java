package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.dto.test.TestSetGenerateReqDTO;
import com.example.multiplechoiceexam.dto.test.TestSetPreviewDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassTestExamListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView classCodeTextView;

    FloatingActionButton btnClassTestAdd;
    ApiService apiService;

    Integer testId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_test_exam_list);

        recyclerView = findViewById(R.id.recycler_class_test_exam);
        classCodeTextView = findViewById(R.id.test_code_title);
        btnClassTestAdd = findViewById(R.id.btn_add_test);

        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {

            btnClassTestAdd.setVisibility(View.GONE);
        }
        findViewById(R.id.imageView_classTestExam_back).setOnClickListener(v -> finish());
        apiService = RetrofitClient.getApiService(getApplicationContext());
        btnClassTestAdd.setOnClickListener(view -> showAddClassTestExam());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String testCode = getIntent().getStringExtra("testCode");
        classCodeTextView.setText(testCode);

        testId = getIntent().getIntExtra("testId",-1);
        String[] lstTestSetCodeArray = getIntent().getStringArrayExtra("lstTestSetCodeArray");

        List<String> lstTestSetCodeList = Arrays.asList(lstTestSetCodeArray);

        ClassTestExamListAdapter adapter = new ClassTestExamListAdapter(testId, lstTestSetCodeList, this);
        recyclerView.setAdapter(adapter);
    }

    private void showAddClassTestExam() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_popup_class_test_add, null);
        Drawable background = new ColorDrawable(Color.WHITE);
        popupView.setBackgroundDrawable(background);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View backgroundView = new View(ClassTestExamListActivity.this);
        backgroundView.setBackgroundColor(Color.parseColor("#80000000"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addContentView(backgroundView, params);

        int height = getResources().getDisplayMetrics().heightPixels;


        TranslateAnimation slideUp = new TranslateAnimation(0, 0, height, 0);
        slideUp.setDuration(500);
        popupView.startAnimation(slideUp);
        backgroundView.setVisibility(View.VISIBLE);

        popupWindow.setOnDismissListener(() -> backgroundView.setVisibility(View.GONE));

        EditText editNumber = popupView.findViewById(R.id.edit_class_exam_add);
        Button buttonSave = popupView.findViewById(R.id.btnSaveClassTest);

        buttonSave.setOnClickListener(v -> {
            Integer editNum = Integer.valueOf(editNumber.getText().toString());
            TestSetGenerateReqDTO testSetGenerateReqDTO = new TestSetGenerateReqDTO();
            testSetGenerateReqDTO.setTestId(Long.valueOf(testId));
            testSetGenerateReqDTO.setNumOfTestSet(editNum);
            addClassTestExam(testSetGenerateReqDTO);


            popupWindow.dismiss();
        });

        popupWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
    }

    private void addClassTestExam(TestSetGenerateReqDTO testSetGenerateReqDTO) {
        Call<List<TestSetPreviewDTO>> call = apiService.createTestSetFromTest(testSetGenerateReqDTO);

        call.enqueue(new Callback<List<TestSetPreviewDTO>>() {
            @Override
            public void onResponse(@NotNull Call<List<TestSetPreviewDTO>> call, @NotNull Response<List<TestSetPreviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(ClassTestExamListActivity.this, "thành công!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(ClassTestExamListActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TestSetPreviewDTO>> call,@NotNull Throwable t) {
                Toast.makeText(ClassTestExamListActivity.this, "lỗi call", Toast.LENGTH_SHORT).show();
            }
        });

    }
}