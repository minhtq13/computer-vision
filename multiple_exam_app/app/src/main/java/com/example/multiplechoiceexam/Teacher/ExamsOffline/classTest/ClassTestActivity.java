package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.ClassTestCreateActivity;
import com.example.multiplechoiceexam.Utils.UpdateInterface;
import com.example.multiplechoiceexam.dto.test.TestClassResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassTestActivity extends AppCompatActivity implements UpdateInterface {

    RecyclerView recyclerView;
    List<TestClassResponse.TestItem> testItems;
    FloatingActionButton floatingTest;
    EditText textSearch;
    ProgressBar progressBar;
    ApiService apiService;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_test);

        recyclerView = findViewById(R.id.recycler_test);
        floatingTest = findViewById(R.id.fab_add_test);
        textSearch = findViewById(R.id.editTextSearch);
        progressBar = findViewById(R.id.progressbar_classTest_loading);
        testItems = new ArrayList<>();
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {
            floatingTest.setVisibility(View.GONE);
        }
        apiService = RetrofitClient.getApiService(getApplicationContext());
        findViewById(R.id.imageView_classTest_back).setOnClickListener(v -> finish());
        floatingTest.setOnClickListener(view -> {
            Intent intent = new Intent(ClassTestActivity.this, ClassTestCreateActivity.class);
            startActivity(intent);
        });
        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý trước sự thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Xử lý khi có sự thay đổi trong nội dung EditText
                String searchText = charSequence.toString();
                searchClassTest(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý sau sự thay đổi
            }
        });

      getAllTestCall();
    }

    private void searchClassTest(String searchText) {
        Call<TestClassResponse> call = apiService.getTestList(
                -1L,
                "ALL",
                "",
                "",
                -1L,
                searchText,
                0,
                10000,
                "modifiedAt"
        );

        call.enqueue(new Callback<TestClassResponse>() {
            @Override
            public void onResponse(@NotNull Call<TestClassResponse> call,@NotNull Response<TestClassResponse> response) {
                if (response.isSuccessful()) {
                    TestClassResponse testClassResponse = response.body();
                    assert testClassResponse != null;
                    testItems = testClassResponse.getContent();
                    getAllTestClass(testItems);
                    showUiWhenCallApiSuccess();
                } else {
                    showUiWhenCallApiFalse();

                }
            }
            @Override
            public void onFailure(@NotNull Call<TestClassResponse> call,@NotNull Throwable t) {
                Toast.makeText(ClassTestActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAllTestCall() {
        Call<TestClassResponse> call = apiService.getTestList(
                -1L,
                "ALL",
                "",
                "",
                -1L,
                "",
                0,
                10000,
                "modifiedAt"
        );

        call.enqueue(new Callback<TestClassResponse>() {
            @Override
            public void onResponse(@NotNull Call<TestClassResponse> call,@NotNull Response<TestClassResponse> response) {
                if (response.isSuccessful()) {
                    TestClassResponse testClassResponse = response.body();
                    assert testClassResponse != null;
                    testItems = testClassResponse.getContent();
                    getAllTestClass(testItems);
                    showUiWhenCallApiSuccess();

                } else {
                    showUiWhenCallApiFalse();

                }
            }
            @Override
            public void onFailure(@NotNull Call<TestClassResponse> call,@NotNull Throwable t) {
                Toast.makeText(ClassTestActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        getAllTestCall();
    }

    private void getAllTestClass(List<TestClassResponse.TestItem> testClassResponseList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ClassTestAdapter classTestAdapter = new ClassTestAdapter(this,testClassResponseList,this);
        recyclerView.setAdapter(classTestAdapter);
        recyclerView.setHasFixedSize(true);
        classTestAdapter.notifyDataSetChanged();
    }

    private void showUiWhenCallApiSuccess() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    private void showUiWhenCallApiFalse() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(ClassTestActivity.this, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpdateComplete() {
        getAllTestCall();
    }
}