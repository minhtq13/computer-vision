package com.example.multiplechoiceexam.Teacher.ExamsOffline.student;

import static com.example.multiplechoiceexam.Utils.DateUtils.generateRandomDateTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Utils.FileUtils;
import com.example.multiplechoiceexam.dto.question.ImportResponseDTO;
import com.example.multiplechoiceexam.dto.student.StudentResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentActivity extends AppCompatActivity {

    private static final int PICK_EXCEL_REQUEST_STUDENT = 1;
    private static final int IMPORT_PERMISSION_REQUEST_CODE = 2;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 4;
    EditText editTextStudent;
    RecyclerView recyclerViewStudent;
    StudentAdapter studentAdapter;
    FloatingActionButton floatingActionButtonStudent;
    ApiService apiService;
    List<StudentResponse> studentResponseList;
    RadioGroup operatorRadioGroup;
    String selectedOperator, excelFilePath, studentName;
    Integer studentCode;
    private TextView menuImportStudentTv;
    ProgressDialog progressDialog;
    private String previousOperator = "";
    FileUtils fileUtils;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        apiService = RetrofitClient.getApiService(getApplicationContext());

        findViewById(R.id.imageView_student_back).setOnClickListener(v -> {
            finish();
        });
        studentResponseList = new ArrayList<>();
        recyclerViewStudent = findViewById(R.id.recycler_student);
        floatingActionButtonStudent = findViewById(R.id.fab_add_student);
        editTextStudent = findViewById(R.id.editTextStudent);
        operatorRadioGroup = findViewById(R.id.operatorRadioGroup);

        progressDialog = new ProgressDialog(StudentActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Tải danh sách sinh viên...");

        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();

        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        studentAdapter = new StudentAdapter(this, studentResponseList);
        recyclerViewStudent.setAdapter(studentAdapter);
        getAllStudent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        floatingActionButtonStudent.setOnClickListener(view -> {
            Intent intent = new Intent(StudentActivity.this, StudentAddActivity.class);
            startActivity(intent);
        });


        menuImportStudentTv = findViewById(R.id.textview_listStudent_menu);
        if (userRoles.contains("ROLE_STUDENT")) {
            // If the user is a student, hide the buttons
            menuImportStudentTv.setVisibility(View.GONE);
        }
        menuImportStudentTv.setOnClickListener(showMenu -> {
            showOptionsDialog();
        });
        operatorRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checkedRadioButton = findViewById(i);
            selectedOperator = checkedRadioButton.getText().toString();

        });


        editTextStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchStudent = charSequence.toString();
                searchStudentByOp(searchStudent, selectedOperator);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("download_channel", "Download Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.createNotificationChannel(channel);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showOptionsDialog() {
        Dialog dialog = new Dialog(StudentActivity.this);
        dialog.setContentView(R.layout.option_item);
        ImageView upload = dialog.findViewById(R.id.upload_excel_img);
        ImageView download = dialog.findViewById(R.id.download_excel_img);
        TextView header =  dialog.findViewById(R.id.textView_dialogImport_header);
        header.setText("Import danh sách sinh viên");
        Button cancel =  dialog.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(v -> dialog.dismiss());

        upload.setOnClickListener(view -> pickExcelFile());
        download.setOnClickListener(view -> {
            downloadExcelFile();
            dialog.dismiss();
            progressDialog.show();
        });
        dialog.show();
    }

    private void downloadExcelFile() {
        performDownload();
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_WRITE_EXTERNAL_STORAGE);
//        } else {
//            performDownload();
//        }
    }


    private void performDownload() {
        Call<ResponseBody> call = apiService.exportExcelFileStudent("",  -1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String randomDateTime = generateRandomDateTime();
                        downloadFile(body, "Student_" + randomDateTime + ".xlsx");
                    } else {
                        Toast.makeText(getApplicationContext(), "Phản hồi từ server rỗng.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gọi API không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call,@NotNull Throwable t) {
                Toast.makeText(StudentActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void downloadFile(ResponseBody body, String s) {
        if (isExternalStorageWritable()) {
            try {
                File externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(externalStorageDir, s);
                FileOutputStream fos = new FileOutputStream(file);
                InputStream inputStream = body.byteStream();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                fos.flush();
                fos.close();
                inputStream.close();
                Toast.makeText(getApplication(), "Tải xuống hoàn tất. Tập tin được lưu trong thư mục Downloads.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } catch (IOException e) {
                Toast.makeText(getApplication(), "Tải xuống thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplication(), "Không thể truy cập bộ nhớ ngoài.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void pickExcelFile() {
        performPickExcelFile();
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Nếu quyền chưa được cấp, yêu cầu người dùng cấp quyền
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_READ_EXTERNAL_STORAGE);
//        } else {
//            // Nếu quyền đã được cấp, tiến hành chọn tệp tin Excel
//            performPickExcelFile();
//        }
    }

    private void performPickExcelFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            startActivityForResult(Intent.createChooser(intent, "Chọn tệp Excel"), PICK_EXCEL_REQUEST_STUDENT);
        } catch (ActivityNotFoundException ignored) {

        }
    }

    private void searchStudentByOp(String searchStudent, String selectedOperator) {

        if (selectedOperator == null || selectedOperator.equals("MSSV")) {
            studentName = searchStudent;
            studentCode = -1;
        } else if (selectedOperator.equals("Họ và tên")) {
            studentName = searchStudent;
            studentCode = -1;
        } else if (selectedOperator.equals("Khóa")) {
            if (!searchStudent.isEmpty()) {
                try {
                    studentCode = Integer.valueOf(searchStudent);
                }catch (NumberFormatException e){
                    studentCode = -1;
                }
            } else {
                studentCode = -1;
            }
            studentName = "";
        }
        apiService.getListStudent(studentName, studentCode).enqueue(new Callback<List<StudentResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<StudentResponse>> call,@NotNull Response<List<StudentResponse>> response) {
                if (response.isSuccessful()) {
                    studentResponseList.clear();
                    assert response.body() != null;
                    studentResponseList.addAll(response.body());
                    studentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(StudentActivity.this, "Lấy danh sách sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<StudentResponse>> call,@NotNull Throwable t) {
                Toast.makeText(StudentActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAllStudent() {
        apiService.getListStudent("", -1).enqueue(new Callback<List<StudentResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<StudentResponse>> call,@NotNull Response<List<StudentResponse>> response) {
                if (response.isSuccessful()) {
                    studentResponseList.clear();
                    assert response.body() != null;
                    studentResponseList.addAll(response.body());
                    studentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(StudentActivity.this, "Lấy danh sách sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<StudentResponse>> call,@NotNull Throwable t) {
                Toast.makeText(StudentActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_REQUEST_STUDENT && resultCode == RESULT_OK) {
            if (data != null) {
                Uri excelFileUri = data.getData();
                fileUtils = new FileUtils(StudentActivity.this);
                excelFilePath = fileUtils.getPath(excelFileUri);
            }
            if (excelFilePath != null) {
                importStudentFromExcel();
            } else {
                Toast.makeText(this, "Lỗi đường dẫn", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không có file nào được chọn", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                performPickExcelFile();
//            } else {
//                Toast.makeText(this, "Ứng dụng cần quyền để đọc tệp tin.", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                performDownload();
//            } else {
//                Toast.makeText(this, "Ứng dụng cần quyền để tải xuống tệp tin.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private void importStudentFromExcel() {
            File excelFile = new File(excelFilePath);
            String fileName = excelFile.getName().replaceAll("[^a-zA-Z0-9.-]", "_");
            if (!excelFile.exists()) {
                // Xử lý khi tệp tin không tồn tại
                System.out.println("File does not exist!");
                return;
            }
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), excelFile);
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), excelFile);
            MultipartBody.Part excelFilePart = MultipartBody.Part.createFormData("file", fileName, requestFile1);
            Call<ImportResponseDTO> call = apiService.importStudent(excelFilePart);
            call.enqueue(new Callback<ImportResponseDTO>() {
                @Override
                public void onResponse(@NotNull Call<ImportResponseDTO> call,@NotNull Response<ImportResponseDTO> response) {
                    if (response.isSuccessful()) {
                        ImportResponseDTO responseMessage = response.body();
                        if (responseMessage != null) {
                            getAllStudent();
                            Toast.makeText(StudentActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StudentActivity.this, "Có lỗi xảy ra khi đẩy tệp Excel lên máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ImportResponseDTO> call,@NotNull Throwable t) {
                    Toast.makeText(StudentActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        getAllStudent();
//    }
}