package com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest.SemesterAdapter;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest.SubjectComboBoxAdapter;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring.ExamClassCodeSearchAdapter;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring.ImageNoScoringAdapter;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring.RecyclerViewItemDecoration;
import com.example.multiplechoiceexam.Utils.BaseUrlUtils;
import com.example.multiplechoiceexam.Utils.FileStorage;
import com.example.multiplechoiceexam.Utils.Utility;
import com.example.multiplechoiceexam.Utils.ValidateInputData;
import com.example.multiplechoiceexam.databinding.ActivityPreviewScoringBinding;
import com.example.multiplechoiceexam.dto.examClass.ICommonIdCode;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;
import com.example.multiplechoiceexam.dto.test.semeter.SemesterBox;
import com.example.multiplechoiceexam.dto.upload.ScoringPreviewItemDTO;
import com.example.multiplechoiceexam.dto.upload.ScoringPreviewResDTO;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.pqpo.smartcropperlib.SmartCropper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class PreviewScoringActivity extends AppCompatActivity {

    private RecyclerView recyclerViewScoring,recyclerViewNoScoring;
    private ScoringPreviewAdapter scoringPreviewAdapter;
    private ScoringPreviewResDTO scoringPreviewResDTO;
    private List<ScoringPreviewItemDTO> scoringPreviewItemDTOList;
    private EditText txtSubject, txtSemester, editTextClassCode;
    private Button btnCheck, btnSaveScoring;
    private ApiService apiService;
    private String examClassCode;
    private ProgressDialog progressDialog, progressDialog1,progressDialogSaveDB;
    private Long semesterId, subjectId;
    private RecyclerView imgRecyclerview;
    private TextView imgCount;
    private ArrayList<Uri> imageList;
    private ImageAdapter imageAdapter;
    private List<FileAttachDTO> fileAttachDTOS;
    List<Uri> newImages = new ArrayList<>();

    private final int REQUESTCODE_GALLEY = 123;
    private final int REQUEST_IMAGE_CAPTURE = 115;
    private final int REQUEST_IMAGE_GALLERY = 116;
    private String permissions[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
    };
    private int showcaseIndex = 0;
    private ActivityPreviewScoringBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewScoringBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showcaseIndex = 0;
        showShowcase();

        apiService = RetrofitClient.getApiService(getApplicationContext());
        scoringPreviewItemDTOList = new ArrayList<>();
        fileAttachDTOS = new ArrayList<>();
        editTextClassCode = findViewById(R.id.editTextScoring);
        btnCheck = findViewById(R.id.btn_scoring);
        txtSubject = findViewById(R.id.txt_subject_test_search);
        txtSemester = findViewById(R.id.txt_semester_test_search);
        btnSaveScoring = findViewById(R.id.btn_save_scoring);
        recyclerViewScoring = findViewById(R.id.recycler_scoring);
        recyclerViewNoScoring = findViewById(R.id.recycler_scoring_no_db);
        imageList = new ArrayList<>();
        imgCount = findViewById(R.id.imageCount_tv);
        imageAdapter = new ImageAdapter(imageList,this);
        imgRecyclerview = findViewById(R.id.recycler_image);

        imgRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        imgRecyclerview.setAdapter(imageAdapter);
        progressDialog1 = new ProgressDialog(PreviewScoringActivity.this);
        progressDialog1.setCancelable(false);
        progressDialog1.setMessage("Uploading...");
        SmartCropper.buildImageDetector(this);
        progressDialog = new ProgressDialog(PreviewScoringActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang chấm vui lòng đợi một chút...");
        progressDialogSaveDB = new ProgressDialog(PreviewScoringActivity.this);
        progressDialogSaveDB.setCancelable(false);
        progressDialogSaveDB.setMessage("Đang lưu kết quả...");

        editTextClassCode.setOnClickListener(view -> {
            getListExamClass();
        });
        txtSubject.setOnClickListener(view -> {
            choseSubjectId();
        });
        txtSemester.setOnClickListener(view -> {
            choseSemesterId();
        });
        btnSaveScoring.setOnClickListener(view -> saveScoringDB());

        findViewById(R.id.pickImageFromDevice).setOnClickListener(v -> pickMutilImage());

        btnCheck.setOnClickListener(view -> {
            examClassCode = editTextClassCode.getText().toString();

            if (!ValidateInputData.stringNotNull(examClassCode, editTextClassCode)) {
                return;
            }
            if (!ValidateInputData.stringNotNull(txtSubject.getText().toString(), txtSubject)) {
                return;
            }
            if (!ValidateInputData.stringNotNull(txtSemester.getText().toString(), txtSemester)) {
                return;
            }
            scoringMultiExam(examClassCode);
        });


        binding.imageViewScoringBack.setOnClickListener(v -> finish());
//        btnScoringDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteScoringDB();
//            }
//        });

    }

    private void pickMutilImage() {
        Dialog dialog = new Dialog(PreviewScoringActivity.this);
        dialog.setContentView(R.layout.dialog_option_select_img);
        ImageView camera =  dialog.findViewById(R.id.camera_img);
        ImageView galley =  dialog.findViewById(R.id.galley_img);
        Button cancel = dialog.findViewById(R.id.cancel_button);
        recyclerViewNoScoring.setVisibility(View.GONE);
        imgRecyclerview.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(v -> dialog.dismiss());
        galley.setOnClickListener(v -> {
            // check permission for galley
            if (ActivityCompat.checkSelfPermission(
                    PreviewScoringActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            PreviewScoringActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            PreviewScoringActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            PreviewScoringActivity.this, permissions[3]) != PackageManager.PERMISSION_GRANTED) {

                // if permission not granted then request
                ActivityCompat.requestPermissions(PreviewScoringActivity.this, permissions, REQUESTCODE_GALLEY);
            } else {
                openGalley();
                dialog.dismiss();

            }

        });
        // click camera
        camera.setOnClickListener(v -> {
            // check permission for camera
            if (ActivityCompat.checkSelfPermission(
                    PreviewScoringActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            PreviewScoringActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            PreviewScoringActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissions, REQUESTCODE_GALLEY);
            } else {
                File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
                startActivityForResult(CameraActivity.getJumpIntent(PreviewScoringActivity.this, false, storageDir), REQUEST_IMAGE_CAPTURE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUESTCODE_GALLEY) { // Kiểm tra mã yêu cầu quyền
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utility.showToast(PreviewScoringActivity.this, "Permission granted !!");
            } else {
                Utility.showToast(PreviewScoringActivity.this, "The app needs permission to access gallery");
            }
        }
    }


    private void openGalley() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY);
    }

    private String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = sdf.format(new Date());
        return "IMG_" + timeStamp + getRandomString(6) + ".jpg"; // Thay ".jpg" bằng phần mở rộng của file ảnh thích hợp.
    }

    private String getRandomString(int length) {
        // Chuỗi ký tự để tạo mã ngẫu nhiên
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        // Tạo mã ngẫu nhiên bằng cách chọn ký tự ngẫu nhiên từ chuỗi characters
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File croppedFile = (File) data.getSerializableExtra(CameraActivity.EXTRA_CROPPED_FILE);

            if (croppedFile != null && croppedFile.exists()) {
                Bitmap croppedBitmap = BitmapFactory.decodeFile(croppedFile.getAbsolutePath());

                int rotationDegrees = 0;
                Matrix matrix = new Matrix();
                matrix.postRotate(rotationDegrees);
                Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, true);

                Uri rotatedImageUri = getImageUri(rotatedBitmap);
                imageList.add(rotatedImageUri);

                updateImageAdapter();
            } else {
                Toast.makeText(this, "Failed to load cropped bitmap", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getClipData() != null) {
                int numberImage = data.getClipData().getItemCount();
                for (int i = 0; i < numberImage; i++) {
                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                    startCropActivity(selectedImageUri);
                }
            } else if (data.getData() != null) {
                Uri selectedImageUri = data.getData();
                startCropActivity(selectedImageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri croppedImageUri = result.getUri();

                imageList.add(croppedImageUri);
                updateImageAdapter();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startCropActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(PreviewScoringActivity.this);
    }

    private Uri getImageUri(Bitmap bitmap) {
        try {
            File imageFile = createImageFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        String currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
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
            public void onResponse(@NotNull Call<SubjectResponse> call,@NotNull Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    SubjectResponse subjectListResponse = response.body();
                    List<SubjectResponse.SubjectItem> subjects = subjectListResponse.getContent();
                    showSubjectSelectionDialog(subjects);
                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(@NotNull Call<SubjectResponse> call, @NotNull Throwable t) {
                // Xử lý thất bại
            }
        });
    }

    private void showSubjectSelectionDialog(List<SubjectResponse.SubjectItem> subjects) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PreviewScoringActivity.this);
        LayoutInflater inflater = LayoutInflater.from(PreviewScoringActivity.this);

        View dialogView = inflater.inflate(R.layout.custom_subject_selection_dialog, null);
        builder.setView(dialogView);

        RecyclerView recyclerViewSubjects = dialogView.findViewById(R.id.recyclerViewSubjects);
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose);

        recyclerViewSubjects.setLayoutManager(new LinearLayoutManager(PreviewScoringActivity.this));
        SubjectComboBoxAdapter subjectAdapter = new SubjectComboBoxAdapter(PreviewScoringActivity.this, subjects);
        recyclerViewSubjects.setAdapter(subjectAdapter);
        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
        recyclerViewSubjects.addItemDecoration(new RecyclerViewItemDecoration(PreviewScoringActivity.this, dividerHeight));
        AlertDialog dialog = builder.create();
        subjectAdapter.setOnItemClickListener(subjectItem -> {
            txtSubject.setText(subjectItem.getTitle());
            subjectId = subjectItem.getId();
            dialog.dismiss();
        });

        dialog.show();

        btnClose.setOnClickListener(view -> dialog.dismiss());
    }


    private void deleteScoringDB() {
        apiService.saveScoringResult(scoringPreviewResDTO.getTmpFileCode(), "DELETE").enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveScoringDB() {
        progressDialogSaveDB.show();
        apiService.saveScoringResult(scoringPreviewResDTO.getTmpFileCode(), "SAVE").enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    progressDialogSaveDB.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(), "Lưu kết quả thành công", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialogSaveDB.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(), "Lưu kết quả thành công 1", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                progressDialogSaveDB.dismiss();
                finish();
                Toast.makeText(getApplicationContext(), "Lưu kết quả thành công 2", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void scoringMultiExam(String examClassCode) {

        progressDialog1.show();
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri imageUri : imageList) {
            // Kiểm tra xem ảnh có trong fileAttachDTOS không
            if (!isImageInFileAttachDTOS(imageUri)) {
                newImages.add(imageUri);

                // Chuyển đổi Uri thành File
                String newFileName = generateFileName();

                // Chuyển đổi Uri thành đường dẫn mới với tên tệp mới
                String newPath = FileStorage.getPathFromUri(PreviewScoringActivity.this, imageUri);

                // Tạo File mới từ đường dẫn mới
                File file = new File(newPath);

                // Tạo RequestBody từ File
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", newFileName, requestFile);
                partList.add(filePart);
            }
        }
        if (partList.isEmpty()) {
            progressDialog1.dismiss();
            return;
        }
        // get api service
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());


        Call<Void> call = apiService.uploadStudentTestImages(examClassCode, partList);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    imgRecyclerview.setVisibility(View.GONE);
                    progressDialog1.dismiss();
                    progressDialog.show();
                    loadScoredStudent();
                    recyclerViewScoring.setVisibility(View.VISIBLE);
                } else {
                    Utility.showToast(PreviewScoringActivity.this, "Error: " + response.code() + response.message());
                    progressDialog.dismiss();
                    progressDialog1.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Utility.showToast(PreviewScoringActivity.this, t.getMessage());
                Log.e("LOG_BUG", "onFailure: " + t.getMessage());
                progressDialog.dismiss();
                progressDialog1.dismiss();
            }
        });

    }
    private boolean isImageInFileAttachDTOS(Uri imageUri) {
        for (FileAttachDTO fileAttachDTO : fileAttachDTOS) {
            String modifiedUrl = changeBaseUrl(fileAttachDTO.getFilePath());
            Uri uri = Uri.parse(modifiedUrl);
            if (uri.equals(imageUri)) {
                return true;
            }
        }
        return false;
    }

    private void loadScoredStudent() {
        apiService.loadScoredStudentTestSet(examClassCode).enqueue(new Callback<ScoringPreviewResDTO>() {
            @Override
            public void onResponse(@NotNull Call<ScoringPreviewResDTO> call,@NotNull Response<ScoringPreviewResDTO> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    scoringPreviewResDTO = response.body();
                    if (scoringPreviewResDTO != null) {
                        scoringPreviewItemDTOList = scoringPreviewResDTO.getPreviews();
                        setupRecyclerView(scoringPreviewItemDTOList);
                        Toast.makeText(PreviewScoringActivity.this, " thanh cong", Toast.LENGTH_SHORT).show();
                        imgCount.setVisibility(View.GONE);
                        btnSaveScoring.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(PreviewScoringActivity.this, " response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.errorBody() != null) {
                        try {
                            String errorBodyString = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBodyString);

                            String errorCode = errorJson.optString("code", "");
                            String errorMessage = errorJson.optString("values", "");

                            if ("error.student.exam.class.not_found".equals(errorCode)) {
                                showErrorMessage("Mã số sinh viên "+ errorMessage +" không có trong lớp thi!" );
                            } else if ("error.test.set.not.found".equals(errorCode)) {
                                showErrorMessage("Không có mã đề thi "+errorMessage+" trong cơ sở dữ liệu!");
                            } else {
                                showErrorMessage("Lỗi khác: " + errorMessage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showErrorMessage("Xảy ra lỗi khi xử lý thông báo lỗi");
                        }
                    } else {
                        showErrorMessage("Error");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ScoringPreviewResDTO> call,@NotNull Throwable t) {
                progressDialog.dismiss();
                imgCount.setVisibility(View.GONE);
                Toast.makeText(PreviewScoringActivity.this, "lỗi", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showErrorMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Lỗi")
                .setCancelable(false)
                .setPositiveButton("Đóng", (dialog, id) -> dialog.dismiss());

        final AlertDialog alert = builder.create();
        alert.show();
        imgRecyclerview.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (alert.isShowing()) {
                alert.dismiss();
            }
        }, 5000);
        progressDialog.dismiss();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerView(List<ScoringPreviewItemDTO> scoringPreviewItemDTOList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewScoring.setLayoutManager(layoutManager);
        scoringPreviewAdapter = new ScoringPreviewAdapter(scoringPreviewItemDTOList, this);
        recyclerViewScoring.setAdapter(scoringPreviewAdapter);
        scoringPreviewAdapter.notifyDataSetChanged();
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
                        Dialog dialog = new Dialog(PreviewScoringActivity.this);
                        dialog.setContentView(R.layout.dialog_class_code_list);
                        Window window = dialog.getWindow();
                        if (window != null) {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int dialogWidth = (int) (displayMetrics.widthPixels * 0.9);
                            window.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                        dialog.setCancelable(true);
                        TextView textViewDialogTitle = dialog.findViewById(R.id.textViewDialogTitleExamClassCode);
                        textViewDialogTitle.setText("Lớp thi");

                        RecyclerView recyclerViewSemesters = dialog.findViewById(R.id.recyclerViewExamClassCode);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(PreviewScoringActivity.this);
                        recyclerViewSemesters.setLayoutManager(layoutManager);

                        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
                        recyclerViewSemesters.addItemDecoration(new RecyclerViewItemDecoration(PreviewScoringActivity.this, dividerHeight));

                        ExamClassCodeSearchAdapter adapter = new ExamClassCodeSearchAdapter(PreviewScoringActivity.this, examClassList);
                        recyclerViewSemesters.setAdapter(adapter);
                        adapter.setOnItemClickListener(iCommonIdCode -> {
                            editTextClassCode.setText(iCommonIdCode.getCode());
                            examClassCode = iCommonIdCode.getCode();
                            imageAdapter.setExamClassCode(examClassCode);
                            imageAdapter.clearImages();
                            searchScoringNoScoring(examClassCode);
                            dialog.dismiss();
                        });

                        dialog.show();
                    } else {
                        Toast.makeText(PreviewScoringActivity.this, "Học phần không có môn thi", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PreviewScoringActivity.this, "thất bại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<ICommonIdCode>> call,@NotNull Throwable t) {
                Toast.makeText(PreviewScoringActivity.this, "lỗi", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void searchScoringNoScoring(String examClassCode) {
        apiService.getListFileInExFolder(examClassCode).enqueue(new Callback<List<FileAttachDTO>>() {
            @Override
            public void onResponse(@NotNull Call<List<FileAttachDTO>> call,@NotNull Response<List<FileAttachDTO>> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    fileAttachDTOS = response.body();
                    if (fileAttachDTOS != null) {
                        recyclerViewNoScoring.setVisibility(View.GONE);
                        //imgRecyclerview.setVisibility(View.GONE);
                        imageAdapter.setFileAttachDTOS(fileAttachDTOS);
                        imageAdapter.setImages(convertFileAttachDTOToUriList(fileAttachDTOS));
                        imgCount.setText("Số lượng(" + imageList.size() + ")");
                        Toast.makeText(PreviewScoringActivity.this, " thanh cong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PreviewScoringActivity.this, " null", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<FileAttachDTO>> call,@NotNull Throwable t) {

            }
        });
    }
    private ArrayList<Uri> convertFileAttachDTOToUriList(List<FileAttachDTO> fileAttachDTOS) {
        ArrayList<Uri> uriList = new ArrayList<>();
        for (FileAttachDTO fileAttachDTO : fileAttachDTOS) {
            String modifiedUrl = changeBaseUrl(fileAttachDTO.getFilePath());
            Uri uri = Uri.parse(modifiedUrl);
            imageList.add(uri);
        }
        return imageList;
    }
    private String changeBaseUrl(String originalUrl) {
        return originalUrl.replace(BaseUrlUtils.BaseUrl.URL_LOCAL, BaseUrlUtils.BaseUrl.URL_ORIGIN);
    }
    private void updateImageAdapter() {
        imgCount.setText("Số lượng(" + imageList.size() + ")");
        imageAdapter.notifyDataSetChanged();
    }
//    private void setupRecyclerViewNoScoring(List<FileAttachDTO> fileAttachDTOS) {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerViewNoScoring.setLayoutManager(layoutManager);
//        ImageNoScoringAdapter imageNoScoringAdapter = new ImageNoScoringAdapter(this, fileAttachDTOS, examClassCode);
//        recyclerViewNoScoring.setAdapter(imageNoScoringAdapter);
//        imageNoScoringAdapter.notifyDataSetChanged();
//    }

    private void choseSemesterId() {
        Call<List<SemesterBox>> call = RetrofitClient.getApiService(getApplicationContext()).getListSemester("");
        call.enqueue(new Callback<List<SemesterBox>>() {
            @Override
            public void onResponse(@NotNull Call<List<SemesterBox>> call,@NotNull Response<List<SemesterBox>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SemesterBox> semesterBoxes = response.body();
                    if (!semesterBoxes.isEmpty()) {
                        Dialog dialog = new Dialog(PreviewScoringActivity.this);
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
                        LinearLayoutManager layoutManager = new LinearLayoutManager(PreviewScoringActivity.this);
                        recyclerViewSemesters.setLayoutManager(layoutManager);
                        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
                        recyclerViewSemesters.addItemDecoration(new RecyclerViewItemDecoration(PreviewScoringActivity.this, dividerHeight));

                        SemesterAdapter adapter = new SemesterAdapter(semesterBoxes, PreviewScoringActivity.this);
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

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
        );
        snackbar.show();
    }

    private void buildShowcase(View view, String title, String content) {
        GuideView guideView = new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(content)
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.outside) //optional - default DismissType.targetView
                .setTargetView(view)
                .setContentTextSize(16)
                .setTitleTextSize(18)
                .setGuideListener(v -> {
                    showcaseIndex++;
                    showShowcase();
                })
                .build();
        guideView.show();
    }

    private void showShowcase() {
        switch (showcaseIndex) {
            case 0:
                buildShowcase(binding.txtSemesterTestSearch, "Bước 1", "Chọn học kì tương ứng với bài thi");
                break;
            case 1:
                buildShowcase(binding.txtSubjectTestSearch, "Bước 2", "Chọn học phần bài thi");
                break;
            case 2:
                buildShowcase(binding.editTextScoring, "Bước 3", "Chọn mã lớp thi của bài thi");
                break;
            case 3:
                buildShowcase(binding.pickImageFromDevice, "Bước 4", "Chụp ảnh bài thi từ camera hoặc chọn từ thư viên");
                break;
            case 4:
                buildShowcase(binding.btnScoring, "Bước 5", "Sau khi chọn đủ thông tin bấm vào nút này để bắt đầu chấm bài");
                break;
            default:
                // No more showcases
        }
    }
}