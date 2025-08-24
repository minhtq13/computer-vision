package com.example.multiplechoiceexam.Teacher.ExamManagement.chapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Teacher.ExamManagement.question.QuestionActivity;
import com.example.multiplechoiceexam.Utils.UpdateInterface;
import com.example.multiplechoiceexam.dto.chapter.ChapterRequest;
import com.example.multiplechoiceexam.dto.chapter.ChapterResponse;
import com.example.multiplechoiceexam.dto.common.ICommonIdCodeName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    Context context;
    List<ICommonIdCodeName> chapterResponseList;

    private UpdateInterface updateInterface;

    private List<ChapterResponse> selectedChapters = new ArrayList<>();
    private int selectedPosition = -1;


    public ChapterAdapter(Context context){
        this.context = context;
    }
    public ChapterAdapter(Context context, List<ICommonIdCodeName> chapterResponseList, UpdateInterface updateInterface) {
        this.context = context;
        this.chapterResponseList = chapterResponseList;
        this.updateInterface = updateInterface;
    }

    @NonNull
    @Override
    public ChapterAdapter.ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_row_item, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ChapterViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ICommonIdCodeName chapterResponse = chapterResponseList.get(position);

        holder.chapterId.setText(String.valueOf(position+1));
        holder.chapterTitle.setText(chapterResponseList.get(position).getName());
        holder.chapterOrder.setText(chapterResponseList.get(position).getCode());


        holder.itemView.setOnClickListener(view -> {
            Long subjectId = ((Activity) context).getIntent().getLongExtra("subjectId", -1);
            Long chapterId = chapterResponse.getId();
            Intent intent = new Intent(context, QuestionActivity.class);
            intent.putExtra("subjectId", subjectId);
            intent.putExtra("chapter_id", chapterId);
            context.startActivity(intent);
        });

        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(context);
        List<String> userRoles = accountSharedPreferences.getRoles();

        // If user has the TEACHER role, show update and delete buttons
        if (userRoles.contains("ROLE_STUDENT")) {
            holder.chapterUpdate.setVisibility(View.GONE);
            holder.chapterDelete.setVisibility(View.GONE);
        }
        holder.chapterUpdate.setOnClickListener(view -> updatePopupWindow(chapterResponse, view));
        holder.chapterDelete.setOnClickListener(view -> showDeleteConfirmationDialog(chapterResponse, position));
    }

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    private void updatePopupWindow(ICommonIdCodeName chapterResponse, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_popup_chapter, null);

        View overlayView = new View(context);
        overlayView.setBackgroundColor(Color.parseColor("#80000000"));
        FrameLayout.LayoutParams overlayParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        ((Activity) context).addContentView(overlayView, overlayParams);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set a TouchListener on the overlay view to dismiss the PopupWindow
        overlayView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            // Check if the overlay view is not null before removing it
            if (overlayView.getParent() != null) {
                ((ViewGroup) overlayView.getParent()).removeView(overlayView);
            }
            return true;
        });

        EditText editTextTitle = popupView.findViewById(R.id.editTextTitleChapter);
        EditText editTextOrder = popupView.findViewById(R.id.editTextOrderChapter);
        EditText editTextDescription = popupView.findViewById(R.id.editTextDescriptionUpdate);
        Button buttonSave = popupView.findViewById(R.id.buttonSave);

        String oldTitle = chapterResponse.getName();
        int dotIndex = oldTitle.indexOf('.');
        String resultPre = oldTitle.substring(0, dotIndex);
        String resultLast = oldTitle.substring(dotIndex + 1).trim();
        //Long oldOrder = chapterResponse.getId();

        editTextTitle.setText(resultLast);
        editTextOrder.setText(resultPre);
        editTextDescription.setText(oldTitle);

        buttonSave.setOnClickListener(v -> {
            String newTitle = editTextTitle.getText().toString();
            int newOrder = Integer.parseInt(editTextOrder.getText().toString());
            String newDescription = editTextDescription.getText().toString();
            Log.d("ChapterUpdateRequest", "ID: " + chapterResponse.getId() + " Title: " + newTitle + " Order: " + newOrder);
            ChapterRequest.ChapterSaveReqDTO updatedChapter = new ChapterRequest.ChapterSaveReqDTO();
            updatedChapter.setTitle(newTitle);
            updatedChapter.setOrders(newOrder);
            updatedChapter.setDescription(newDescription);

            updateChapters(chapterResponse.getId(), updatedChapter, popupWindow);

            // Remove the overlay view
            if (overlayView.getParent() != null) {
                ((ViewGroup) overlayView.getParent()).removeView(overlayView);
            }
        });

        popupWindow.setOnDismissListener(() -> {
            // Check if the overlay view is not null before removing it
            if (overlayView.getParent() != null) {
                ((ViewGroup) overlayView.getParent()).removeView(overlayView);
            }
        });

        // Hiển thị PopupWindow
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void updateChapters(Long id, ChapterRequest.ChapterSaveReqDTO updatedChapter, PopupWindow popupWindow) {
        RetrofitClient.getApiService(context.getApplicationContext()).updateChapterById(id, updatedChapter).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    notifyDataSetChanged();
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                    if (updateInterface != null) {
                        updateInterface.onUpdateComplete();
                    }
                } else {
                    Toast.makeText(context, "Cập nhật thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(context, "Lỗi call: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(ICommonIdCodeName chapterResponse, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Gọi phương thức xóa
            deleteItem(chapterResponse.getId(), position);
        });

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            // Hủy bỏ cửa sổ nhỏ
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(Long id, int position) {
        RetrofitClient.getApiService(context.getApplicationContext()).deleteChapterById(id).enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChapterResponse> call,@NotNull Response<ChapterResponse> response) {
                if (updateInterface != null) {
                    updateInterface.onUpdateComplete();
                }
                notifyDataSetChanged();
                Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<ChapterResponse> call,@NotNull Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (chapterResponseList != null) {
            return chapterResponseList.size();
        } else {
            return 0;
        }
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {

        TextView chapterTitle, chapterOrder, chapterId;
        ImageView chapterUpdate, chapterDelete;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterId = itemView.findViewById(R.id.chapter_id);
            chapterOrder = itemView.findViewById(R.id.chapter_order);
            chapterTitle = itemView.findViewById(R.id.chapter_title);
            chapterUpdate = itemView.findViewById(R.id.chapter_edit);
            chapterDelete = itemView.findViewById(R.id.chapter_delete);
        }
    }
}
