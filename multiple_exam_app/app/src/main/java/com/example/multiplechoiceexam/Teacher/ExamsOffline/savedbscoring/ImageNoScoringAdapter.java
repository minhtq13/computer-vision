package com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.BaseUrlUtils;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;
import com.example.multiplechoiceexam.dto.studentTest.HandledImagesDeleteDTO;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImageNoScoringAdapter extends RecyclerView.Adapter<ImageNoScoringAdapter.ImageNoScoringViewHolder> {
    private final Context context;
    private final List<FileAttachDTO> fileAttachDTOS;
    private final String examClassCode;

    public ImageNoScoringAdapter(Context context, List<FileAttachDTO> fileAttachDTOS, String examClassCode) {
        this.context = context;
        this.fileAttachDTOS = fileAttachDTOS;
        this.examClassCode = examClassCode;
    }

    @NonNull
    @Override
    public ImageNoScoringAdapter.ImageNoScoringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_list_image_no_scoring, parent, false);
        return new ImageNoScoringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageNoScoringAdapter.ImageNoScoringViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FileAttachDTO fileAttachDTO = fileAttachDTOS.get(position);
        String originalFilePath = fileAttachDTO.getFilePath();
        String updatedFilePath = originalFilePath.replace(BaseUrlUtils.BaseUrl.URL_LOCAL, BaseUrlUtils.BaseUrl.URL_ORIGIN);
        Picasso.get().load(updatedFilePath).into(holder.imageViewNoScoring);
 //       Picasso.get().load(fileAttachDTO.getFilePath()).into(holder.imageViewNoScoring);

        holder.scoringDelete.setOnClickListener(view -> showDeleteConfirmationDialog(fileAttachDTO, position));
    }

    private void showDeleteConfirmationDialog(FileAttachDTO fileAttachDTO, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Gọi phương thức xóa
            deleteItem(fileAttachDTO.getId(), position,examClassCode);
        });

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            // Hủy bỏ cửa sổ nhỏ
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(Long id, int position, String examClassCode) {
        HandledImagesDeleteDTO deleteDTO = new HandledImagesDeleteDTO();
        deleteDTO.setExamClassCode(examClassCode);
        deleteDTO.getLstFileName().add(fileAttachDTOS.get(position).getFileName());

        RetrofitClient.getApiService(context.getApplicationContext())
                .deleteImagesInClassFolder(deleteDTO)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            fileAttachDTOS.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (fileAttachDTOS != null) {
            return fileAttachDTOS.size();
        } else {
            return 0;
        }
    }

    public class ImageNoScoringViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewNoScoring,scoringDelete;
        public ImageNoScoringViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewNoScoring = itemView.findViewById(R.id.imageViewNoScoring);
            scoringDelete = itemView.findViewById(R.id.scoringDelete);
        }
    }
}
