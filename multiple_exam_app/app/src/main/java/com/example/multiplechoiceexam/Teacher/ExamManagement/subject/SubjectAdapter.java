package com.example.multiplechoiceexam.Teacher.ExamManagement.subject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.SharedPreferences.SubjectSharedPreferences;
import com.example.multiplechoiceexam.Teacher.ExamManagement.chapter.ChapterActivity;
import com.example.multiplechoiceexam.dto.subject.SubjectCode;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    Context context;
    List<SubjectResponse.SubjectItem> subjectResponseList;
    List<SubjectCode> subjectList = new ArrayList<>();
    private final SubjectSharedPreferences subjectSharedPreferences;

    public SubjectAdapter(Context context, List<SubjectResponse.SubjectItem> subjectResponseList) {
        this.context = context;
        this.subjectResponseList = subjectResponseList;
        subjectSharedPreferences = new SubjectSharedPreferences(context);

    }
    @NonNull
    @Override
    public SubjectAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_row_item ,parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.SubjectViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SubjectResponse.SubjectItem subjectResponse = subjectResponseList.get(position);
        Long id = subjectResponse.getId();
        String code = subjectResponseList.get(position).getCode();
        String title = subjectResponseList.get(position).getTitle();
        holder.subjectCode.setText(code);
        holder.subjectTitle.setText(title);
        holder.subjectCredit.setText(String.valueOf(subjectResponseList.get(position).getCredit()));
//        holder.subjectChapter.setText(subjectResponseList.get(position).getDepartmentName());

        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(context);
        List<String> userRoles = accountSharedPreferences.getRoles();

        // Check if the "STUDENT" role is present
        if (userRoles.contains("ROLE_STUDENT")) {
            holder.imageDelete.setVisibility(View.GONE);
            holder.imageUpdate.setVisibility(View.GONE);
        } else {
            holder.imageDelete.setVisibility(View.GONE);

            holder.imageDelete.setOnClickListener(view -> showDeleteConfirmationDialog(subjectResponse, position));
        }

        holder.imageUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(context, SubjectUpdateActivity.class);
            intent.putExtra("subjectId", subjectResponse.getId());
            intent.putExtra("subjectTitle", subjectResponse.getTitle());
            intent.putExtra("subjectCode", subjectResponse.getCode());
            intent.putExtra("subjectCredit", subjectResponse.getCredit());
            intent.putExtra("subjectDepartment", subjectResponse.getDepartmentName());
            context.startActivity(intent);
        });
        holder.itemView.setOnClickListener(view -> {
        //    String code1 = subjectResponseList.get(position).getCode();
            Intent intent = new Intent(context, ChapterActivity.class);
            intent.putExtra("subjectId", id);
            context.startActivity(intent);
        });
        subjectList.add(new SubjectCode(id, title));
        subjectSharedPreferences.saveSubjectList(subjectList);
    }

    private void showDeleteConfirmationDialog(SubjectResponse.SubjectItem subjectResponse, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> deleteItem(subjectResponse.getId(), position));

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(Long id, int position) {
        RetrofitClient.getApiService(context.getApplicationContext()).deleteSubjectById(id).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseMessage> call,@NotNull Throwable t) {
                Toast.makeText(context, "lỗi gọi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        if (subjectResponseList != null) {
            return subjectResponseList.size();
        } else {
            return 0;
        }
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {

        TextView subjectCode,subjectTitle, subjectCredit;
        ImageView imageDelete, imageUpdate;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectCode = itemView.findViewById(R.id.subject_code);
            subjectTitle=itemView.findViewById(R.id.subject_title);
            subjectCredit=itemView.findViewById(R.id.subject_credit);
            imageDelete = itemView.findViewById(R.id.subject_delete);
            imageUpdate = itemView.findViewById(R.id.subject_update);

        }
    }
}
