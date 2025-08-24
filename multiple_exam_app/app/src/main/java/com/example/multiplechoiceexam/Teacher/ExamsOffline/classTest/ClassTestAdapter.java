package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;

import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Utils.DateUtils;
import com.example.multiplechoiceexam.Utils.UpdateInterface;
import com.example.multiplechoiceexam.dto.test.TestClassResponse;
import com.example.multiplechoiceexam.dto.test.TestSetGenerateReqDTO;
import com.example.multiplechoiceexam.dto.test.TestSetPreviewDTO;
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassTestAdapter extends RecyclerView.Adapter<ClassTestAdapter.ClassTestViewHolder> {

    Context context;
    private final List<TestClassResponse.TestItem> testResponses;

    private final UpdateInterface updateInterface;

    private int selectedPosition = -1;

    public ClassTestAdapter(Context context, List<TestClassResponse.TestItem> testResponses, UpdateInterface updateInterface) {
        this.context = context;
        this.testResponses = testResponses;
        this.updateInterface = updateInterface;
    }

    @NonNull
    @Override
    public ClassTestAdapter.ClassTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_class_test_item, parent, false);
        return new ClassTestViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClassTestAdapter.ClassTestViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TestClassResponse.TestItem testClassResponse = testResponses.get(position);
        holder.className.setText(testClassResponse.getName());
        holder.classSemester.setText(testClassResponse.getSemester());
        holder.classTestSubject.setText(testClassResponse.getSubjectName());
        holder.classTestDate.setText(DateUtils.formatDateTime(testClassResponse.getCreatedAt()));
        holder.classTestTime.setText(DateUtils.formatDateTime(testClassResponse.getModifiedAt()));
        holder.classTestTime1.setText(Integer.toString(testClassResponse.getDuration()));
        holder.classTestTotalPoint.setText(Integer.toString(testClassResponse.getQuestionQuantity()));

        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(context);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if (userRoles.contains("ROLE_STUDENT")) {
            // If the user is a student, hide the buttons
            holder.testCreate.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
        boolean isExpand = testResponses.get(position).isExpanded();
        holder.constraintLayout.setVisibility(isExpand ? View.VISIBLE : View.GONE);
        holder.testView.setOnClickListener(view -> {
 //           List<String> testSetNos = new ArrayList<>();

            Integer testId = testClassResponse.getId();

            String[] lstTestSetCodeArray = testClassResponse.getLstTestSetCodeArray();
            Intent intent = new Intent(context, ClassTestExamListActivity.class);
            intent.putExtra(("testId"), testId);
            intent.putExtra("lstTestSetCodeArray", lstTestSetCodeArray);
            intent.putExtra(("testCode"), testClassResponse.getSubjectName());
            context.startActivity(intent);
        });

        holder.testCreate.setOnClickListener(view -> {
            selectedPosition = position;
            showPopupClassExamCreate(testClassResponse, view);
        });

        holder.btnDelete.setOnClickListener(view -> showDeleteConfirmationDialog(testClassResponse, position));
    }

        private void showDeleteConfirmationDialog(TestClassResponse.TestItem testClassResponse, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> deleteItem(testClassResponse.getId(), position));

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem(Integer id, int position) {
        RetrofitClient.getApiService(context.getApplicationContext()).disableTestById(id).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMessage> call,@NotNull Response<ResponseMessage> response) {
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

    private void showPopupClassExamCreate(TestClassResponse.TestItem testClassResponse, View view) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_popup_class_test_add);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        int height = context.getResources().getDisplayMetrics().heightPixels;

        TranslateAnimation slideUp = new TranslateAnimation(0, 0, height, 0);
        slideUp.setDuration(500);
        view.startAnimation(slideUp);
//        //     backgroundView.setVisibility(View.VISIBLE);

//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                backgroundView.setVisibility(View.GONE);
//            }
//        });

        EditText editNumber = dialog.findViewById(R.id.edit_class_exam_add);
        Button buttonSave = dialog.findViewById(R.id.btnSaveClassTest);

        buttonSave.setOnClickListener(v -> {
            Integer editNum = Integer.valueOf(editNumber.getText().toString());
            addClassTestExam(testClassResponse.getId(), editNum, dialog);
        });

        dialog.show();

    }

    private void addClassTestExam(Integer id, Integer editNum, Dialog dialog) {
        if (selectedPosition != -1) {
            if (selectedPosition >= 0 && selectedPosition < testResponses.size()) {
                TestSetGenerateReqDTO testSetGenerateReqDTO = new TestSetGenerateReqDTO();
                testSetGenerateReqDTO.setTestId(Long.valueOf(id));
                testSetGenerateReqDTO.setNumOfTestSet(editNum);
                RetrofitClient.getApiService(context.getApplicationContext()).createTestSetFromTest(testSetGenerateReqDTO).enqueue(new Callback<List<TestSetPreviewDTO>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<TestSetPreviewDTO>> call, @NotNull Response<List<TestSetPreviewDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Xử lý kết quả ở đây
                            notifyDataSetChanged();
                            Toast.makeText(context, "thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (updateInterface != null) {
                                updateInterface.onUpdateComplete();
                            }
                        } else {
                            Toast.makeText(context, "Thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<TestSetPreviewDTO>> call,@NotNull Throwable t) {
                        Toast.makeText(context, "lỗi call", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            selectedPosition = -1;
        }
    }

    @Override
    public int getItemCount() {
        if (testResponses != null) {
            return testResponses.size();
        } else {
            return 0;
        }
    }

    public class ClassTestViewHolder extends RecyclerView.ViewHolder {

        TextView classTestSubject, classTestDate, classTestTime, classTestTime1, classTestTotalPoint, className, classSemester;
        ConstraintLayout headerFrame;
        ConstraintLayout constraintLayout;
        Button testView, testCreate;
        ImageView btnDelete;

        public ClassTestViewHolder(@NonNull View itemView) {
            super(itemView);
            classTestSubject = itemView.findViewById(R.id.test_subject);
            className = itemView.findViewById(R.id.test_name);
            classSemester = itemView.findViewById(R.id.test_semester);
            classTestDate = itemView.findViewById(R.id.test_time_start);
            classTestTime = itemView.findViewById(R.id.test_time_end);
            classTestTime1 = itemView.findViewById(R.id.test_time_1);
            classTestTotalPoint = itemView.findViewById(R.id.test_total_point);
            headerFrame = itemView.findViewById(R.id.headerFrame);
            constraintLayout = itemView.findViewById(R.id.expandableLayout);
            testView = itemView.findViewById(R.id.test_view);
            testCreate = itemView.findViewById(R.id.test_create);
            btnDelete = itemView.findViewById(R.id.test_delete);

            headerFrame.setOnClickListener(view -> {
                TestClassResponse.TestItem testClassResponse = testResponses.get(getAdapterPosition());
                testClassResponse.setExpanded(!testClassResponse.isExpanded());
                notifyDataSetChanged();
            });
        }
    }
}