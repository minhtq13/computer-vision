package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.ExamManagement.answer.AnswerAdapter;
import com.example.multiplechoiceexam.dto.answer.AnswerResponse;
import com.example.multiplechoiceexam.dto.question.QuestionResponse;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDetailActivity extends AppCompatActivity {

    TextView textViewTopicText, textViewLevel;
    ImageView imageView;
    RecyclerView recyclerViewAnswers;
    ApiService apiService;
    List<AnswerResponse> answers;
    Long questionId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        apiService = RetrofitClient.getApiService(getApplicationContext());
        answers = new ArrayList<>();


        textViewTopicText = findViewById(R.id.textViewTopicText);
        textViewLevel = findViewById(R.id.textViewLevel);
        imageView = findViewById(R.id.imageView);
        recyclerViewAnswers = findViewById(R.id.recyclerViewAnswers);
        recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(this));

        imageView.setOnClickListener(view -> showImageDialog());

        questionId = getIntent().getLongExtra("questionId", -1);
        getQuestionDetail(questionId);

    }
    private void showImageDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        dialogImageView.setImageBitmap(bitmap);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void getQuestionDetail(Long questionId) {
        Call<QuestionResponse> call = apiService.getQuestionById(questionId);

        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionResponse> call, @NotNull Response<QuestionResponse> response) {
                if (response.isSuccessful()) {
                    QuestionResponse questionResponse = response.body();
                    assert questionResponse != null;
                    String htmlTransfer = questionResponse.getContent();
                    Document doc = Jsoup.parse(htmlTransfer, "", Parser.xmlParser());

                    //Element firstParagraph = doc.select("p").first();
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
                        textViewTopicText.setText(combinedText.toString());
                    } else {
                        textViewTopicText.setText(doc.text());
                    }

                    // Tìm và trích xuất chuỗi Base64 từ thẻ img
                    Element img = doc.select("img").first();
                    String base64Image = "";
                    if (img != null) {
                        String imgSrc = img.attr("src");
                        Pattern pattern = Pattern.compile("base64,([^\\\"]+)");
                        Matcher matcher = pattern.matcher(imgSrc);
                        if (matcher.find()) {
                            base64Image = matcher.group(1);
                        }
                    }

                    assert base64Image != null;
                    if (!base64Image.isEmpty()) {
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    //                    Glide.with(getApplicationContext()).asBitmap().load(decodedString).into(imageView);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);

                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                    // Lấy đối tượng Spanned từ chuỗi HTML
//                    Spanned spannedHtml = HtmlUtils.getSpannedFromHtml(htmlTransfer);
//
//                    // Hiển thị text trong TextView
//                    textViewTopicText.setText(HtmlUtils.getTextFromHtml(htmlTransfer));
//                    HtmlUtils.setHtmlTextWithFirstImage(getApplicationContext(),photoView,htmlTransfer);

//                    String htmlTranfer = questionResponse.getContent();
//                    HtmlUtils.setHtmlTextWithImage(textViewTopicText,htmlTransfer);
                    textViewLevel.setText(questionResponse.getCode());
//                    String imageUrl = null;
//                    if (questionResponse.getLstImage() != null) {
//                        imageUrl = "http://192.168.1.9:8088/e-learning//" + questionResponse.getLstImage().toString();
//                    }
//                    Glide.with(QuestionDetailActivity.this)
//                            .load(imageUrl)
//                            .into(imageView);
                    answers = questionResponse.getLstAnswer();
                    showRecyclerView(answers);
                    Toast.makeText(QuestionDetailActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuestionDetailActivity.this, "thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionResponse> call,@NotNull Throwable t) {
                Toast.makeText(QuestionDetailActivity.this, "lỗi gọi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showRecyclerView(List<AnswerResponse> answerss) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAnswers.setLayoutManager(layoutManager);
        AnswerAdapter adapter = new AnswerAdapter(QuestionDetailActivity.this, answerss);
        recyclerViewAnswers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}