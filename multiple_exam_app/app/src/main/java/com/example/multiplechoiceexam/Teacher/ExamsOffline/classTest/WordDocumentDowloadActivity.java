package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.ExamClassManagement.studentTest.StudentTestDetailExamActivity;
import com.example.multiplechoiceexam.Utils.WordFormFiller;
import com.example.multiplechoiceexam.dto.test.ClassTestInfo;
import com.example.multiplechoiceexam.dto.test.TestQuestionAnswerResDTO;
import com.example.multiplechoiceexam.dto.test.TestSetAnswerResDTO;
import com.example.multiplechoiceexam.dto.test.TestSetDetailResponse;
import com.example.multiplechoiceexam.dto.test.TestSetSearchReqDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordDocumentDowloadActivity extends AppCompatActivity {

    WebView webView;
    Button btnTestQuiz;
    ApiService apiService;
    TestSetSearchReqDTO testSetSearchReqDTO;
    private Long testId;
    private String testNo;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_document_dowload);
        apiService = RetrofitClient.getApiService(getApplicationContext());


        ClassTestInfo testInfo = (ClassTestInfo) getIntent().getSerializableExtra("testInfo");
        if (testInfo != null) {
            testNo = testInfo.getTestCode();
            testId = testInfo.getTestId();

        }

        testSetSearchReqDTO = new TestSetSearchReqDTO();
        testSetSearchReqDTO.setCode(testNo);
        testSetSearchReqDTO.setTestId(testId);

        webView = findViewById(R.id.webView);
        btnTestQuiz = findViewById(R.id.btnDownload);
//        btnTestQuiz = findViewById(R.id.btnOpenQuestionActivity);


        btnTestQuiz.setOnClickListener(view -> {
            if (testId != 0 && testNo != null ) {
                ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
                Call<TestSetDetailResponse> call = apiService.getTestSetDetail(testSetSearchReqDTO);

                call.enqueue(new Callback<TestSetDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TestSetDetailResponse> call, @NonNull Response<TestSetDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TestSetDetailResponse testSetDetailResponse = response.body();
                            List<TestQuestionAnswerResDTO> questions = testSetDetailResponse.getLstQuestion();

                            if (questions != null && !questions.isEmpty()) {
                                Intent intent = new Intent(WordDocumentDowloadActivity.this, StudentTestDetailExamActivity.class);
                                intent.putExtra("testInfo",testInfo);
                                startActivity(intent);
                            } else {
                                Toast.makeText(WordDocumentDowloadActivity.this, "Không có câu hỏi để hiển thị", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(WordDocumentDowloadActivity.this, "Lỗi khi lấy chi tiết bài kiểm tra", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TestSetDetailResponse> call, @NonNull Throwable t) {
                        Toast.makeText(WordDocumentDowloadActivity.this, "Lỗi khi kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(WordDocumentDowloadActivity.this, "Thiếu thông tin để lấy chi tiết bài kiểm tra", Toast.LENGTH_SHORT).show();
            }
        });



        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        loadWordDocument();
        //setupDownloadButton();

    }

//    private void setupDownloadButton() {
//        btnDownload.setOnClickListener(view -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                // Kiểm tra quyền cho Android 11 trở lên
//                if (Environment.isExternalStorageManager()) {
//                    downloadWordDocument();
//                } else {
//                    // Yêu cầu quyền
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                    intent.setData(Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent, 1001);
//                }
//            } else {
//                // Cho các phiên bản Android dưới 11
//                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                } else {
//                    downloadWordDocument();
//                }
//            }
//        });
//    }
//
//    private void downloadWordDocument() {
//
//        String downloadUrl = "http://192.168.1.9:8088/e-learning//api/v1/test-set/word/export/" + testId + "/" + testNo;
//        String title = "Đề " +testId + "mã đề " + testNo+".docx";
//        // Tạo yêu cầu tải về
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
//        request.setTitle(title);
//        request.setDescription("Tải tài liệu");
//
//        // Cài đặt thư mục lưu trữ và tên tệp cho tài liệu tải về
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
//
//        // Nhận ID của tài liệu đã tải về
//        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//        long downloadId = downloadManager.enqueue(request);
//
//        // Hiển thị thông báo khi tải xong
//        Toast.makeText(WordDocumentDowloadActivity.this, "Tài liệu đang được tải về...", Toast.LENGTH_SHORT).show();
//    }

    private void loadWordDocument() {

        Call<TestSetDetailResponse> call = apiService.getTestSetDetail(testSetSearchReqDTO);

        call.enqueue(new Callback<TestSetDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TestSetDetailResponse> call, @NonNull Response<TestSetDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TestSetDetailResponse testSetDetailResponse = response.body();
                    runOnUiThread(() -> {
                        // Xử lý dữ liệu từ API response
                        Map<String, Object> dataModel = convertTestSetDetailResponseToMap(testSetDetailResponse);

                        String html = WordFormFiller.generateHtml(dataModel);
                        webView.loadData(html, "text/html", "UTF-8");
                    });

                    Toast.makeText(WordDocumentDowloadActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WordDocumentDowloadActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TestSetDetailResponse> call, @NonNull Throwable t) {
                Toast.makeText(WordDocumentDowloadActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String, Object> convertTestSetDetailResponseToMap(TestSetDetailResponse testSetDetailResponse) {
        Map<String, Object> resultMap = new HashMap<>();

        // Convert TestSetResponse to Map
        Map<String, Object> testSetMap = new HashMap<>();
        testSetMap.put("testNo", testSetDetailResponse.getTestSet().getTestSetCode());
        testSetMap.put("duration", testSetDetailResponse.getTestSet().getDuration());
        testSetMap.put("subjectTitle", testSetDetailResponse.getTestSet().getSubjectTitle());
        testSetMap.put("subjectCode", testSetDetailResponse.getTestSet().getSubjectCode());
        testSetMap.put("testDay", testSetDetailResponse.getTestSet().getSemester());
        resultMap.put("testSet", testSetMap);

        // Convert List<TestSetQuestionResponse> to List<Map<String, Object>>
        List<Map<String, Object>> questionsMapList = new ArrayList<>();
        for (TestQuestionAnswerResDTO question : testSetDetailResponse.getLstQuestion()) {
            Map<String, Object> questionMap = new HashMap<>();
            questionMap.put("id", question.getId());
            questionMap.put("questionNo", question.getQuestionNo());
            questionMap.put("level", question.getLevel());
            questionMap.put("topicText", question.getContent());
            questionMap.put("topicImage", question.getImages());

            // Convert List<TestSetQuestionAnswerResponse> to List<Map<String, Object>>
            List<Map<String, Object>> answersMapList = new ArrayList<>();
            for (TestSetAnswerResDTO answer : question.getAnswers()) {
                Map<String, Object> answerMap = new HashMap<>();
                answerMap.put("id", answer.getAnswerId());
                answerMap.put("answerNo", answer.getAnswerNoMask());
                answerMap.put("content", answer.getContent());
                answersMapList.add(answerMap);
            }

            questionMap.put("answers", answersMapList);
            questionsMapList.add(questionMap);
        }

        resultMap.put("questions", questionsMapList);

        return resultMap;
    }
}