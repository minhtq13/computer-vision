package com.example.multiplechoiceexam.Teacher.ExamsOffline.student;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.multiplechoiceexam.dto.student.StudentResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final Context context;
    private final List<StudentResponse> studentResponseList;
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    public StudentAdapter(Context context, List<StudentResponse> studentResponseList) {
        this.context = context;
        this.studentResponseList = studentResponseList;
    }
    
    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_student_row_item ,parent, false);
        return new StudentAdapter.StudentViewHolder(view);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.StudentViewHolder holder, int position) {

        StudentResponse studentResponse = studentResponseList.get(position);
        holder.studentName.setText(studentResponse.getLastName() + " " + studentResponse.getFirstName());
        holder.studentMSSV.setText(studentResponse.getCode());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentProfileActivity.class);
                intent.putExtra("studentId", studentResponse.getId());
                intent.putExtra("student_name", studentResponse.getLastName() +" "+ studentResponse.getFirstName());
                intent.putExtra("student_birth", studentResponse.getBirthDate());
                intent.putExtra("student_course", studentResponse.getCourseNum());
                intent.putExtra("student_email", studentResponse.getEmail());
                intent.putExtra("student_gender", studentResponse.getGender());
                intent.putExtra("student_phone", studentResponse.getPhoneNumber());
                intent.putExtra("student_code", studentResponse.getCode());
                context.startActivity(intent);
            }
        });

        hideIcons(holder);

        holder.itemView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int direction = event.getX() > v.getWidth() / 2 ? ItemTouchHelper.RIGHT : ItemTouchHelper.LEFT;
                //showIcons(holder, direction);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        if (studentResponseList != null) {
            return studentResponseList.size();
        } else {
            return 0;
        }
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageView imageStudent, imageDelete,imageEdit;

        TextView studentName, studentMSSV;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name_item);
            studentMSSV = itemView.findViewById(R.id.student_mssv);
            imageDelete = itemView.findViewById(R.id.imageViewDeleteStudent);
            imageEdit = itemView.findViewById(R.id.imageViewUpdateStudent);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper.Callback callback = new SwipeToStudentCallback(this, recyclerView);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public void deleteItem(int position) {
        StudentResponse studentResponse = studentResponseList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> deleteItemStudent(studentResponse.getId(), position));

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        notifyItemRemoved(position);
    }

    private void deleteItemStudent(Long id, int position) {
        RetrofitClient.getApiService(context.getApplicationContext()).disableStudentById(UserTypeEnum.STUDENT,id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                Toast.makeText(context, "lỗi gọi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateItem(int position) {
        StudentResponse studentResponse = studentResponseList.get(position);

        Intent intent = new Intent(context, StudentUpdateActivity.class);
        intent.putExtra("student_id",studentResponse.getId());
        intent.putExtra("student_name_first", studentResponse.getFirstName());
        intent.putExtra("student_name_last", studentResponse.getLastName());
        intent.putExtra("student_birth", studentResponse.getBirthDate());
        intent.putExtra("student_code", studentResponse.getCode());
        intent.putExtra("student_email", studentResponse.getEmail());
        intent.putExtra("departmentId",studentResponse.getDepartmentId());
        intent.putExtra("student_gender", studentResponse.getGender());
        intent.putExtra("student_phone", studentResponse.getPhoneNumber());

        context.startActivity(intent);
        notifyItemChanged(position);
    }

    public void showIcons(StudentViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            viewHolder.imageDelete.setVisibility(View.VISIBLE);
            viewHolder.imageEdit.setVisibility(View.INVISIBLE);
        } else if (direction == ItemTouchHelper.RIGHT) {
            viewHolder.imageDelete.setVisibility(View.INVISIBLE);
            viewHolder.imageEdit.setVisibility(View.VISIBLE);
        }
    }


    public void hideIcons(StudentViewHolder viewHolder) {
        viewHolder.imageDelete.setVisibility(View.INVISIBLE);
        viewHolder.imageEdit.setVisibility(View.INVISIBLE);
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
            int direction = dX > 0 ? ItemTouchHelper.RIGHT : ItemTouchHelper.LEFT;
            StudentViewHolder holder = (StudentViewHolder) viewHolder;
            showIcons(holder, direction);
        }
    }


}

