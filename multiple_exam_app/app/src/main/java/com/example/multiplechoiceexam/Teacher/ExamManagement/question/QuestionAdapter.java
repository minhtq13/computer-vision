package com.example.multiplechoiceexam.Teacher.ExamManagement.question;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Utils.UpdateInterface;
import com.example.multiplechoiceexam.dto.question.QuestionListDTO;
import com.github.chrisbanes.photoview.PhotoView;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    Context context;
    List<QuestionListDTO> questionResponseList;
    private Long chapterId = -1L;
    private UpdateInterface updateInterface;
    private static final int UPDATE_QUESTION_REQUEST_CODE = 1;

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public QuestionAdapter(Context context, List<QuestionListDTO> questionResponseList,UpdateInterface updateInterface) {
        this.context = context;
        this.questionResponseList = questionResponseList;
        this.updateInterface = updateInterface;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_row_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        QuestionListDTO questionResponse = questionResponseList.get(position);

        holder.questionId.setText(String.valueOf(position+1));
        String htmlTransfer = questionResponse.getContent();
        Document doc = null;
        if (htmlTransfer != null) {
            doc = Jsoup.parse(htmlTransfer, "", Parser.xmlParser());
        }

        //String textContent = "";
        List<String> paragraphTexts = new ArrayList<>();
        String base64Image = "";
        if (doc != null) {
            Elements paragraphElements = doc.select("p");
            for (Element paragraphElement : paragraphElements) {
                String paragraphText = paragraphElement.text();
                paragraphTexts.add(paragraphText);
            }

            Element img = doc.select("img").first();
            if (img != null) {
                String imgSrc = img.attr("src");
                Pattern pattern = Pattern.compile("base64,([^\\\"]+)");
                Matcher matcher = pattern.matcher(imgSrc);
                if (matcher.find()) {
                    base64Image = matcher.group(1);
                }
            }
        }

        if (!paragraphTexts.isEmpty()) {
            StringBuilder combinedText = new StringBuilder();
            for (String paragraphText : paragraphTexts) {
                combinedText.append(paragraphText).append("\n");
            }
            holder.questionTitle.setText(combinedText.toString());
        } else {
            assert doc != null;
            holder.questionTitle.setText(doc.text());
        }
        //holder.questionTitle.setText(textContent);

        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(context);
        List<String> userRoles = accountSharedPreferences.getRoles();

        // If user has the STUDENT role, hide update and delete buttons
        if (userRoles.contains("ROLE_STUDENT") || chapterId == -1L) {
            holder.editQuestion.setVisibility(View.GONE);
            holder.deleteQuestion.setVisibility(View.GONE);
        } else {
            holder.editQuestion.setVisibility(View.VISIBLE);
            holder.deleteQuestion.setVisibility(View.VISIBLE);

            holder.editQuestion.setOnClickListener(view -> {
                Intent intent = new Intent(context, QuestionUpdateActivity.class);
                intent.putExtra("questionId",questionResponse.getId());
                context.startActivity(intent);
            });
            holder.deleteQuestion.setOnClickListener(view -> showDeleteConfirmationDialog(questionResponse, position));
        }
        holder.photoView.setOnClickListener(view -> {
            if (context instanceof Activity && !((Activity) context).isFinishing()) {
                Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_image);

                PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

                BitmapDrawable drawable = (BitmapDrawable) holder.photoView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                dialogImageView.setImageBitmap(bitmap);
                dialogImageView.setOnClickListener(v -> dialog.dismiss());

                dialog.show();
            }
        });

        assert base64Image != null;
        if (!base64Image.isEmpty()) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//            Glide.with(context).asBitmap().load(decodedString).into(holder.photoView);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.photoView.setImageBitmap(bitmap);
            holder.photoView.setVisibility(View.VISIBLE);

        } else {
            holder.photoView.setVisibility(View.GONE);
        }
//        holder.questionTitle.setText(questionResponse.getContent());
        Integer level = questionResponse.getLevel();

        if (level == 0) {
            holder.questionLevel.setText("Dễ");
            holder.questionLevel.setTextColor(Color.GREEN);
        } else if(level == 1){
            holder.questionLevel.setText("Trung bình");
            holder.questionLevel.setTextColor(Color.YELLOW);
        }else {
            holder.questionLevel.setText("Khó");
            holder.questionLevel.setTextColor(Color.RED);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, QuestionDetailActivity.class);
            intent.putExtra("questionId",questionResponse.getId());
            context.startActivity(intent);
        });

//        holder.deleteQuestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDeleteConfirmationDialog(questionResponse, position);
//            }
//        });
    }

    private void showDeleteConfirmationDialog(QuestionListDTO questionResponse, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> deleteItem(questionResponse.getId(), position));

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(Long id, int position) {
        RetrofitClient.getApiService(context.getApplicationContext()).disableQuestion(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (updateInterface != null) {
                    updateInterface.onUpdateComplete();
                }
                notifyDataSetChanged();
                Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (questionResponseList != null) {
            return questionResponseList.size();
        } else {
            return 0;
        }
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionId, questionTitle, questionLevel;
        ImageButton deleteQuestion,editQuestion;
        ImageView photoView;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionId = itemView.findViewById(R.id.question_id);
            questionTitle = itemView.findViewById(R.id.question_title);
            questionLevel = itemView.findViewById(R.id.question_level);
            deleteQuestion = itemView.findViewById(R.id.question_delete);
            photoView = itemView.findViewById(R.id.questionImage);
            editQuestion = itemView.findViewById(R.id.question_edit);

        }
    }

}
