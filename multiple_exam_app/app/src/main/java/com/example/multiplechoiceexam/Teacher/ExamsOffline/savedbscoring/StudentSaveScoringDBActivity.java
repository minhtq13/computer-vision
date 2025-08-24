package com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring;

import static com.example.multiplechoiceexam.Utils.DateUtils.generateRandomDateTime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;

import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest.SemesterAdapter;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest.SubjectComboBoxAdapter;
import com.example.multiplechoiceexam.Utils.ValidateInputData;
import com.example.multiplechoiceexam.dto.examClass.ICommonIdCode;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;
import com.example.multiplechoiceexam.dto.studentTest.ExamClassResultStatisticsDTO;
import com.example.multiplechoiceexam.dto.studentTest.StudentTestSetResultDTO;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;
import com.example.multiplechoiceexam.dto.test.semeter.SemesterBox;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentSaveScoringDBActivity extends AppCompatActivity {
    private RecyclerView recyclerViewScoring;
    private StudentTestSetResultAdapter studentTestSetResultAdapter;
    private ExamClassResultStatisticsDTO examClassResultStatisticsDTO;
    private List<FileAttachDTO> fileAttachDTOS;
    private TextInputEditText txtSemester,txtSubject,editTextClassCode;
    private Button btnCheck, btnDBNoScoring, btnExport;
    private ImageView imageBack;
    private ApiService apiService;
    private String examClassCode;
    private Long semesterId,subjectId;
    private ProgressDialog progressDialog, progressDialogDownload;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_save_scoring_dbactivity);
        apiService = RetrofitClient.getApiService(getApplicationContext());
        fileAttachDTOS = new ArrayList<>();

        editTextClassCode = findViewById(R.id.editTextScoringSaveDB);
        btnCheck = findViewById(R.id.btn_scoring_saveDB);
        btnExport = findViewById(R.id.btn_scoring_export);
        txtSubject = findViewById(R.id.txt_subject_test_search);
        txtSemester = findViewById(R.id.txt_semester_test_search);
        btnDBNoScoring = findViewById(R.id.btn_scoring_saveDB_no_scoring);
        recyclerViewScoring = findViewById(R.id.recycler_scoring_db);
        imageBack = findViewById(R.id.imageView_save_scoring_back);
        progressDialog = new ProgressDialog(StudentSaveScoringDBActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang lấy bài thi...");
        progressDialogDownload = new ProgressDialog(StudentSaveScoringDBActivity.this);
        progressDialogDownload.setCancelable(false);
        progressDialogDownload.setMessage("Đang tải danh sách sinh viên...");

        imageBack.setOnClickListener(view -> finish());
        editTextClassCode.setOnClickListener(view -> getListExamClass());
        txtSubject.setOnClickListener(view -> choseSubjectId());
        txtSemester.setOnClickListener(view -> choseSemesterId());
        btnCheck.setOnClickListener(view -> {
            examClassCode = editTextClassCode.getText().toString();
            if(!ValidateInputData.stringNotNull(examClassCode, editTextClassCode)){
                return;
            }
            if(!ValidateInputData.stringNotNull(txtSubject.getText().toString(), txtSubject)){
                return;
            }
            if(!ValidateInputData.stringNotNull(txtSemester.getText().toString(), txtSemester)){
                return;
            }
            progressDialog.show();
            searchScoringMultiExam(examClassCode);
        });

        btnDBNoScoring.setOnClickListener(view -> {
            examClassCode = editTextClassCode.getText().toString();
            if(!ValidateInputData.stringNotNull(txtSemester.getText().toString(), txtSemester)){
                return;
            }
            if(!ValidateInputData.stringNotNull(txtSubject.getText().toString(), txtSubject)){
                return;
            }

            if(!ValidateInputData.stringNotNull(examClassCode, editTextClassCode)){
                return;
            }
            progressDialog.show();
            searchScoringNoScoring(examClassCode);
        });
        btnExport.setOnClickListener(view -> {
            examClassCode = editTextClassCode.getText().toString();
            if(!ValidateInputData.stringNotNull(txtSemester.getText().toString(), txtSemester)){
                return;
            }
            if(!ValidateInputData.stringNotNull(txtSubject.getText().toString(), txtSubject)){
                return;
            }

            if(!ValidateInputData.stringNotNull(examClassCode, editTextClassCode)){
                return;
            }
            progressDialogDownload.show();
            exportStudentTestScoring(examClassCode);
        });
    }

    private void exportStudentTestScoring(String examClassCode) {
        Call<ResponseBody> call = apiService.exportStudentTestScoring(examClassCode);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody customInputStreamResource = response.body();
                    if (customInputStreamResource != null) {
                        String randomDateTime = generateRandomDateTime();
                        downloadFile(customInputStreamResource, "Student_Exam_" + examClassCode+"_" + randomDateTime + ".xlsx");
                        progressDialogDownload.dismiss();
                    } else {
                        progressDialogDownload.dismiss();
                        Toast.makeText(getApplicationContext(), "Phản hồi từ server rỗng.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gọi API không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    progressDialogDownload.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call,@NotNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Looix !", Toast.LENGTH_SHORT).show();
                progressDialogDownload.dismiss();
            }
        });
    }

    private void choseSubjectId() {
        Call<SubjectResponse> call = RetrofitClient.getApiService(getApplicationContext()).getListSubject(
                "",
                -1L, // departmentId
                "",
                0, // page
                10000, // size
                "code" // sort
        );

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(@NotNull Call<SubjectResponse> call, @NotNull Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    SubjectResponse subjectListResponse = response.body();
                    List<SubjectResponse.SubjectItem> subjects = subjectListResponse.getContent();
                    showSubjectSelectionDialog(subjects);
                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(@NotNull Call<SubjectResponse> call,@NotNull Throwable t) {
                // Xử lý thất bại
            }
        });
    }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void downloadFile(ResponseBody body, String s) {
        if (isExternalStorageWritable()) {
            try {
                File externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(externalStorageDir, s);
                FileOutputStream fos = new FileOutputStream(file);
                InputStream inputStream = body.byteStream();

                // Bộ đệm để đọc/ghi dữ liệu
                byte[] buffer = new byte[4096];
                int bytesRead;

                // Vòng lặp để đọc từ luồng dữ liệu và ghi vào tập tin
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                // Đóng luồng dữ liệu
                fos.flush();
                fos.close();
                inputStream.close();

                Toast.makeText(getApplication(), "Tải xuống hoàn tất. Tập tin được lưu trong thư mục Downloads.", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                Toast.makeText(getApplication(), "Tải xuống thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplication(), "Không thể truy cập bộ nhớ ngoài.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showSubjectSelectionDialog(List<SubjectResponse.SubjectItem> subjects) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentSaveScoringDBActivity.this);
        LayoutInflater inflater = LayoutInflater.from(StudentSaveScoringDBActivity.this);

        View dialogView = inflater.inflate(R.layout.custom_subject_selection_dialog, null);
        builder.setView(dialogView);

        RecyclerView recyclerViewSubjects = dialogView.findViewById(R.id.recyclerViewSubjects);
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose);

        recyclerViewSubjects.setLayoutManager(new LinearLayoutManager(StudentSaveScoringDBActivity.this));
        SubjectComboBoxAdapter subjectAdapter = new SubjectComboBoxAdapter(StudentSaveScoringDBActivity.this, subjects);
        recyclerViewSubjects.setAdapter(subjectAdapter);
        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
        recyclerViewSubjects.addItemDecoration(new RecyclerViewItemDecoration(StudentSaveScoringDBActivity.this, dividerHeight));
        AlertDialog dialog = builder.create();
        subjectAdapter.setOnItemClickListener(subjectItem -> {
            txtSubject.setText(subjectItem.getTitle());
            subjectId = subjectItem.getId();
            dialog.dismiss();
        });

        dialog.show();

        btnClose.setOnClickListener(view -> dialog.dismiss());
    }

    private void searchScoringNoScoring(String examClassCode) {
        apiService.getListFileInExFolder(examClassCode).enqueue(new Callback<List<FileAttachDTO>>() {
            @Override
            public void onResponse(@NotNull Call<List<FileAttachDTO>> call,@NotNull Response<List<FileAttachDTO>> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    fileAttachDTOS = response.body();
                    if (fileAttachDTOS != null) {
                        setupRecyclerViewNoScoring(fileAttachDTOS);
                        Toast.makeText(StudentSaveScoringDBActivity.this, " thanh cong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentSaveScoringDBActivity.this, " response body is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<FileAttachDTO>> call,@NotNull Throwable t) {

            }
        });
    }

    private void setupRecyclerViewNoScoring(List<FileAttachDTO> fileAttachDTOS) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewScoring.setLayoutManager(layoutManager);
        ImageNoScoringAdapter imageNoScoringAdapter = new ImageNoScoringAdapter(this, fileAttachDTOS ,examClassCode);
        recyclerViewScoring.setAdapter(imageNoScoringAdapter);
        imageNoScoringAdapter.notifyDataSetChanged();
    }


    private void searchScoringMultiExam(String examClassCode) {
        apiService.getStudentTestSetResult(examClassCode).enqueue(new Callback<ExamClassResultStatisticsDTO>() {
            @Override
            public void onResponse(@NotNull Call<ExamClassResultStatisticsDTO> call,@NotNull Response<ExamClassResultStatisticsDTO> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    examClassResultStatisticsDTO = response.body();
                    if (examClassResultStatisticsDTO != null) {
                        List<StudentTestSetResultDTO> studentTestSetResultDTO= examClassResultStatisticsDTO.getResults();
                        setupRecyclerView(studentTestSetResultDTO);
                        btnExport.setVisibility(View.VISIBLE);
                        Toast.makeText(StudentSaveScoringDBActivity.this, " thanh cong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentSaveScoringDBActivity.this, " response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StudentSaveScoringDBActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ExamClassResultStatisticsDTO> call,@NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(StudentSaveScoringDBActivity.this, "lỗi", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerView(List<StudentTestSetResultDTO> studentTestSetResultDTO) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewScoring.setLayoutManager(layoutManager);
        studentTestSetResultAdapter = new StudentTestSetResultAdapter(studentTestSetResultDTO, this);
        recyclerViewScoring.setAdapter(studentTestSetResultAdapter);
        studentTestSetResultAdapter.notifyDataSetChanged();
    }
    private void choseSemesterId() {
        Call<List<SemesterBox>> call = RetrofitClient.getApiService(getApplicationContext()).getListSemester("");
        call.enqueue(new Callback<List<SemesterBox>>() {
            @Override
            public void onResponse(@NotNull Call<List<SemesterBox>> call,@NotNull Response<List<SemesterBox>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SemesterBox> semesterBoxes = response.body();
                    if (semesterBoxes != null && !semesterBoxes.isEmpty()) {
                        Dialog dialog = new Dialog(StudentSaveScoringDBActivity.this);
                        dialog.setContentView(R.layout.dialog_semester_list);
                        Window window = dialog.getWindow();
                        if (window != null) {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int dialogWidth = (int) (displayMetrics.widthPixels * 0.8);
                            window.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                        TextView textViewDialogTitle = dialog.findViewById(R.id.textViewDialogTitle);
                        textViewDialogTitle.setText("Học kì");

                        RecyclerView recyclerViewSemesters = dialog.findViewById(R.id.recyclerViewSemesters);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(StudentSaveScoringDBActivity.this);
                        recyclerViewSemesters.setLayoutManager(layoutManager);
                        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
                        recyclerViewSemesters.addItemDecoration(new RecyclerViewItemDecoration(StudentSaveScoringDBActivity.this, dividerHeight));

                        SemesterAdapter adapter = new SemesterAdapter(semesterBoxes,StudentSaveScoringDBActivity.this);
                        recyclerViewSemesters.setAdapter(adapter);
                        adapter.setOnItemClickListener(semesterLists -> {
                            txtSemester.setText(semesterLists.getName());
                            semesterId = semesterLists.getId();
                            dialog.dismiss();
                        });
                        ImageButton buttonCloseDialog = dialog.findViewById(R.id.buttonCloseDialog);
                        buttonCloseDialog.setOnClickListener(v -> dialog.dismiss());

                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<SemesterBox>> call,@NotNull Throwable t) {

            }
        });
    }
    private void getListExamClass() {
        Call<List<ICommonIdCode>> call = apiService.getListExamClass(
                semesterId,
                subjectId,
                ""
        );

        call.enqueue(new Callback<List<ICommonIdCode>>() {
            @Override
            public void onResponse(@NotNull Call<List<ICommonIdCode>> call,@NotNull Response<List<ICommonIdCode>> response) {
                if (response.isSuccessful()) {
                    List<ICommonIdCode> examClassList = response.body();
                    if (examClassList != null && !examClassList.isEmpty()) {
                        Dialog dialog = new Dialog(StudentSaveScoringDBActivity.this);
                        dialog.setContentView(R.layout.dialog_class_code_list);
                        Window window = dialog.getWindow();
                        if (window != null) {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int dialogWidth = (int) (displayMetrics.widthPixels * 0.9);
                            window.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                        TextView textViewDialogTitle = dialog.findViewById(R.id.textViewDialogTitleExamClassCode);
                        textViewDialogTitle.setText("Lớp thi");

                        RecyclerView recyclerViewSemesters = dialog.findViewById(R.id.recyclerViewExamClassCode);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(StudentSaveScoringDBActivity.this);
                        recyclerViewSemesters.setLayoutManager(layoutManager);

                        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
                        recyclerViewSemesters.addItemDecoration(new RecyclerViewItemDecoration(StudentSaveScoringDBActivity.this, dividerHeight));

                        ExamClassCodeSearchAdapter adapter = new ExamClassCodeSearchAdapter(StudentSaveScoringDBActivity.this,examClassList);
                        recyclerViewSemesters.setAdapter(adapter);
                        adapter.setOnItemClickListener(iCommonIdCode -> {
                            editTextClassCode.setText(iCommonIdCode.getCode());
                            examClassCode = iCommonIdCode.getCode();
                            dialog.dismiss();
                        });
//                        Button buttonCloseDialog = dialog.findViewById(R.id.buttonCloseDialogExamClassCode);
//                        buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });

                        dialog.show();
                        Toast.makeText(StudentSaveScoringDBActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(StudentSaveScoringDBActivity.this, "Học phần không có môn thi", Toast.LENGTH_SHORT).show();
                    }
                   
                }else {
                    Toast.makeText(StudentSaveScoringDBActivity.this, "thất bại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<ICommonIdCode>> call,@NotNull Throwable t) {
                Toast.makeText(StudentSaveScoringDBActivity.this, "lỗi", Toast.LENGTH_SHORT).show();

            }
        });
    }
}