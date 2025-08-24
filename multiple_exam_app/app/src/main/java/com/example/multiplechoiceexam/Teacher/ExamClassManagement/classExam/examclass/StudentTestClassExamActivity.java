package com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.examClass.UserExamClassDTO;
import com.example.multiplechoiceexam.dto.examClass.UserExamClassRoleEnum;
import com.example.multiplechoiceexam.dto.student.StudentResponse;
import com.example.multiplechoiceexam.dto.teacher.TeacherResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTestClassExamActivity extends AppCompatActivity {

    private Long id;
    boolean studentCheck = false;
    String selectedRole,check;
    private ApiService apiService;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_test_class_exam);

        apiService = RetrofitClient.getApiService(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudents);
        Button btnOk = findViewById(R.id.btn_add_test_class_exam);
        studentCheck = getIntent().getBooleanExtra("studentcheck",false);
        check = getIntent().getStringExtra("check");
        id = getIntent().getLongExtra("examClassId", -1L);
        if (id == -1L ) {
                if(studentCheck){
                    ArrayList<StudentResponse> receivedStudentList = getIntent().getParcelableArrayListExtra("student_list");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    StudentTestClassExamAdapter adapter = new StudentTestClassExamAdapter(receivedStudentList);
                    recyclerView.setAdapter(adapter);
                    btnOk.setOnClickListener(view -> {
                        List<Long> selectedStudentIds = adapter.getSelectedStudentIds();
                        List<Integer> selectedStudentIdsInt = new ArrayList<>();
                        for (Long id : selectedStudentIds) {
                            selectedStudentIdsInt.add(id.intValue());
                        }
                        Intent resultIntent = new Intent();
                        resultIntent.putIntegerArrayListExtra("selected_student_ids", new ArrayList<>(selectedStudentIdsInt));
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                });}else {
                    ArrayList<TeacherResponse> receivedTeacherList = getIntent().getParcelableArrayListExtra("teacher_list");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    TeacherTestClassExamAdapter adapterTeacher = new TeacherTestClassExamAdapter(receivedTeacherList);
                    recyclerView.setAdapter(adapterTeacher);
                    btnOk.setOnClickListener(view -> {
                        List<Long> selectedTeacherIds = adapterTeacher.getSelectedTeacherIds();
                        List<Integer> selectedTeacherIdsInt = new ArrayList<>();
                        for (Long id : selectedTeacherIds) {
                            selectedTeacherIdsInt.add(id.intValue());
                        }
                        Intent resultIntent = new Intent();
                        resultIntent.putIntegerArrayListExtra("selected_teacher_ids", new ArrayList<>(selectedTeacherIdsInt));
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    });
                }
            }else{
        if(check == null || "student".equals(check)) {
            ArrayList<StudentResponse> receivedStudentList = getIntent().getParcelableArrayListExtra("student_list");
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            StudentTestClassExamAdapter adapter = new StudentTestClassExamAdapter(receivedStudentList);
            recyclerView.setAdapter(adapter);
            id = getIntent().getLongExtra("examClassId", -1);
            selectedRole = getIntent().getStringExtra("role");

                btnOk.setOnClickListener(view -> {
                    List<Long> selectedStudentIds = adapter.getSelectedStudentIds();
                    List<StudentResponse> selectedStudents = new ArrayList<>();
                    for (StudentResponse student : receivedStudentList) {
                        if (selectedStudentIds.contains(student.getId())) {
                            selectedStudents.add(student);
                        }
                    }

                    UserExamClassDTO userExamClassDTO = new UserExamClassDTO();
                    userExamClassDTO.setExamClassId(id);

                    for (StudentResponse selectedStudent : selectedStudents) {
                        UserExamClassDTO.UserExamClassRoleDTO roleDTO = new UserExamClassDTO.UserExamClassRoleDTO();
                        roleDTO.setUserId(selectedStudent.getId());
                        roleDTO.setRole(UserExamClassRoleEnum.STUDENT);

                        userExamClassDTO.getLstParticipant().add(roleDTO);
                    }
                    apiService.updateParticipantToExamClass(userExamClassDTO).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NotNull Call<Void> call,@NotNull Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(StudentTestClassExamActivity.this, "Thaành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(StudentTestClassExamActivity.this, " thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                            Toast.makeText(StudentTestClassExamActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();

                        }
                    });
                });
            }else {
                ArrayList<TeacherResponse> receivedTeacherList = getIntent().getParcelableArrayListExtra("teacher_list");
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                TeacherTestClassExamAdapter adapterTeacher = new TeacherTestClassExamAdapter(receivedTeacherList);
                recyclerView.setAdapter(adapterTeacher);
                id = getIntent().getLongExtra("examClassId", -1);
                selectedRole = getIntent().getStringExtra("role");
                btnOk.setOnClickListener(view -> {
                    List<Long> selectedTeacherIds = adapterTeacher.getSelectedTeacherIds();
                    List<TeacherResponse> selectedTeachers = new ArrayList<>();
                    for (TeacherResponse teacher : receivedTeacherList) {
                        if (selectedTeacherIds.contains(teacher.getId())) {
                            selectedTeachers.add(teacher);
                        }
                    }

                    UserExamClassDTO userExamClassDTO = new UserExamClassDTO();
                    userExamClassDTO.setExamClassId(id);

                    for (TeacherResponse selectedStudent : selectedTeachers) {
                        UserExamClassDTO.UserExamClassRoleDTO roleDTO = new UserExamClassDTO.UserExamClassRoleDTO();
                        roleDTO.setUserId(selectedStudent.getId());
                        roleDTO.setRole(UserExamClassRoleEnum.SUPERVISOR);

                        userExamClassDTO.getLstParticipant().add(roleDTO);
                    }
                    apiService.updateParticipantToExamClass(userExamClassDTO).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(StudentTestClassExamActivity.this, "Thaành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(StudentTestClassExamActivity.this, " thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<Void> call,@NotNull Throwable t) {
                            Toast.makeText(StudentTestClassExamActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();

                        }
                    });
                });
            }
        }
    }


}