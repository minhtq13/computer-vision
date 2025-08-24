package com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest.SemesterAdapter;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest.SubjectComboBoxAdapter;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring.RecyclerViewItemDecoration;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.savedbscoring.StudentSaveScoringDBActivity;
import com.example.multiplechoiceexam.Utils.ValidateInputData;
import com.example.multiplechoiceexam.dto.examClass.ClassRequest;
import com.example.multiplechoiceexam.dto.student.StudentResponse;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;
import com.example.multiplechoiceexam.dto.teacher.TeacherResponse;
import com.example.multiplechoiceexam.dto.test.TestClassResponse;
import com.example.multiplechoiceexam.dto.test.semeter.SemesterBox;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamClassAddActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NEW_ACTIVITY = 123;
    private static final int REQUEST_CODE_NEW_ACTIVITY_TEACHER = 321;
    private TextInputEditText editClassRoom, editTextTeacher, editClassHk, editClassTestCodeId,editTextStudent,
            editExamineTime, editSemester,editSubject;
    private Button btnClassExamSave;
    private ProgressDialog progressDialog;
    private List<Long> selectedStudents, selectedTeachers;
    private String formattedTime,date;
    private Long testId, subjectId,semesterId;
    private ApiService apiService;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_class_add);

        List<TestClassResponse> testClassResponseList = new ArrayList<>();
        selectedStudents = new ArrayList<>();
        apiService = RetrofitClient.getApiService(getApplicationContext());
        findViewById(R.id.imageView_examClassAdd_back).setOnClickListener(v ->
            finish());

        anhXa();
        editExamineTime.setOnClickListener(v -> showTimePickerDialog());
        btnClassExamSave.setOnClickListener(view -> saveClassExam());
        editClassTestCodeId.setOnClickListener(view -> callApiAndGetItemTest());
        editTextStudent.setOnClickListener(view -> callApiAndGetStudents());
        editTextTeacher.setOnClickListener(view -> callApiAndGetTeachers());
        editSubject.setOnClickListener(view -> callApiSubject());
        editSemester.setOnClickListener(view -> callApiSemester());
    }

    private void callApiSemester() {
        Call<List<SemesterBox>> call = RetrofitClient.getApiService(getApplicationContext()).getListSemester("");
        call.enqueue(new Callback<List<SemesterBox>>() {
            @Override
            public void onResponse(@NotNull Call<List<SemesterBox>> call,@NotNull Response<List<SemesterBox>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SemesterBox> semesterBoxes = response.body();
                    if (semesterBoxes != null && !semesterBoxes.isEmpty()) {
                        Dialog dialog = new Dialog(ExamClassAddActivity.this);
                        dialog.setContentView(R.layout.dialog_semester_list);
                        Window window = dialog.getWindow();
                        if (window != null) {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int dialogWidth = (int) (displayMetrics.widthPixels * 0.8);
                            window.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                        TextView textViewDialogTitle = dialog.findViewById(R.id.textViewDialogTitle);
                        textViewDialogTitle.setText("Học kì");

                        RecyclerView recyclerViewSemesters = dialog.findViewById(R.id.recyclerViewSemesters);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ExamClassAddActivity.this);
                        recyclerViewSemesters.setLayoutManager(layoutManager);
                        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
                        recyclerViewSemesters.addItemDecoration(new RecyclerViewItemDecoration(ExamClassAddActivity.this, dividerHeight));

                        SemesterAdapter adapter = new SemesterAdapter(semesterBoxes,ExamClassAddActivity.this);
                        recyclerViewSemesters.setAdapter(adapter);
                        adapter.setOnItemClickListener(semesterLists -> {
                            editSemester.setText(semesterLists.getName());
                            semesterId = semesterLists.getId();
                            dialog.dismiss();
                        });
                        ImageButton buttonCloseDialog = dialog.findViewById(R.id.buttonCloseDialog);
                        buttonCloseDialog.setOnClickListener(v -> dialog.dismiss());

                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<SemesterBox>> call,@NotNull Throwable t) {

            }
        });
    }

    private void callApiSubject() {
        Call<SubjectResponse> call = RetrofitClient.getApiService(getApplicationContext()).getListSubject(
                "",
                -1L, // departmentId
                "",
                0, // page
                10000, // size
                "code" // sort
        );

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(@NotNull Call<SubjectResponse> call, @NotNull Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    SubjectResponse subjectListResponse = response.body();
                    List<SubjectResponse.SubjectItem> subjects = subjectListResponse.getContent();
                    showSubjectSelectionDialog(subjects);
                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(@NotNull Call<SubjectResponse> call,@NotNull Throwable t) {
                // Xử lý thất bại
            }
        });
    }

    private void showSubjectSelectionDialog(List<SubjectResponse.SubjectItem> subjects) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamClassAddActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ExamClassAddActivity.this);

        View dialogView = inflater.inflate(R.layout.custom_subject_selection_dialog, null);
        builder.setView(dialogView);

        RecyclerView recyclerViewSubjects = dialogView.findViewById(R.id.recyclerViewSubjects);
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose);

        recyclerViewSubjects.setLayoutManager(new LinearLayoutManager(ExamClassAddActivity.this));
        SubjectComboBoxAdapter subjectAdapter = new SubjectComboBoxAdapter(ExamClassAddActivity.this, subjects);
        recyclerViewSubjects.setAdapter(subjectAdapter);
        int dividerHeight = getResources().getDimensionPixelSize(R.dimen.divider_height);
        recyclerViewSubjects.addItemDecoration(new RecyclerViewItemDecoration(ExamClassAddActivity.this, dividerHeight));
        AlertDialog dialog = builder.create();
        subjectAdapter.setOnItemClickListener(subjectItem -> {
            editSubject.setText(subjectItem.getTitle());
            subjectId = subjectItem.getId();
            dialog.dismiss();
        });

        dialog.show();

        btnClose.setOnClickListener(view -> dialog.dismiss());
    }
    private void callApiAndGetTeachers() {
        apiService.getListTeacher("").enqueue(new Callback<List<TeacherResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<TeacherResponse>> call, @NotNull Response<List<TeacherResponse>> response) {
                if (response.isSuccessful()) {
                    List<TeacherResponse> teacherResponses = response.body();
                    showTeacherSelectionDialog(teacherResponses);
                } else {
                    Toast.makeText(ExamClassAddActivity.this, "Lấy danh sách giảng viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TeacherResponse>> call,@NotNull Throwable t) {
                Toast.makeText(ExamClassAddActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTeacherSelectionDialog(List<TeacherResponse> teacherResponses) {
        Intent intent = new Intent(ExamClassAddActivity.this, StudentTestClassExamActivity.class);
        intent.putParcelableArrayListExtra("teacher_list", new ArrayList<>(teacherResponses));
        startActivityForResult(intent, REQUEST_CODE_NEW_ACTIVITY_TEACHER);
    }

//    private void showTimePickerDialog() {
//        Calendar calendar = Calendar.getInstance();
//        final int year = calendar.get(Calendar.YEAR);
//        final int month = calendar.get(Calendar.MONTH);
//        final int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(ExamClassAddActivity.this, (datePicker, year1, month1, day1) -> {
//            int formattedMonth = month1 + 1;
//
//            TimePickerDialog timePickerDialog = new TimePickerDialog(ExamClassAddActivity.this, (timePicker, hourOfDay, minute) -> {
//                formattedTime = String.format("%02d:%02d", hourOfDay, minute);
//                date = String.format("%s %02d/%02d/%d", formattedTime, day1, formattedMonth, year1);
//
//                editExamineTime.setText(date);
//            }, 0, 0, true);
//            timePickerDialog.show();
//        }, year, month, day);
//
//        datePickerDialog.show();
//    }
private void showTimePickerDialog() {
    Calendar calendar = Calendar.getInstance();
    final int currentYear = calendar.get(Calendar.YEAR);
    final int currentMonth = calendar.get(Calendar.MONTH);
    final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog datePickerDialog = new DatePickerDialog(ExamClassAddActivity.this, (datePicker, year1, month1, day1) -> {
        if (year1 > currentYear || (year1 == currentYear && month1 > currentMonth) || (year1 == currentYear && month1 == currentMonth && day1 >= currentDay)) {
            int formattedMonth = month1 + 1;

            TimePickerDialog timePickerDialog = new TimePickerDialog(ExamClassAddActivity.this, (timePicker, hourOfDay, minute) -> {
                formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                date = String.format("%s %02d/%02d/%d", formattedTime, day1, formattedMonth, year1);

                editExamineTime.setText(date);
            }, 0, 0, true);
            timePickerDialog.show();
        } else {
            editExamineTime.setText("");
            Toast.makeText(ExamClassAddActivity.this, "Không thể chọn ngày", Toast.LENGTH_SHORT).show();
        }
    }, currentYear, currentMonth, currentDay);

    datePickerDialog.show();
}


    private void saveClassExam() {
        ClassRequest classRequest = new ClassRequest();
        String roomName = Objects.requireNonNull(editClassRoom.getText()).toString().trim();

        if(!ValidateInputData.stringNotNull(roomName, editClassRoom)){
            return;
        }

        String code = Objects.requireNonNull(editClassHk.getText()).toString().trim();
        if(!ValidateInputData.stringNotNull(code, editClassHk)){
            return;
        }
        classRequest.setRoomName(roomName);
        classRequest.setCode(code);
        classRequest.setExamineTime(date);
        classRequest.setTestId(testId);
        classRequest.setLstSupervisorId(selectedTeachers);
        classRequest.setLstStudentId(selectedStudents);

        apiService.createClass(classRequest).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(@NotNull Call<Long> call,@NotNull Response<Long> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ExamClassAddActivity.this, "Tạo phòng thi thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ExamClassAddActivity.this, "Tạo phòng thi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Long> call,@NotNull Throwable t) {
                     Toast.makeText(ExamClassAddActivity.this, "Lỗi call", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NEW_ACTIVITY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                    ArrayList<Integer> selectedStudentIdsInt = data.getIntegerArrayListExtra("selected_student_ids");
                    if (selectedStudentIdsInt != null) {
                        List<Long> selectedStudentIdsLong = new ArrayList<>();
                        for (Integer idInt : selectedStudentIdsInt) {
                            selectedStudentIdsLong.add(idInt != null ? idInt.longValue() : null);
                        }
                        selectedStudents = new ArrayList<>(selectedStudentIdsLong);
                        editTextStudent.setText(selectedStudents.toString());
                    }
            }
        }
        if (requestCode == REQUEST_CODE_NEW_ACTIVITY_TEACHER && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                    ArrayList<Integer> selectedTeacherIdsInt = data.getIntegerArrayListExtra("selected_teacher_ids");
                    if (selectedTeacherIdsInt != null) {
                        List<Long> selectedTeacherIdsLong = new ArrayList<>();
                        for (Integer idInt : selectedTeacherIdsInt) {
                            selectedTeacherIdsLong.add(idInt != null ? idInt.longValue() : null);
                        }
                        selectedTeachers = new ArrayList<>(selectedTeacherIdsLong);
                        editTextTeacher.setText(selectedTeachers.toString());
                    }
            }
        }
    }

    private void callApiAndGetStudents() {
        apiService.getListStudent("",-1).enqueue(new Callback<List<StudentResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<StudentResponse>> call,@NotNull Response<List<StudentResponse>> response) {
                if (response.isSuccessful()) {
                    List<StudentResponse> studentResponseList = response.body();
                    showStudentSelectionDialog(studentResponseList);
                } else {
                    Toast.makeText(ExamClassAddActivity.this, "Lấy danh sách sinh viên thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<StudentResponse>> call,@NotNull Throwable t) {
                Toast.makeText(ExamClassAddActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showStudentSelectionDialog(List<StudentResponse> studentList) {
        Intent intent = new Intent(ExamClassAddActivity.this, StudentTestClassExamActivity.class);
        intent.putParcelableArrayListExtra("student_list", new ArrayList<>(studentList));
        intent.putExtra("studentcheck", true);
        startActivityForResult(intent, REQUEST_CODE_NEW_ACTIVITY);
    }

    private void callApiAndGetItemTest() {
        Call<TestClassResponse> call = apiService.getTestList(
                subjectId,
                "ALL",
                "",
                "",
                semesterId,
                "",
                0,
                10000,
                "modifiedAt"
        );

        call.enqueue(new Callback<TestClassResponse>() {
            @Override
            public void onResponse(@NotNull Call<TestClassResponse> call,@NotNull Response<TestClassResponse> response) {
                if (response.isSuccessful()) {
                    TestClassResponse testClassResponse = response.body();
                    assert testClassResponse != null;
                    List<TestClassResponse.TestItem> testItems =  testClassResponse.getContent();
                    showDialogTestID(testItems);
                    Toast.makeText(ExamClassAddActivity.this, " thành công", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ExamClassAddActivity.this, " thất bại", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NotNull Call<TestClassResponse> call,@NotNull Throwable t) {
                Toast.makeText(ExamClassAddActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void showDialogTestID(List<TestClassResponse.TestItem> testClassResponseList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn bộ đề");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_test_class, null);
        builder.setView(dialogView);

        RecyclerView recyclerViewMonHoc = dialogView.findViewById(R.id.recyclerViewMonHoc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMonHoc.setLayoutManager(layoutManager);

        TessClassExamIdAdapter adapter = new TessClassExamIdAdapter(testClassResponseList,getApplicationContext());
        recyclerViewMonHoc.setAdapter(adapter);

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        adapter.setOnItemClickListener(testClassItem -> {
            editClassTestCodeId.setText(testClassItem.getName());
            testId = Long.valueOf(testClassItem.getId());
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            } else {
                Log.e("ExamClassAddActivity", "AlertDialog is null or not showing");
            }
        });


    }

    private void anhXa() {
        editClassRoom = findViewById(R.id.txt_class_room_name);
        editExamineTime = findViewById(R.id.txt_class_semester);
        editClassHk = findViewById(R.id.txt_class_code);
        editClassTestCodeId = findViewById(R.id.txt_class_test_id);
        editTextStudent = findViewById(R.id.txt_class_student);
        btnClassExamSave = findViewById(R.id.btn_class_exam_save);
        editTextTeacher = findViewById(R.id.txt_class_teacher);
        editSemester = findViewById(R.id.txt_class_semester_1);
        editSubject = findViewById(R.id.txt_class_subject_1);
    }
}