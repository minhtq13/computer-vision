package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Teacher.ExamManagement.questionexam.QuestionExamOfflineActivity;
import com.example.multiplechoiceexam.Utils.FileUtils;
import com.example.multiplechoiceexam.Utils.UpdateInterface;
import com.example.multiplechoiceexam.dto.question.ImportResponseDTO;
import com.example.multiplechoiceexam.dto.question.QuestionLevelEnum;
import com.example.multiplechoiceexam.dto.question.QuestionListDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity implements UpdateInterface {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_EXCEL_REQUEST_QUESTION = 2;
    private static final int REQUEST_ADD_QUESTION = 3;
    private QuestionAdapter questionAdapter;
    private RecyclerView recyclerView;

    private TextView importQuestion, subjectExamTest;
    private FileUtils fileUtils;

    FloatingActionButton floatingActionButton;
    ApiService apiService;
    String code, excelFilePath;
    Long subjectId,chapterId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        apiService = RetrofitClient.getApiService(getApplicationContext());
        fileUtils = new FileUtils(QuestionActivity.this);

        recyclerView = findViewById(R.id.recycler_question);
        importQuestion = findViewById(R.id.import_question);
        subjectExamTest = findViewById(R.id.subject_exam);
        floatingActionButton = findViewById(R.id.fab_add_question_add);
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {
            floatingActionButton.setVisibility(View.GONE);
            importQuestion.setVisibility(View.GONE);
            subjectExamTest.setVisibility(View.VISIBLE);
        }


        importQuestion.setOnClickListener(view -> pickExcelFile());

        subjectExamTest.setOnClickListener(view -> createExamForSubject());
        subjectId = getIntent().getLongExtra("subjectId", -1);
        chapterId = getIntent().getLongExtra("chapter_id", -1);
        code = getIntent().getStringExtra("code");
        if (chapterId == -1) {
            floatingActionButton.setVisibility(View.GONE);
            importQuestion.setVisibility(View.GONE);
            subjectExamTest.setVisibility(View.VISIBLE);
            getAllQuestionSubject();
        } else {
            getAllQuestionSubjectChapter();
        }

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(QuestionActivity.this, QuestionAddItemActivity.class);
            intent.putExtra("chapterNo", chapterId);
            startActivityForResult(intent, REQUEST_ADD_QUESTION);
        });
    }

    private void createExamForSubject() {
        Intent intent = new Intent(QuestionActivity.this, QuestionExamOfflineActivity.class);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("chapter_id", chapterId);
        intent.putExtra("code", code);

        // Start the new activity
        startActivity(intent);
    }


    private void importQuestionFromExcel() {

            File excelFile = new File(excelFilePath);
            String fileName = excelFile.getName().replaceAll("[^a-zA-Z0-9.-]", "_");
            if (!excelFile.exists()) {
                // Xử lý khi tệp tin không tồn tại
                System.out.println("File does not exist!");
                return;
            }
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), excelFile);
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), excelFile);
            MultipartBody.Part excelFilePart = MultipartBody.Part.createFormData("file",fileName, requestFile1);
            Call<ImportResponseDTO> call = apiService.importQuestions(excelFilePart);
            call.enqueue(new Callback<ImportResponseDTO>() {
                @Override
                public void onResponse(@NotNull Call<ImportResponseDTO> call,@NotNull Response<ImportResponseDTO> response) {
                    if (response.isSuccessful()) {
                        ImportResponseDTO responseMessage = response.body();
                        if (responseMessage != null) {
                            if (chapterId == -1) {
                                getAllQuestionSubject();
                            } else {
                                getAllQuestionSubjectChapter();
                            }
                            Toast.makeText(QuestionActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(QuestionActivity.this, "Có lỗi xảy ra khi đẩy tệp Excel lên máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ImportResponseDTO> call, @NotNull Throwable t) {
                    // Xử lý lỗi kết nối
                    Toast.makeText(QuestionActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void getAllQuestionSubjectChapter() {
        Call<List<QuestionListDTO>> call = apiService.getListQuestion(subjectId,"","",chapterId, QuestionLevelEnum.ALL);

        call.enqueue(new Callback<List<QuestionListDTO>>() {
            @Override
            public void onResponse(@NotNull Call<List<QuestionListDTO>> call,@NotNull Response<List<QuestionListDTO>> response) {
                List<QuestionListDTO> questionResponseList = response.body();
                getAllQuestion(questionResponseList);
                Toast.makeText(QuestionActivity.this, "thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<List<QuestionListDTO>> call,@NotNull Throwable t) {
                Toast.makeText(QuestionActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllQuestionSubject() {
        Call<List<QuestionListDTO>> call = apiService.getListQuestion(subjectId,code,"",-1L, QuestionLevelEnum.ALL);

        call.enqueue(new Callback<List<QuestionListDTO>>() {
            @Override
            public void onResponse(@NotNull Call<List<QuestionListDTO>> call,@NotNull Response<List<QuestionListDTO>> response) {
                List<QuestionListDTO> questionResponseList = response.body();
                getAllQuestion(questionResponseList);
                Toast.makeText(QuestionActivity.this, "thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<List<QuestionListDTO>> call,@NotNull Throwable t) {
                Toast.makeText(QuestionActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllQuestion(List<QuestionListDTO> questionResponseList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        questionAdapter = new QuestionAdapter(this, questionResponseList, this);
        questionAdapter.setChapterId(chapterId);
        recyclerView.setAdapter(questionAdapter);
        questionAdapter.notifyDataSetChanged();
    }

    private void pickExcelFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            startActivityForResult(Intent.createChooser(intent, "Chọn tệp Excel"), PICK_EXCEL_REQUEST_QUESTION);
        } catch (android.content.ActivityNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_REQUEST_QUESTION && resultCode == RESULT_OK) {
            if (data != null) {
                Uri excelFileUri = data.getData();
                fileUtils = new FileUtils(QuestionActivity.this);
                excelFilePath = fileUtils.getPath(excelFileUri);
            }if (excelFilePath != null) {
                importQuestionFromExcel();
            } else {
                // Handle the case when filePath is null
                Toast.makeText(this, "Lỗi đường dẫn", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_ADD_QUESTION && resultCode == RESULT_OK) {
            onUpdateComplete();
        }

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (chapterId == -1) {
//            getAllQuestionSubject();
//        } else {
//            getAllQuestionSubjectChapter();
//        }
//    }

    @Override
    public void onUpdateComplete() {
        if (chapterId == -1) {
            getAllQuestionSubject();
        } else {
            getAllQuestionSubjectChapter();
        }
    }
}