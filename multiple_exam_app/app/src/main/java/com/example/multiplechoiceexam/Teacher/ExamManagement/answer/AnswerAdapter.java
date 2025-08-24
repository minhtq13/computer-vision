package com.example.multiplechoiceexam.Teacher.ExamManagement.answer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.answer.AnswerResponse;
import com.github.chrisbanes.photoview.PhotoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private final List<AnswerResponse> answerList;
    private final List<Integer> selectedPositions;
    Context context;

    public AnswerAdapter(Context context, List<AnswerResponse> answers) {
        this.context = context;
        this.answerList = answers;
        this.selectedPositions = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AnswerResponse answer = answerList.get(position);
        holder.bind(answer);

        holder.itemView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(Integer.valueOf(position));
            } else {
                selectedPositions.add(position);
            }
            notifyDataSetChanged();
        });
        holder.photoViewAnswer.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_image);
            PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);
            BitmapDrawable drawable = (BitmapDrawable) holder.photoViewAnswer.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            dialogImageView.setImageBitmap(bitmap);
            dialogImageView.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox radioButtonAnswer;
        CardView cardView;
        TextView answerContent;
        ImageView photoViewAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButtonAnswer = itemView.findViewById(R.id.radioButtonAnswer);
            answerContent = itemView.findViewById(R.id.textViewAnswerContent);
            photoViewAnswer = itemView.findViewById(R.id.photoViewAnswer);
            cardView = itemView.findViewById(R.id.cardViewAnswer);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void bind(AnswerResponse answer) {
 //           answerContent.setText(answer.getContent());
            radioButtonAnswer.setChecked(selectedPositions.contains(getAdapterPosition()));

            String htmlTransfer = answer.getContent();
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
                answerContent.setText(combinedText.toString());
            } else {
                answerContent.setText(doc.text());
            }
//            Element paragraphElement = doc.select("p").first();
//            String textContent;

//            if (paragraphElement != null) {
//                textContent = paragraphElement.text();
//            } else {
//                textContent = doc.text();
//            }
//            answerContent.setText(textContent);

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


            if (!base64Image.isEmpty()) {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//                Glide.with(context).asBitmap().load(decodedString).into(photoViewAnswer);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoViewAnswer.setImageBitmap(bitmap);
                photoViewAnswer.setVisibility(View.VISIBLE);

            } else {
                photoViewAnswer.setVisibility(View.GONE);
            }

            if (selectedPositions.contains(getAdapterPosition())) {
                if (answer != null && answer.getCorrected() != null && answer.getCorrected()) {
                    cardView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
                } else {
                    cardView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                }
            } else {
                cardView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
            }

            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    selectedPositions.add(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            radioButtonAnswer.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    selectedPositions.add(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}