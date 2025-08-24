package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
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
import com.example.multiplechoiceexam.dto.answer.AnswerRequest;
import com.example.multiplechoiceexam.dto.answer.AnswerResponse;
import com.example.multiplechoiceexam.dto.question.QuestionLevelEnum;
import com.example.multiplechoiceexam.dto.question.QuestionResponse;
import com.example.multiplechoiceexam.dto.question.QuestionUpdateDTO;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionUpdateActivity extends AppCompatActivity {

    private final static int PICK_IMAGE_REQUEST_UPDATE = 1;
    private static final int REQUEST_IMAGE_CAPTURE_UPDATE = 2;
    private static final int REQUEST_IMAGE_CAPTURE_ANSWER_UPDATE = 4;
    private static final int PICK_IMAGE_REQUEST_ANSWER_UPDATE = 3;
    public static final int  REQUEST_RECORD_AUDIO_PERMISSION_UPDATE = 5;
    public static final int RESULT_SPEECH_UPDATE = 6;
    public static final int RESULT_SPEECH_ANSWER_UPDATE = 7;
    private final int REQUESTCODE_GALLERY_UPDATE = 123;
    private ImageView imageViewQuestionUpdate,chooseImageBtnUpdate;
    private Spinner levelSpinnerUpdate;
    private EditText questionEditTextUpdate;
    private LinearLayout answerLayoutUpdate,answerEntry, innerLayout, innerLayout2;
    ImageView answerImageView;
    EditText answerEditText;
    Button chooseImageButton,deleteAnswerButton ;
    private Button addAnswerButtonUpdate,saveQuestionButtonUpdate;
    private ApiService apiService;
    private CheckBox checkBox;
    Long chapterNo, questionId;
    private List<AnswerResponse> answers;
    private ImageView voiceInputButton, voiceInputButtonAnswer,removeImageButtonUpdate;
    private String currentPhotoPathUpdate,userEnteredHtmlContentAnswerUpdate, imageBase64AnswerUpdate,userEnteredHtmlContentUpdate,imageBase64Update,answerText;
    private QuestionLevelEnum selectedLevelUpdate;
    private ProgressDialog progressDialog;
    private List<View> answerViewsUpdate = new ArrayList<>();
    private boolean checkQuestionUpdate = false;

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
        setContentView(R.layout.activity_question_update);
        answers = new ArrayList<>();
        apiService = RetrofitClient.getApiService(getApplicationContext());
        progressDialog = new ProgressDialog(QuestionUpdateActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang lưu câu hỏi...");

        Intent intent = getIntent();
        questionId = intent.getLongExtra("questionId", -1L);
        chapterNo = getIntent().getLongExtra("chapterNo",-1);
        getQuestionDetail(questionId);

        // Initialize your views
        imageViewQuestionUpdate = findViewById(R.id.image_view_question_update);
        chooseImageBtnUpdate = findViewById(R.id.chooseImageBtnUpdate);
        levelSpinnerUpdate = findViewById(R.id.levelSpinnerUpdate);
        voiceInputButton = findViewById(R.id.voiceInputButtonUpdate);
        removeImageButtonUpdate = findViewById(R.id.removeImageButtonUpdate);
        questionEditTextUpdate = findViewById(R.id.questionEditTextUpdate);
        answerLayoutUpdate = findViewById(R.id.answerLayoutUpdate);
        addAnswerButtonUpdate = findViewById(R.id.addAnswerButtonUpdate);
        saveQuestionButtonUpdate = findViewById(R.id.saveQuestionButtonUpdate);

        removeImageButtonUpdate.setOnClickListener(v -> removeImageUpdate());

        voiceInputButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(QuestionUpdateActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognitionUpdate();
            } else {
                ActivityCompat.requestPermissions(QuestionUpdateActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_UPDATE);
            }
        });
        chooseImageBtnUpdate.setOnClickListener(v -> {
            checkQuestionUpdate = true;
            showImageQuestionSelectionDialog();
        });

        levelSpinnerUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLevelUpdate = QuestionLevelEnum.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        addAnswerButtonUpdate.setOnClickListener(v -> addAnswerFieldUpdate());

        saveQuestionButtonUpdate.setOnClickListener(v -> {
            progressDialog.show();
            saveQuestionAndAnswersUpdate();
        });

        // Initialize the level dropdown
        levelDropdownUpdate();
    }

    private void removeImageUpdate() {
        imageViewQuestionUpdate.setVisibility(View.GONE);
        removeImageButtonUpdate.setVisibility(View.GONE);
        currentPhotoPathUpdate = null;
        imageBase64Update = null;
    }

    private void startVoiceRecognitionUpdate() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        try {
            startActivityForResult(intent, RESULT_SPEECH_UPDATE);
            questionEditTextUpdate.setText("");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ chuyển giọng thành văn bản", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getQuestionDetail(Long questionId) {
        Call<QuestionResponse> call = apiService.getQuestionById(questionId);

        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionResponse> call,@NotNull Response<QuestionResponse> response) {
                if (response.isSuccessful()) {
                    QuestionResponse questionResponse = response.body();
                    assert questionResponse != null;
                    String htmlTransfer = questionResponse.getContent();
                    Document doc = Jsoup.parse(htmlTransfer, "", Parser.xmlParser());

                    List<String> paragraphTexts = new ArrayList<>();
                    Elements paragraphElements = doc.select("p");
                    for (Element paragraphElement : paragraphElements) {
                        String paragraphText = paragraphElement.text();
                        paragraphTexts.add(paragraphText);
                    }

                    if (!paragraphTexts.isEmpty()) {
                        StringBuilder combinedText = new StringBuilder();
                        for (String paragraphText : paragraphTexts) {
                            combinedText.append(paragraphText).append("\n");
                        }
                        questionEditTextUpdate.setText(combinedText.toString());
                    } else {
                        questionEditTextUpdate.setText(doc.text());
                    }
//                    Element firstParagraph = doc.select("p").first();
//                    if (firstParagraph != null) {
//                        questionEditTextUpdate.setText(firstParagraph.text());
//                    } else {
//                        questionEditTextUpdate.setText(doc.text());
//                    }

                    Element img = doc.select("img").first();
                    imageBase64Update = "";
                    if (img != null) {
                        String imgSrc = img.attr("src");
                        Pattern pattern = Pattern.compile("base64,([^\\\"]+)");
                        Matcher matcher = pattern.matcher(imgSrc);
                        if (matcher.find()) {
                            imageBase64Update = matcher.group(1);
                        }
                    }

                    byte[] decodedString = Base64.decode(imageBase64Update, Base64.DEFAULT);
                    if (!imageBase64Update.isEmpty()) {
//                        Glide.with(getApplicationContext()).asBitmap().load(decodedString).into(imageViewQuestionUpdate);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageViewQuestionUpdate.setImageBitmap(bitmap);
                        imageViewQuestionUpdate.setVisibility(View.VISIBLE);

                    } else {
                        imageViewQuestionUpdate.setVisibility(View.GONE);
                    }
                    Integer levelPosition = getLevelPosition(questionResponse.getLevel());
                    levelSpinnerUpdate.setSelection(levelPosition);

                    answers = questionResponse.getLstAnswer();
                    for (AnswerResponse answer : answers) {
                        addAnswerField(answer);
                    }
                    chapterNo = questionResponse.getChapterId();
                    Toast.makeText(QuestionUpdateActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuestionUpdateActivity.this, "thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionResponse> call,@NotNull Throwable t) {
                Toast.makeText(QuestionUpdateActivity.this, "lỗi gọi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAnswerField(AnswerResponse answer) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout answerEntry = (LinearLayout) inflater.inflate(R.layout.answer_field_layout, null);

        checkBox = answerEntry.findViewById(R.id.checkboxAnswer);
        innerLayout = answerEntry.findViewById(R.id.innerLayout);
        answerImageView = answerEntry.findViewById(R.id.answerImageView);
        answerEditText = answerEntry.findViewById(R.id.answerEditText);
        voiceInputButtonAnswer =answerEntry.findViewById(R.id.voiceInputButtonAnswer);
        chooseImageButton = answerEntry.findViewById(R.id.chooseImageButton);
        deleteAnswerButton = answerEntry.findViewById(R.id.deleteAnswerButton);
        innerLayout2 = answerEntry.findViewById(R.id.innerLayout2);

        voiceInputButtonAnswer.setOnClickListener(view -> startVoiceRecognitionAnswerUpdate(answerEntry));

        chooseImageButton.setOnClickListener(v -> {
            checkQuestionUpdate = false;
            showImageSelectionDialog(answerEntry);
        });

        answerImageView.setOnClickListener(v -> {
            if (answerImageView.getVisibility() == View.VISIBLE) {
                showImageDialogAnswer(answerImageView);
            }
        });

        deleteAnswerButton.setOnClickListener(v -> removeAnswerField(answerEntry));

        answerText = extractAnswerText(answer.getContent());
        imageBase64AnswerUpdate = extractAnswerImageBase64(answer.getContent());
        answerEditText.setText(answerText);
        checkBox.setActivated(answer.getCorrected());
        byte[] decodedStringAnswer = Base64.decode(imageBase64AnswerUpdate, Base64.DEFAULT);
        if (!imageBase64AnswerUpdate.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedStringAnswer, 0, decodedStringAnswer.length);
            answerImageView.setImageBitmap(bitmap);
            answerImageView.setVisibility(View.VISIBLE);

        } else {
            answerImageView.setVisibility(View.GONE);
        }

        answerViewsUpdate.add(answerEntry);
        answerLayoutUpdate.addView(answerEntry);
    }

    private String extractAnswerText(String content) {
        Document doc = Jsoup.parse(content, "", Parser.xmlParser());
        return doc.text();
    }

    private String extractAnswerImageBase64(String content) {
        Document doc = Jsoup.parse(content, "", Parser.xmlParser());
        Element img = doc.select("img").first();
        if (img != null) {
            String imgSrc = img.attr("src");
            Pattern pattern = Pattern.compile("base64,([^\\\"]+)");
            Matcher matcher = pattern.matcher(imgSrc);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return "";
    }
    private int getLevelPosition(Integer levelCode) {
        QuestionLevelEnum[] levels = QuestionLevelEnum.values();
        for (int i = 0; i < levels.length; i++) {
            if (levels[i].getLevel().equals(levelCode)) {
                return i;
            }
        }
        return 0;
    }

    private void showImageQuestionSelectionDialog() {
        Dialog dialog = new Dialog(QuestionUpdateActivity.this);
        dialog.setContentView(R.layout.dialog_option_select_img);
        ImageView camera = dialog.findViewById(R.id.camera_img);
        ImageView gallery = dialog.findViewById(R.id.galley_img);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(v -> dialog.dismiss());

        gallery.setOnClickListener(v -> {
            if (checkStoragePermissions()) {
                openGallery();
                dialog.dismiss();
            } else {
                requestStoragePermissions();
            }
        });

        camera.setOnClickListener(v -> {
            if (checkCameraPermissions()) {
                openCamera();
                dialog.dismiss();
            } else {
                requestCameraPermissions();
            }
        });

        dialog.show();
    }

    private void showImageSelectionDialog(final LinearLayout answerEntry) {
        this.answerEntry = answerEntry;
        Dialog dialog = new Dialog(QuestionUpdateActivity.this);
        dialog.setContentView(R.layout.dialog_option_select_img);
        ImageView camera = dialog.findViewById(R.id.camera_img);
        ImageView gallery = dialog.findViewById(R.id.galley_img);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(v -> dialog.dismiss());

        gallery.setOnClickListener(v -> {
            if (checkStoragePermissions()) {
                openGallery();
                dialog.dismiss();
            } else {
                requestStoragePermissions();
            }
        });

        camera.setOnClickListener(v -> {
            if (checkCameraPermissions()) {
                openCamera();
                dialog.dismiss();
            } else {
                requestCameraPermissions();
            }
        });

        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST_UPDATE);
    }

    private void openCamera() {
        String fileName = "photo";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(fileName, ".jpg", storageDir);
            currentPhotoPathUpdate = imageFile.getAbsolutePath();
            Uri imageUri = FileProvider.getUriForFile(
                    QuestionUpdateActivity.this,
                    "com.example.multiplechoiceexam.fileprovider",
                    imageFile
            );
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_UPDATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkStoragePermissions() {
        return ActivityCompat.checkSelfPermission(
                QuestionUpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        QuestionUpdateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(
                QuestionUpdateActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUESTCODE_GALLERY_UPDATE
        );
    }

    private boolean checkCameraPermissions() {
        return ActivityCompat.checkSelfPermission(
                QuestionUpdateActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(
                QuestionUpdateActivity.this,
                new String[]{Manifest.permission.CAMERA},
                REQUESTCODE_GALLERY_UPDATE
        );
    }

    private void levelDropdownUpdate() {
        LevelSpinnerAdapter adapter = new LevelSpinnerAdapter(this, Arrays.asList(QuestionLevelEnum.values()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinnerUpdate.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH_UPDATE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            questionEditTextUpdate.setText(text.get(0));
        }else if (requestCode == RESULT_SPEECH_ANSWER_UPDATE && resultCode == RESULT_OK && data != null) {
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
        if (requestCode == REQUEST_IMAGE_CAPTURE_UPDATE && resultCode == RESULT_OK) {
            cropImage(Uri.fromFile(new File(currentPhotoPathUpdate)));
        } else if (requestCode == PICK_IMAGE_REQUEST_UPDATE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            cropImage(data.getData());
        } else if (requestCode == PICK_IMAGE_REQUEST_ANSWER_UPDATE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            cropImageAnswer(data.getData());
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_ANSWER_UPDATE && resultCode == RESULT_OK) {
            cropImageAnswer(Uri.fromFile(new File(currentPhotoPathUpdate)));
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            handleCroppedImage(CropImage.getActivityResult(data).getUri());
        }
    }
    private void cropImageAnswer(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    private void cropImage(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    private void handleCroppedImage(Uri croppedUri) {
        try {
            if (checkQuestionUpdate) {
                Picasso.get().load(croppedUri).into(imageViewQuestionUpdate);
                imageViewQuestionUpdate.setVisibility(View.VISIBLE);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedUri);
                imageBase64Update = HtmlUtils.convertBitmapToBase64(bitmap);
            } else {
                if (answerEntry != null) {
                    innerLayout = answerEntry.findViewById(R.id.innerLayout);
                    answerImageView = innerLayout.findViewById(R.id.answerImageView);
                    if (answerImageView != null) {
                        answerImageView.setVisibility(View.VISIBLE);
                        Picasso.get().load(croppedUri).into(answerImageView);
                        Toast.makeText(this, "alo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedUri);
//                imageBase64Answer = HtmlUtils.convertBitmapToBase64(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addAnswerFieldUpdate() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout answerEntry = (LinearLayout) inflater.inflate(R.layout.answer_field_layout, null);

        checkBox = answerEntry.findViewById(R.id.checkboxAnswer);
        innerLayout = answerEntry.findViewById(R.id.innerLayout);
        voiceInputButtonAnswer =answerEntry.findViewById(R.id.voiceInputButtonAnswer);
        answerImageView = answerEntry.findViewById(R.id.answerImageView);
        answerEditText = answerEntry.findViewById(R.id.answerEditText);
        chooseImageButton = answerEntry.findViewById(R.id.chooseImageButton);
        deleteAnswerButton = answerEntry.findViewById(R.id.deleteAnswerButton);
        innerLayout2 = answerEntry.findViewById(R.id.innerLayout2);
        voiceInputButtonAnswer.setOnClickListener(view -> startVoiceRecognitionAnswerUpdate(answerEntry));
        chooseImageButton.setOnClickListener(v -> {
            checkQuestionUpdate = false;
            showImageSelectionDialog(answerEntry);
        });

        answerImageView.setOnClickListener(v -> {
            if (answerImageView.getVisibility() == View.VISIBLE) {
                showImageDialogAnswer(answerImageView);
            }
        });

        deleteAnswerButton.setOnClickListener(v -> {
            removeAnswerField(answerEntry);
            answerViewsUpdate.remove(answerEntry);
            imageBase64AnswerUpdate = null;
        });

        answerViewsUpdate.add(answerEntry);
        answerLayoutUpdate.addView(answerEntry);
    }

    private void startVoiceRecognitionAnswerUpdate(final LinearLayout answerEntry) {
        this.answerEntry = answerEntry;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        try {
            startActivityForResult(intent, RESULT_SPEECH_ANSWER_UPDATE);
            answerEditText.setText("");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ chuyển giọng thành văn bản", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showImageDialogAnswer(ImageView answerImageView) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        BitmapDrawable drawable = (BitmapDrawable) imageViewQuestionUpdate.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        dialogImageView.setImageBitmap(bitmap);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void removeAnswerField(View answerEntry) {
        answerLayoutUpdate.removeView(answerEntry);
        answerViewsUpdate.remove(answerEntry);
    }

    private void saveQuestionAndAnswersUpdate() {
        List<AnswerRequest> answerList = new ArrayList<>();

        for (View answerView : answerViewsUpdate) {
            CheckBox checkBox = answerView.findViewById(R.id.checkboxAnswer);
            EditText answerEditText = answerView.findViewById(R.id.answerEditText);
            ImageView answerImageView = answerView.findViewById(R.id.answerImageView);

            boolean isCorrect = checkBox.isChecked();
            String answerText = answerEditText.getText().toString();
            Log.d("Answer Update", "Answer Text: " + answerText);
            Log.d("Answer Update", "Is Correct: " + isCorrect);
            userEnteredHtmlContentAnswerUpdate = "<p>" + answerText+ "</p>";
            imageBase64AnswerUpdate = null;

            if (answerImageView.getVisibility() == View.VISIBLE) {
                // Lấy imageBase64Answer cho câu trả lời này
                Bitmap bitmap = ((BitmapDrawable) answerImageView.getDrawable()).getBitmap();
                imageBase64AnswerUpdate = HtmlUtils.convertBitmapToBase64(bitmap);
                userEnteredHtmlContentAnswerUpdate += "<p><img src='data:image/png;base64," + imageBase64AnswerUpdate + "'/></p>";
            }

            AnswerRequest answerRequest = new AnswerRequest(userEnteredHtmlContentAnswerUpdate, isCorrect);
            answerList.add(answerRequest);
        }

        userEnteredHtmlContentUpdate = "<p>" + questionEditTextUpdate.getText().toString() + "</p>";

        if (imageBase64Update != null && !imageBase64Update.isEmpty()) {
            userEnteredHtmlContentUpdate += "<p><img src='data:image/png;base64," + imageBase64Update + "'/></p>";
        }

        QuestionUpdateDTO questionRequest = new QuestionUpdateDTO();
        questionRequest.setContent(userEnteredHtmlContentUpdate);
        questionRequest.setLevel(selectedLevelUpdate);
        questionRequest.setLstAnswer(answerList);
        questionRequest.setChapterId(chapterNo);

        updateQuestion(questionId,questionRequest);

    }

    private void updateQuestion(Long questionId, QuestionUpdateDTO questionRequest) {
        apiService.updateQuestion(questionId,questionRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(QuestionUpdateActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(QuestionUpdateActivity.this, "Có lỗi xảy ra khi gửi câu hỏi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                progressDialog.dismiss();
                finish();
                Toast.makeText(QuestionUpdateActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
