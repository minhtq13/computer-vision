package com.example.multiplechoiceexam.Teacher.ExamsOffline.imagescoring;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Utils.BaseUrlUtils;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;
import com.example.multiplechoiceexam.dto.studentTest.HandledImagesDeleteDTO;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<Uri> list;
    private String examClassCode;
    private List<FileAttachDTO> fileAttachDTOS;
    Context context;

    public void clearImages() {
        this.list.clear();
        notifyDataSetChanged();
    }
    public ImageAdapter(ArrayList<Uri> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setImages(ArrayList<Uri> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void setExamClassCode(String examClassCode) {
        this.examClassCode =examClassCode;
        notifyDataSetChanged();
    }
    public void setFileAttachDTOS(List<FileAttachDTO> fileAttachDTOS) {
        this.fileAttachDTOS = fileAttachDTOS;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUrl = list.get(position);


        Picasso.get()
                .load(imageUrl)
                .into(holder.image);
        holder.image.setOnClickListener(view -> showImageDialog(imageUrl, holder.image.getContext()));
        holder.imageDelete.setOnClickListener(view -> showDeleteConfirmationDialog(position));
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Gọi phương thức xóa
            deleteItem(position,examClassCode);
        });

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            // Hủy bỏ cửa sổ nhỏ
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(int position, String examClassCode) {
        if (position != RecyclerView.NO_POSITION) {
            Uri imageUrl = list.get(position);

            // Kiểm tra xem Uri có thuộc danh sách FileAttachDTO không
            if (isImageInFileAttachDTOS(imageUrl)) {
                // Lấy fileName từ fileAttachDTOS
                String fileName = getFileNameFromUri(imageUrl);
                // Gọi API để xóa ảnh từ thư mục lớp
                deleteImageFromServer(fileName,position);
            }else {
                list.remove(position);
                notifyItemRemoved(position);
            }
        }
    }
    private String getFileNameFromUri(Uri imageUrl) {
        if (fileAttachDTOS != null && !fileAttachDTOS.isEmpty()) {
            for (FileAttachDTO fileAttachDTO : fileAttachDTOS) {
                Uri uriFromDto = Uri.parse(changeBaseUrl(fileAttachDTO.getFilePath()));
                if (uriFromDto.equals(imageUrl)) {
                    return fileAttachDTO.getFileName();
                }
            }
        }
        return null;
    }
    private void deleteImageFromServer(String fileName,int position) {
        HandledImagesDeleteDTO deleteDTO = new HandledImagesDeleteDTO();
        deleteDTO.setExamClassCode(examClassCode);
        deleteDTO.getLstFileName().add(fileName);

        ApiService apiService = RetrofitClient.getApiService(context.getApplicationContext());
        Call<Void> call = apiService.deleteImagesInClassFolder(deleteDTO);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    if (fileAttachDTOS != null && position < fileAttachDTOS.size()) {
                        fileAttachDTOS.remove(position);
                    }
                    list.remove(position);
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


    private boolean isImageInFileAttachDTOS(Uri imageUri) {
        if (fileAttachDTOS != null && !fileAttachDTOS.isEmpty()) {
            for (FileAttachDTO fileAttachDTO : fileAttachDTOS) {
                Uri uriFromDto = Uri.parse(changeBaseUrl(fileAttachDTO.getFilePath()));
                if (uriFromDto.equals(imageUri)) {
                    return true;
                }
            }
        }
        return false;
    }
    private String changeBaseUrl(String originalUrl) {
        return originalUrl.replace(BaseUrlUtils.BaseUrl.URL_LOCAL, BaseUrlUtils.BaseUrl.URL_ORIGIN);
    }

    private void showImageDialog(Uri imageUrl, Context context) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        PhotoView dialogImageView = dialog.findViewById(R.id.dialogImageView);

        // Load the image using Picasso
        Picasso.get().load(imageUrl).into(dialogImageView);

        dialogImageView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView image, imageDelete;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_imageview);
            imageDelete = itemView.findViewById(R.id.imageDelete);
        }
    }
}
