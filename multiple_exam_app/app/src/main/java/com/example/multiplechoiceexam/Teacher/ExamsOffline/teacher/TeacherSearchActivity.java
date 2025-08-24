package com.example.multiplechoiceexam.Teacher.ExamsOffline.teacher;

import static com.example.multiplechoiceexam.Utils.DateUtils.generateRandomDateTime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.example.multiplechoiceexam.dto.teacher.TeacherResponse;
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

public class TeacherSearchActivity extends AppCompatActivity {

    private static final int PICK_EXCEL_REQUEST_TEACHER = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 4;
    private RecyclerView recyclerViewTeacher;

    private TeacherAdapter teacherAdapter;
    EditText editTextTeacher;
    private FloatingActionButton floatingActionButtonTeacher;
    RadioGroup operatorRadioGroup;
    private ApiService apiService;
    private FileUtils fileUtils;
    String excelFilePath,selectedOperator, teacherName;
    private TextView menuImportTeacherTv;
    ProgressDialog progressDialog;
    private List<TeacherResponse> teacherResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search);

        apiService = RetrofitClient.getApiService(getApplicationContext());
        fileUtils = new FileUtils(getApplicationContext());

        teacherResponses = new ArrayList<>();
        recyclerViewTeacher = findViewById(R.id.recycler_teacher);
        floatingActionButtonTeacher = findViewById(R.id.fab_add_teacher);
        editTextTeacher = findViewById(R.id.editTextTeacher);
        operatorRadioGroup = findViewById(R.id.operatorRadioGroup);
        progressDialog = new ProgressDialog(TeacherSearchActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Tải danh sách giáo viên...");


        operatorRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checkedRadioButton = findViewById(i);
            selectedOperator = checkedRadioButton.getText().toString();
        });
        editTextTeacher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchTeacher = charSequence.toString();
                searchTeacherByOp(searchTeacher, selectedOperator);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        recyclerViewTeacher.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        teacherAdapter = new TeacherAdapter(teacherResponses,this);
        recyclerViewTeacher.setAdapter(teacherAdapter);
        getAllTeacher();


        menuImportTeacherTv = findViewById(R.id.textview_listTeacher_menu);
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(this);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {
            menuImportTeacherTv.setVisibility(View.GONE);
        }
        menuImportTeacherTv.setOnClickListener(showMenu -> showOptionsDialog());

        floatingActionButtonTeacher.setOnClickListener(view -> startActivity(new Intent(TeacherSearchActivity.this, TeacherAddActivity.class)));
    }

    private void searchTeacherByOp(String searchTeacher, String selectedOperator) {
        if(selectedOperator == null ||selectedOperator.equals("Mã học phần")){
            teacherName = searchTeacher;
        }else if (selectedOperator.equals("Họ và tên")) {
            teacherName = searchTeacher;
        }
        apiService.getListTeacher(teacherName).enqueue(new Callback<List<TeacherResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<TeacherResponse>> call,@NotNull Response<List<TeacherResponse>> response) {
                if (response.isSuccessful()) {
                    teacherResponses.clear();
                    teacherResponses.addAll(response.body());
                    teacherAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TeacherSearchActivity.this, "Lấy danh sách giảng viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TeacherResponse>> call, @NotNull Throwable t) {
                Toast.makeText(TeacherSearchActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOptionsDialog() {
        Dialog dialog = new Dialog(TeacherSearchActivity.this);
        dialog.setContentView(R.layout.option_item);
        TextView header = dialog.findViewById(R.id.textView_dialogImport_header);
        header.setText("Import danh sách giáo viên");
        ImageView upload =  dialog.findViewById(R.id.upload_excel_img);
        ImageView download =  dialog.findViewById(R.id.download_excel_img);
        Button cancel = dialog.findViewById(R.id.cancel_button);
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
    }

    private void performDownload() {
        Call<ResponseBody> call = apiService.exportExcelFileTeacher("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String randomDateTime = generateRandomDateTime();
                        downloadFile(body, "Teacher_" +  randomDateTime + ".xlsx");
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
                Toast.makeText(TeacherSearchActivity.this, "Looix !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

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
                progressDialog.dismiss();

            } catch (IOException e) {
                Toast.makeText(getApplication(), "Tải xuống thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                progressDialog.dismiss();

            }
        } else {
            Toast.makeText(getApplication(), "Không thể truy cập bộ nhớ ngoài.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
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
            startActivityForResult(Intent.createChooser(intent, "Chọn tệp Excel"), PICK_EXCEL_REQUEST_TEACHER);
        } catch (ActivityNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_REQUEST_TEACHER && resultCode == RESULT_OK) {
            if (data != null) {
                Uri excelFileUri = data.getData();
                fileUtils = new FileUtils(TeacherSearchActivity.this);
                excelFilePath = fileUtils.getPath(excelFileUri);
            }if (excelFilePath != null) {
                importTeacherFromExcel();
            } else {
                Toast.makeText(this, "Lỗi đường dẫn", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không có file nào được chọn", Toast.LENGTH_SHORT).show();
        }

    }

    private void importTeacherFromExcel() {
            File excelFile = new File(excelFilePath);
            String fileName = excelFile.getName().replaceAll("[^a-zA-Z0-9.-]", "_");
            if (!excelFile.exists()) {
                // Xử lý khi tệp tin không tồn tại
                System.out.println("File does not exist!");
                return;
            }
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), excelFile);
            MultipartBody.Part excelFilePart = MultipartBody.Part.createFormData("file",fileName, requestFile1);
            Call<ImportResponseDTO> call = apiService.importTeacher(excelFilePart);
            call.enqueue(new Callback<ImportResponseDTO>() {
                @Override
                public void onResponse(@NotNull Call<ImportResponseDTO> call,@NotNull Response<ImportResponseDTO> response) {
                    if (response.isSuccessful()) {
                        ImportResponseDTO responseMessage = response.body();
                        if (responseMessage != null) {
                            getAllTeacher();
                            Toast.makeText(TeacherSearchActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TeacherSearchActivity.this, "Có lỗi xảy ra khi đẩy tệp Excel lên máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ImportResponseDTO> call,@NotNull Throwable t) {
                    // Xử lý lỗi kết nối
                    Toast.makeText(TeacherSearchActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
    }


    private void getAllTeacher() {
        apiService.getListTeacher("").enqueue(new Callback<List<TeacherResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<TeacherResponse>> call,@NotNull Response<List<TeacherResponse>> response) {
                if (response.isSuccessful()) {
                    teacherResponses.clear();
                    assert response.body() != null;
                    teacherResponses.addAll(response.body());
                    teacherAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(TeacherSearchActivity.this, "Lấy danh sách sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TeacherResponse>> call,@NotNull Throwable t) {
                Toast.makeText(TeacherSearchActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}