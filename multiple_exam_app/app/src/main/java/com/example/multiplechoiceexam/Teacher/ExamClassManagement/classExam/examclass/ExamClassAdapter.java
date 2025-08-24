package com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclass;

import static com.example.multiplechoiceexam.Utils.DateUtils.generateRandomDateTime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclassdetail.ExamClassDetailActivity;
import com.example.multiplechoiceexam.dto.examClass.ExamClass;
import com.example.multiplechoiceexam.dto.examClass.UserExamClassRoleEnum;
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamClassAdapter extends RecyclerView.Adapter<ExamClassAdapter.ExamClassViewHolder> {

    Context context;

    private final List<ExamClass> classResponseList;

    public ExamClassAdapter(Context context, List<ExamClass> classResponseList) {
        this.context = context;
        this.classResponseList = classResponseList;
    }

    @NonNull
    @Override
    public ExamClassAdapter.ExamClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_item, parent, false);
        return new ExamClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamClassAdapter.ExamClassViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ExamClass examClass = classResponseList.get(position);

        holder.classId.setText(String.valueOf(position+1));
        holder.className.setText(classResponseList.get(position).getRoomName());
        holder.classSemester.setText(classResponseList.get(position).getSemester());
        holder.classCode.setText(classResponseList.get(position).getCode());
        holder.classSubject.setText(classResponseList.get(position).getSubjectTitle());
        holder.classDate.setText(classResponseList.get(position).getExamineDate());
        holder.classTime.setText(classResponseList.get(position).getExamineTime());
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(context);
        List<String> userRoles = accountSharedPreferences.getRoles();

        if (userRoles.contains("ROLE_STUDENT")) {
            holder.disableClass.setVisibility(View.GONE);
            holder.classDownload.setVisibility(View.GONE);
            holder.classUpdate.setVisibility(View.GONE);
            holder.classDownloads.setVisibility(View.GONE);
        } else {
            holder.disableClass.setVisibility(View.VISIBLE);
            holder.classDownload.setVisibility(View.VISIBLE);
            holder.classUpdate.setVisibility(View.VISIBLE);

            holder.disableClass.setOnClickListener(view -> showDeleteConfirmationDialog(examClass, position));
            holder.classDownload.setOnClickListener(v -> {
                // Hiển thị dialog xác nhận trước khi tải xuống tập tin
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn tải xuống tập tin không?");
                builder.setPositiveButton("Có", (dialogInterface, i) -> downloadFileWithConfirmation(examClass.getId(),examClass.getCode()));
                builder.setNegativeButton("Không", null);
                builder.show();
            });

        }
        holder.classUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(context, ExamClassUpdateActivity.class);
            intent.putExtra("exam_classId", examClass.getId());
            intent.putExtra("exam_class_name", examClass.getRoomName());
            intent.putExtra("exam_time", examClass.getExamineTime() + " " +examClass.getExamineDate());
            intent.putExtra("exam_test", examClass.getTestId());
            context.startActivity(intent);
        });

//        holder.disableClass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDeleteConfirmationDialog(examClass, position);
//            }
//        });
        holder.itemView.setOnClickListener(view -> {
            Long id = classResponseList.get(position).getId();
            Intent intent = new Intent(context, ExamClassDetailActivity.class);
            intent.putExtra("examClassCode", classResponseList.get(position).getCode());
            intent.putExtra("id", id);
            context.startActivity(intent);
        });

    }

    private void showDeleteConfirmationDialog(ExamClass examClass, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> deleteItem(examClass.getId(), position));

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(Long id, int position) {
        RetrofitClient.getApiService(context.getApplicationContext()).disableClassRoomExam(id).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMessage> call, @NonNull Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseMessage> call, @NonNull Throwable t) {
                Toast.makeText(context, "lỗi gọi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadFileWithConfirmation(Long id, String classCode) {
        ApiService apiService = RetrofitClient.getApiService(context.getApplicationContext());
        Call<ResponseBody> call = apiService.exportStudentTestExcel(id, UserExamClassRoleEnum.STUDENT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String randomDateTime = generateRandomDateTime();
                        downloadFile(body, "StudentTest_" + classCode +"_"+ randomDateTime+ ".xlsx");
                    } else {
                        Toast.makeText(context, "Phản hồi từ server rỗng.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gọi API không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Lỗi tải xuống", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void downloadFile(ResponseBody body, String filename) {
        if (isExternalStorageWritable()) {
            try {
                File externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(externalStorageDir, filename);
                FileOutputStream fos = new FileOutputStream(file);
                InputStream inputStream = body.byteStream();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                fos.flush();
                fos.close();
                inputStream.close();

                Toast.makeText(context, "Tải xuống hoàn tất. Tập tin được lưu trong thư mục Downloads.", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                Toast.makeText(context, "Tải xuống thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Không thể truy cập bộ nhớ ngoài.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    @Override
    public int getItemCount() {
        if (classResponseList != null) {
            return classResponseList.size();
        } else {
            return 0;
        }
    }

    public class ExamClassViewHolder extends RecyclerView.ViewHolder {

        TextView classId, className, classSemester, classCode, classDownload,classSubject, classUpdate,
                classTime, classDate;
        ImageView disableClass, classDownloads;

        public ExamClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classId = itemView.findViewById(R.id.class_id);
            className = itemView.findViewById(R.id.class_name);
            classSemester = itemView.findViewById(R.id.class_semester);
            classCode = itemView.findViewById(R.id.class_code);
            classDownload = itemView.findViewById(R.id.class_text_download);
            classDownloads = itemView.findViewById(R.id.class_download);
            disableClass = itemView.findViewById(R.id.disable_class_room);
            classTime = itemView.findViewById(R.id.class_time);
            classDate = itemView.findViewById(R.id.class_date);
            classSubject = itemView.findViewById(R.id.class_subject);
            classUpdate = itemView.findViewById(R.id.class_update);
        }
    }
}
