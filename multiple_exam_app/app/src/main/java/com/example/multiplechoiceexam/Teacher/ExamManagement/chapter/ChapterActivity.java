package com.example.multiplechoiceexam.Teacher.ExamManagement.chapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Utils.UpdateInterface;
import com.example.multiplechoiceexam.dto.chapter.ChapterRequest;
import com.example.multiplechoiceexam.dto.common.ICommonIdCodeName;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterActivity extends AppCompatActivity implements UpdateInterface {

    private RecyclerView recyclerView;
    private ChapterAdapter chapterAdapter;
    private ApiService apiService;
    private Long subjectId;
    private FloatingActionButton fabChapter;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        recyclerView =findViewById(R.id.recyclerview_chapter_chapters);
        fabChapter = findViewById(R.id.fab_add_chapter);
        progressBar = findViewById(R.id.progressbar_chapter_loading);

        apiService = RetrofitClient.getApiService(getApplicationContext());

        subjectId = getIntent().getLongExtra("subjectId",-1);

        getAllChapter(subjectId);
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {
            fabChapter.setVisibility(View.GONE);
        } else {
            fabChapter.setVisibility(View.VISIBLE);
            fabChapter.setOnClickListener(view -> showAddChapter());
        }
    }
    private void showAddChapter() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_popup_chapter_add, null);
        Drawable background = new ColorDrawable(Color.WHITE);
        popupView.setBackgroundDrawable(background);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Tạo màn mờ nền
        View backgroundView = new View(ChapterActivity.this);
        backgroundView.setBackgroundColor(Color.parseColor("#80000000")); // Màu đen với độ trong suốt
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addContentView(backgroundView, params);

        int height = getResources().getDisplayMetrics().heightPixels;


        TranslateAnimation slideUp = new TranslateAnimation(0, 0, height, 0);
        slideUp.setDuration(500);
        popupView.startAnimation(slideUp);
        backgroundView.setVisibility(View.VISIBLE);
        popupWindow.setOnDismissListener(() -> backgroundView.setVisibility(View.GONE));

        EditText editTextTitle = popupView.findViewById(R.id.editTextTitleChapterAdd);
        EditText editTextOrder = popupView.findViewById(R.id.editTextOrderChapterAdd);
        EditText editTextDescription = popupView.findViewById(R.id.editTextDescriptionChapter);
        Button buttonSave = popupView.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String order  = editTextOrder.getText().toString();
            String description = editTextDescription.getText().toString();

            int orderTmp = 0;
            if (!order.isEmpty()) {
                try {
                    orderTmp = Integer.parseInt(order);
                } catch (NumberFormatException e) {
                    Toast.makeText(ChapterActivity.this,"lỗi nhập" , Toast.LENGTH_SHORT).show();
                }
            }
            ChapterRequest.ChapterSaveReqDTO chapterSaveReqDTO = new ChapterRequest.ChapterSaveReqDTO();
            chapterSaveReqDTO.setTitle(title);
            chapterSaveReqDTO.setDescription(description);
            chapterSaveReqDTO.setOrders(orderTmp);

            List<ChapterRequest.ChapterSaveReqDTO> lstChapter = new ArrayList<>();
            lstChapter.add(chapterSaveReqDTO);

            ChapterRequest chapterRequest = new ChapterRequest();
            chapterRequest.setSubjectId(subjectId);
            chapterRequest.setLstChapter(lstChapter);

            addChapter(chapterRequest);

            popupWindow.dismiss();
        });
        popupWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
    }

    private void addChapter(ChapterRequest chapterRequest) {
        Call<Void> call = apiService.addSubjectChapter(chapterRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    getAllChapter(subjectId);
                    Toast.makeText(ChapterActivity.this,"thành công" , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChapterActivity.this,"thất bại" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(ChapterActivity.this,"lỗi" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllChapter(Long subjectId) {
        Call<List<ICommonIdCodeName>> call = apiService.getAllSubjectChapters(subjectId);

        call.enqueue(new Callback<List<ICommonIdCodeName>>() {
            @Override
            public void onResponse(@NotNull Call<List<ICommonIdCodeName>> call,@NotNull Response<List<ICommonIdCodeName>> response) {
                if (response.isSuccessful()) {
                    List<ICommonIdCodeName> list = response.body();
                    getAllChapterBySub(list);
                    showUiWhenCallApiSuccess();
                } else {
                    showUiWhenCallApiFalse();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ICommonIdCodeName>> call,@NotNull Throwable t) {
                Toast.makeText(ChapterActivity.this, "lỗi" , Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showUiWhenCallApiSuccess() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    private void showUiWhenCallApiFalse() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(ChapterActivity.this, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show();
    }

    private void getAllChapterBySub(List<ICommonIdCodeName> list){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        chapterAdapter = new ChapterAdapter(this, list,this);
        recyclerView.setAdapter(chapterAdapter);
        chapterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateComplete() {
        getAllChapter(subjectId);
    }
}