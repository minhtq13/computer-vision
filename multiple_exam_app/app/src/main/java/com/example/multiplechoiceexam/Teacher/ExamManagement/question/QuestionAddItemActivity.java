package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.HtmlUtils;
import com.example.multiplechoiceexam.Utils.UpdateInterface;
import com.example.multiplechoiceexam.Utils.Utility;
import com.example.multiplechoiceexam.dto.answer.AnswerRequest;
import com.example.multiplechoiceexam.dto.question.MultiQuestionRequest;
import com.example.multiplechoiceexam.dto.question.QuestionLevelEnum;
import com.example.multiplechoiceexam.dto.question.SingleQuestionRequest;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAddItemActivity extends AppCompatActivity {

    public final static int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_CAPTURE_ANSWER = 4;
    private static final int PICK_IMAGE_REQUEST_ANSWER = 3;
    public static final int  REQUEST_RECORD_AUDIO_PERMISSION = 5;
    public static final int RESULT_SPEECH = 6;
    public static final int RESULT_SPEECH_ANSWER = 7;
    private final int REQUESTCODE_GALLEY =  123;
    private EditText questionEditText;
    private EditText answerEditText;
    private Button chooseImageButton,deleteAnswerButton,saveQuestionButton,addAnswerButton ;
    private LinearLayout answerLayout;
    LinearLayout answerEntry, innerLayout, innerLayout2;
    private ImageView imageViewQuestion,selectButton, answerImageView;
    private ApiService apiService;
    private boolean isCorrectSelected = false;
    private CheckBox checkBox;
    String userEnteredHtmlContent,userEnteredHtmlContentAnswer,currentPhotoPath;
    private List<View> answerViews = new ArrayList<>();
    private Spinner levelSpinner;
    private QuestionLevelEnum selectedLevel;
    String  imageBase64,imageBase64Answer;
    private Long chapterNo;
    private ImageView voiceInputButton, voiceInputButtonAnswer, removeImageButton;
    private UpdateInterface updateInterface;

    boolean checkQuestion = false;
    private ProgressDialog progressDialog;
    private String permissions[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add_item);

        apiService = RetrofitClient.getApiService(getApplicationContext());
        chapterNo = getIntent().getLongExtra("chapterNo",-1);

        questionEditText = findViewById(R.id.questionEditText);
        answerLayout = findViewById(R.id.answerLayout);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);
        addAnswerButton = findViewById(R.id.addAnswerButton);
        levelSpinner = findViewById(R.id.levelSpinner);
        voiceInputButton = findViewById(R.id.voiceInputButton);
        removeImageButton = findViewById(R.id.removeImageButton);
        imageViewQuestion = findViewById(R.id.image_view_question);
        selectButton = findViewById(R.id.chooseImageBtn);
        progressDialog = new ProgressDialog(QuestionAddItemActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tạo câu hỏi...");
        voiceInputButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(QuestionAddItemActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognition();
            } else {
                ActivityCompat.requestPermissions(QuestionAddItemActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        });
        imageViewQuestion.setOnClickListener(v -> {
            if (imageViewQuestion.getVisibility() == View.VISIBLE) {
                showImageDialog();
            }
        });

        removeImageButton.setOnClickListener(v -> removeImage());
        
        selectButton.setOnClickListener(view -> {
            checkQuestion = true;
            Dialog dialog = new Dialog(QuestionAddItemActivity.this);
            dialog.setContentView(R.layout.dialog_option_select_img);
            ImageView camera =  dialog.findViewById(R.id.camera_img);
            ImageView galley =  dialog.findViewById(R.id.galley_img);
            Button cancel = dialog.findViewById(R.id.cancel_button);
            cancel.setOnClickListener(v -> dialog.dismiss());
            galley.setOnClickListener(v -> {
                // check permission for galley
                if (ActivityCompat.checkSelfPermission(
                        QuestionAddItemActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                                QuestionAddItemActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                                QuestionAddItemActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                                QuestionAddItemActivity.this, permissions[3]) != PackageManager.PERMISSION_GRANTED) {

                    // if permission not granted then request
                    ActivityCompat.requestPermissions(QuestionAddItemActivity.this,permissions, REQUESTCODE_GALLEY);
                }
                else {
                    openGalley();
                    dialog.dismiss();
                }

            });
            // click camera
            camera.setOnClickListener(v -> {
                // check permission for camera
                if (ActivityCompat.checkSelfPermission(
                        QuestionAddItemActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                                QuestionAddItemActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                                QuestionAddItemActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {

                    // if permission not granted then request
                    requestPermissions(permissions, REQUESTCODE_GALLEY);
                }
                else {
                    String fileName = "photo";
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    try {
                        File imageFile= File.createTempFile(fileName,".jpg",storageDir);
                        currentPhotoPath = imageFile.getAbsolutePath();
                        Uri imageUri =  FileProvider.getUriForFile(QuestionAddItemActivity.this,
                                "com.example.multiplechoiceexam.fileprovider",imageFile);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                        dialog.dismiss();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }


            });

            dialog.show();
        });


        levelDropdown();

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLevel = QuestionLevelEnum.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        saveQuestionButton.setOnClickListener(v -> {
            progressDialog.show();
            saveQuestionAndAnswers();
        });

        addAnswerButton.setOnClickListener(v -> addAnswerField());
    }

    private void removeImage() {
        imageViewQuestion.setVisibility(View.GONE);
        removeImageButton.setVisibility(View.GONE);
        currentPhotoPath = null;
        imageBase64 = null;
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
            questionEditText.setText("");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ chuyển giọng thành văn bản", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void showImageDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        BitmapDrawable drawable = (BitmapDrawable) imageViewQuestion.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        dialogImageView.setImageBitmap(bitmap);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void openGalley() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void levelDropdown() {

        LevelSpinnerAdapter adapter = new LevelSpinnerAdapter(this, Arrays.asList(QuestionLevelEnum.values()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUESTCODE_GALLEY) { // Kiểm tra mã yêu cầu quyền
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utility.showToast(QuestionAddItemActivity.this,"Permission granted !!");
            } else {
                Utility.showToast(QuestionAddItemActivity.this,"The app needs permission to access gallery");
            }
        }
    }

    private void addAnswerField() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout  answerEntry = (LinearLayout) inflater.inflate(R.layout.answer_field_layout, null);

        checkBox = answerEntry.findViewById(R.id.checkboxAnswer);
        innerLayout = answerEntry.findViewById(R.id.innerLayout);
        voiceInputButtonAnswer =answerEntry.findViewById(R.id.voiceInputButtonAnswer);
        answerImageView = answerEntry.findViewById(R.id.answerImageView);
        answerEditText = answerEntry.findViewById(R.id.answerEditText);
        chooseImageButton = answerEntry.findViewById(R.id.chooseImageButton);
        deleteAnswerButton = answerEntry.findViewById(R.id.deleteAnswerButton);
        innerLayout2 = answerEntry.findViewById(R.id.innerLayout2);
        voiceInputButtonAnswer.setOnClickListener(view -> startVoiceRecognitionAnswer(answerEntry));
        chooseImageButton.setOnClickListener(v -> {
            checkQuestion = false;
            onChooseImageClickAnswer(answerEntry);
        });
        answerImageView.setOnClickListener(v -> {
            if (answerImageView.getVisibility() == View.VISIBLE) {
                showImageDialogAnswer();
            }
        });
        deleteAnswerButton.setOnClickListener(v -> {
            answerLayout.removeView(answerEntry);
            answerViews.remove(answerEntry);
            imageBase64Answer =null;
        });
        answerViews.add(answerEntry);
        answerLayout.addView(answerEntry);


    }

    private void startVoiceRecognitionAnswer(final LinearLayout answerEntry) {
        this.answerEntry = answerEntry;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        try {
            startActivityForResult(intent, RESULT_SPEECH_ANSWER);
            answerEditText.setText("");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ chuyển giọng thành văn bản", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void showImageDialogAnswer() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        BitmapDrawable drawable = (BitmapDrawable) answerImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        dialogImageView.setImageBitmap(bitmap);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void onChooseImageClickAnswer(final LinearLayout answerEntry) {
        this.answerEntry = answerEntry;
        Dialog dialog = new Dialog(QuestionAddItemActivity.this);
        dialog.setContentView(R.layout.dialog_option_select_img);
        ImageView camera = dialog.findViewById(R.id.camera_img);
        ImageView galley =dialog.findViewById(R.id.galley_img);
        Button cancel =  dialog.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(v -> dialog.dismiss());
        galley.setOnClickListener(v -> {
            // check permission for galley
            if (ActivityCompat.checkSelfPermission(
                    QuestionAddItemActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            QuestionAddItemActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            QuestionAddItemActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            QuestionAddItemActivity.this, permissions[3]) != PackageManager.PERMISSION_GRANTED) {

                // if permission not granted then request
                ActivityCompat.requestPermissions(QuestionAddItemActivity.this,permissions, REQUESTCODE_GALLEY);
            }
            else {
                openGalleyAnswer();
                dialog.dismiss();
            }

        });
        // click camera
        camera.setOnClickListener(v -> {
            // check permission for camera
            if (ActivityCompat.checkSelfPermission(
                    QuestionAddItemActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            QuestionAddItemActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            QuestionAddItemActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {

                // if permission not granted then request
                requestPermissions(permissions, REQUESTCODE_GALLEY);
            }
            else {
                String fileName = "photo";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile= File.createTempFile(fileName,".jpg",storageDir);
                    currentPhotoPath = imageFile.getAbsolutePath();
                    Uri imageUri =  FileProvider.getUriForFile(QuestionAddItemActivity.this,
                            "com.example.multiplechoiceexam.fileprovider",imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,REQUEST_IMAGE_CAPTURE_ANSWER);
                    dialog.dismiss();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        });
        dialog.show();

    }

    private void openGalleyAnswer() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST_ANSWER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH && resultCode == RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            questionEditText.setText(text.get(0));
        }else if (requestCode == RESULT_SPEECH_ANSWER && resultCode == RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (answerEntry != null) {
                EditText answerEditText = answerEntry.findViewById(R.id.answerEditText);
                if (answerEditText != null) {
                    answerEditText.setText(text.get(0));
                } else {
                    Toast.makeText(this, "answerEditText is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "answerEntry is null", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cropImage(Uri.fromFile(new File(currentPhotoPath)));
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            cropImage(data.getData());
        } else if (requestCode == PICK_IMAGE_REQUEST_ANSWER && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            cropImageAnswer(data.getData());
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_ANSWER && resultCode == RESULT_OK) {
            cropImageAnswer(Uri.fromFile(new File(currentPhotoPath)));
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null) {
                handleCroppedImage(result.getUri(), answerEntry);
            } else {
                Toast.makeText(this, "Không thể xử lý ảnh được cắt", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cropImageAnswer(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    private void cropImage(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    private void handleCroppedImage(Uri croppedUri,final LinearLayout answerEntry) {
        try {
            if (checkQuestion) {
                Picasso.get().load(croppedUri).into(imageViewQuestion);
                imageViewQuestion.setVisibility(View.VISIBLE);
                removeImageButton.setVisibility(View.VISIBLE);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedUri);
                imageBase64 = HtmlUtils.convertBitmapToBase64(bitmap);
            } else {
                if (answerEntry != null) {
                    innerLayout = answerEntry.findViewById(R.id.innerLayout);
                    answerImageView = innerLayout.findViewById(R.id.answerImageView);
                    if (answerImageView != null) {
                        answerImageView.setVisibility(View.VISIBLE);
                        Picasso.get().load(croppedUri).into(answerImageView);
                    } else {
                        Toast.makeText(this, "answerImageView is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "answerEntry is null", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveQuestionAndAnswers() {
        List<AnswerRequest> answerList = new ArrayList<>();

        for (View answerView : answerViews) {
            CheckBox checkBox = answerView.findViewById(R.id.checkboxAnswer);
            EditText answerEditText = answerView.findViewById(R.id.answerEditText);
            ImageView answerImageView = answerView.findViewById(R.id.answerImageView);

            boolean isCorrect = checkBox.isChecked();
            userEnteredHtmlContentAnswer = "<p>" + answerEditText.getText().toString() + "</p>";
            imageBase64Answer = null;

            if (answerImageView.getVisibility() == View.VISIBLE) {
                Bitmap bitmap = ((BitmapDrawable) answerImageView.getDrawable()).getBitmap();
                imageBase64Answer = HtmlUtils.convertBitmapToBase64(bitmap);
                userEnteredHtmlContentAnswer += "<p><img src='data:image/png;base64," + imageBase64Answer + "'/></p>";
            }

            AnswerRequest answerRequest = new AnswerRequest(userEnteredHtmlContentAnswer, isCorrect);
            answerList.add(answerRequest);
        }

        userEnteredHtmlContent = "<p>" + questionEditText.getText().toString() + "</p>";

        if (imageBase64 != null) {
            userEnteredHtmlContent += "<p><img src='data:image/png;base64," + imageBase64 + "'/></p>";
        }

        SingleQuestionRequest questionRequest = new SingleQuestionRequest();
        questionRequest.setContent(userEnteredHtmlContent);
        questionRequest.setLevel(selectedLevel);
        questionRequest.setLstAnswer(answerList);

        MultiQuestionRequest multipleQuestionRequest = new MultiQuestionRequest();
        multipleQuestionRequest.getLstQuestion().add(questionRequest);
        multipleQuestionRequest.setChapterId(chapterNo);

        Call<Void> call = apiService.createQuestion(multipleQuestionRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(QuestionAddItemActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(QuestionAddItemActivity.this, "Có lỗi xảy ra khi gửi câu hỏi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                progressDialog.dismiss();
                finish();
                Toast.makeText(QuestionAddItemActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
