package com.example.multiplechoiceexam.Teacher.ExamsOffline.teacher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.auth.UserTypeEnum;
import com.example.multiplechoiceexam.dto.teacher.TeacherResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    private final List<TeacherResponse> teacherResponses;
    private ItemTouchHelper itemTouchHelper;
    private final Context context;

    public TeacherAdapter(List<TeacherResponse> teacherResponses, Context context) {
        this.teacherResponses = teacherResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherAdapter.TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_teacher_row_item ,parent, false);
        return new TeacherViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.TeacherViewHolder holder, int position) {
        TeacherResponse teacherResponse = teacherResponses.get(position);

        holder.teacherName.setText(teacherResponse.getLastName() + " " +teacherResponse.getFirstName());
        holder.teacherMSSV.setText(teacherResponse.getCode());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, TeacherProfileActivity.class);
            intent.putExtra("teacherId", teacherResponse.getId());
            intent.putExtra("teacher_name_1", teacherResponse.getLastName() + " " +teacherResponse.getFirstName());
            intent.putExtra("teacher_birth_1", teacherResponse.getBirthDate());
            intent.putExtra("teacher_email_1", teacherResponse.getEmail());
            intent.putExtra("teacher_gender_1", teacherResponse.getGender());
            intent.putExtra("teacher_phone_1", teacherResponse.getPhoneNumber());
            intent.putExtra("teacher_code_1", teacherResponse.getCode());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (teacherResponses != null) {
            return teacherResponses.size();
        } else {
            return 0;
        }
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder {

        ImageView imageTeacher ,imageDeleteTeacher, imageEditTeach;

        TextView teacherName, teacherMSSV;
        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacher_name_item);
            teacherMSSV = itemView.findViewById(R.id.teacher_mssv);
            imageDeleteTeacher = itemView.findViewById(R.id.imageViewDeleteTeacher);
            imageEditTeach = itemView.findViewById(R.id.imageViewUpdateTeacher);
        }
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper.Callback callback = new SwipeToTeacherCallback(this, recyclerView);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public void deleteItemTeacher(int position) {
        TeacherResponse teacherResponse = teacherResponses.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> deleteItemTeacher(teacherResponse.getId(), position));

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        notifyItemRemoved(position);
    }

    private void deleteItemTeacher(Long id, int position) {
        RetrofitClient.getApiService(context.getApplicationContext()).disableTeacherById(UserTypeEnum.TEACHER,id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Toast.makeText(context, "lỗi gọi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateItemTeacher(int position) {
        TeacherResponse teacherResponse = teacherResponses.get(position);

        Intent intent = new Intent(context, TeacherUpdateActivity.class);
        intent.putExtra("teacher_id",teacherResponse.getId());
        intent.putExtra("teacher_name_first", teacherResponse.getFirstName());
        intent.putExtra( "teacher_name_last" ,teacherResponse.getLastName());
        intent.putExtra("teacher_birth", teacherResponse.getBirthDate());
        intent.putExtra("teacher_email", teacherResponse.getEmail());
        intent.putExtra("teacher_code",teacherResponse.getCode());
        intent.putExtra("userType",teacherResponse.getUserType());
        intent.putExtra("departmentId", teacherResponse.getDepartmentId());
        intent.putExtra("teacher_gender", teacherResponse.getGender());
        intent.putExtra("teacher_phone", teacherResponse.getPhoneNumber());

        context.startActivity(intent);
        notifyItemChanged(position);
    }

    public void showIcons(TeacherAdapter.TeacherViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            viewHolder.imageDeleteTeacher.setVisibility(View.VISIBLE);
            viewHolder.imageEditTeach.setVisibility(View.INVISIBLE);
        } else if (direction == ItemTouchHelper.RIGHT) {
            viewHolder.imageDeleteTeacher.setVisibility(View.INVISIBLE);
            viewHolder.imageEditTeach.setVisibility(View.VISIBLE);
        }
    }


    public void hideIcons(TeacherAdapter.TeacherViewHolder viewHolder) {
        viewHolder.imageDeleteTeacher.setVisibility(View.INVISIBLE);
        viewHolder.imageEditTeach.setVisibility(View.INVISIBLE);
    }
    public void onChildDraw(
            Canvas c,
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Hiển thị và ẩn icon tùy thuộc vào hướng vuốt
            int direction = dX > 0 ? ItemTouchHelper.RIGHT : ItemTouchHelper.LEFT;
            TeacherAdapter.TeacherViewHolder holder = (TeacherAdapter.TeacherViewHolder) viewHolder;
            showIcons(holder, direction);
        }
    }


}
