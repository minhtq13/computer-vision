package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.R;

import com.example.multiplechoiceexam.dto.common.ICommonIdCodeName;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;
import com.example.multiplechoiceexam.dto.test.TestClassRequest;
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


public class ClassAutoTestFragment extends Fragment {

    MultiAutoCompleteTextView txtChapter;
    TestClassRequest.GenTestConfigDTO testConfigDTO;
    private ChapterAutoCompleteAdapter chapterAdapter;
    TextInputEditText txtTimeEndStart,txtTimeEnd,txtQuestion,txtDua,txtPoint,
            txtSemester,txtSubject, textName;
    EditText txtEasy,txtMedium,txtHard;
    private RecyclerView chapterRecyclerView;
    private ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    Long subjectId,semesterId;
    private List<Integer> selectedChapterIds = new ArrayList<>();
    Button btnSave;

    public ClassAutoTestFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"MissingInflatedId","DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_auto_test, container, false);

        textName = view.findViewById(R.id.txt_name_test);
        txtSubject = view.findViewById(R.id.txt_subject_test);
        txtChapter = view.findViewById(R.id.txt_chapter_test);
        txtTimeEndStart = view.findViewById(R.id.txt_time_test_start);
        txtTimeEnd = view.findViewById(R.id.txt_time_test_end);
        txtQuestion = view.findViewById(R.id.txt_question_test);
        relativeLayout = view.findViewById(R.id.relative_question);
        txtDua = view.findViewById(R.id.txt_dua_test);
        txtPoint = view.findViewById(R.id.txt_point_test);
        txtSemester =view.findViewById(R.id.txt_semester_test);
        txtEasy =view.findViewById(R.id.txt_easy);
        txtMedium=view.findViewById(R.id.txt_trungbinh);
        txtHard = view.findViewById(R.id.txt_kho);
        btnSave = view.findViewById(R.id.btn_save);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tạo đề thi...");

        txtHard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkInputs();
            }
        });

        txtSemester.setOnClickListener(view1 -> choseSemesterId());

        txtSubject.setOnClickListener(view12 -> choseSubjectId());

        chapterRecyclerView = view.findViewById(R.id.chapterRecyclerView);
        chapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chapterAdapter = new ChapterAutoCompleteAdapter(getContext());
        chapterRecyclerView.setAdapter(chapterAdapter);


        txtChapter.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                chapterRecyclerView.setVisibility(View.VISIBLE);
            } else {
                chapterRecyclerView.setVisibility(View.GONE);
            }
        });

        txtChapter.setOnClickListener(view13 -> {
            List<ICommonIdCodeName> selectedChapters = chapterAdapter.getSelectedChapters();

            StringBuilder selectedChapterNames = new StringBuilder();
            for (ICommonIdCodeName chapter : selectedChapters) {
                selectedChapterNames.append(chapter.getName()).append(", ");
            }
            if (selectedChapterNames.length() > 0) {
                selectedChapterNames.delete(selectedChapterNames.length() - 2, selectedChapterNames.length());
            }
            txtChapter.setText(selectedChapterNames.toString());
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


//        txtTimeEnd.setOnClickListener(view14 ->{
//            DatePickerDialog datePickerDialog1 = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
//                int formattedMonth = month1 + 1;
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hourOfDay, minute) -> {
//                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
//                    String date = String.format("%02d/%02d/%d %s", day1, formattedMonth, year1, formattedTime);
//
//                    txtTimeEnd.setText(date);
//                }, 0, 0, true);
//                timePickerDialog.show();
//            }, year, month, day);
//            datePickerDialog1.show();
//        });
        txtTimeEnd.setOnClickListener(view14 -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                if (year1 > year || (year1 == year && month1 > month) || (year1 == year && month1 == month && day1 >= day)) {
                    int formattedMonth = month1 + 1;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hourOfDay, minute) -> {
                        String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                        String date = String.format("%02d/%02d/%d %s", day1, formattedMonth, year1, formattedTime);
                        txtTimeEnd.setText(date);
                    }, 0, 0, true);
                    timePickerDialog.show();
                } else {
                    txtTimeEnd.setText("");
                    Toast.makeText(getContext(), "Không thể chọn ngày trong quá khứ", Toast.LENGTH_SHORT).show();
                }
            }, year, month, day);
            datePickerDialog1.show();
        });

//        txtTimeEndStart.setOnClickListener(view15 -> {
//            DatePickerDialog datePickerDialog1 = new DatePickerDialog(getContext(), (datePicker, year12, month12, day12) -> {
//                int formattedMonth = month12 + 1;
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hourOfDay, minute) -> {
//                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
//                    String date = String.format("%02d/%02d/%d %s", day12, formattedMonth, year12, formattedTime);
//
//                    txtTimeEndStart.setText(date);
//                }, 0, 0, true);
//                timePickerDialog.show();
//            }, year, month, day);
//            datePickerDialog1.show();
//        });
        txtTimeEndStart.setOnClickListener(view15 -> {
            DatePickerDialog datePickerDialog1 = new DatePickerDialog(getContext(), (datePicker, year12, month12, day12) -> {
                if (year12 > year || (year12 == year && month12 > month) || (year12 == year && month12 == month && day12 >= day)) {
                    int formattedMonth = month12 + 1;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hourOfDay, minute) -> {
                        String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                        String date = String.format("%02d/%02d/%d %s", day12, formattedMonth, year12, formattedTime);

                        txtTimeEndStart.setText(date);
                    }, 0, 0, true);
                    timePickerDialog.show();
                } else {
                    txtTimeEndStart.setText("");
                    Toast.makeText(getContext(), "Không thể chọn ngày trong quá khứ", Toast.LENGTH_SHORT).show();
                }
            }, year, month, day);
            datePickerDialog1.show();
        });



        btnSave.setOnClickListener(v -> {
            boolean isValid = validateForm();
            if (isValid) {
                progressDialog.show();
                createRandomTest();
            }
        });

        return view;
    }

    private void checkInputs() {
        Integer questionQuantity;
        try {
            questionQuantity = Integer.parseInt(txtQuestion.getText().toString());
            testConfigDTO = new TestClassRequest.GenTestConfigDTO();
            Integer numTotalQuestion = questionQuantity;
            Integer numEasyQuestion = Integer.valueOf(txtEasy.getText().toString());
            Integer numMediumQuestion = Integer.valueOf(txtMedium.getText().toString());
            Integer numHardQuestion = Integer.valueOf(txtHard.getText().toString());

            if (numEasyQuestion + numMediumQuestion + numHardQuestion > numTotalQuestion) {
                Toast.makeText(getContext(), "lỗi nhiều hơn tổng số câu", Toast.LENGTH_SHORT).show();
                resetInputs();
            } else if (numEasyQuestion + numMediumQuestion + numHardQuestion < numTotalQuestion) {
                Toast.makeText(getContext(), "lỗi ít hơn tổng số câu", Toast.LENGTH_SHORT).show();
                resetInputs();
            } else {
                testConfigDTO.setNumTotalQuestion(numTotalQuestion);
                testConfigDTO.setNumEasyQuestion(numEasyQuestion);
                testConfigDTO.setNumMediumQuestion(numMediumQuestion);
                testConfigDTO.setNumHardQuestion(numHardQuestion);
            }

            if (questionQuantity < 1 || questionQuantity > 60) {
                txtQuestion.setError("Sỗ lượng câu hỏi từ 1 đến 60 câu");
                resetInputs();
            } else {
                txtQuestion.setError(null);
            }
        } catch (NumberFormatException e) {
            txtQuestion.setError("Vui lòng nhập số lượng câu hỏi !");
            resetInputs();
        }

    }

    private void choseSubjectId() {
        Call<SubjectResponse> call = RetrofitClient.getApiService(getContext()).getListSubject(
                "",
                -1L, // departmentId
                "",
                0, // page
                10000, // size
                "code" // sort
        );

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(@NotNull Call<SubjectResponse> call,@NotNull Response<SubjectResponse> response) {
                if (response.isSuccessful()) {
                    SubjectResponse subjectListResponse = response.body();
                    assert subjectListResponse != null;
                    List<SubjectResponse.SubjectItem> subjects = subjectListResponse.getContent();
                    showSubjectSelectionDialog(subjects);
                } else {
                    Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SubjectResponse> call,@NotNull Throwable t) {
                // Xử lý thất bại
            }
        });
    }

    private void showSubjectSelectionDialog(List<SubjectResponse.SubjectItem> subjects) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View dialogView = inflater.inflate(R.layout.custom_subject_selection_dialog, null);
        builder.setView(dialogView);

        RecyclerView recyclerViewSubjects = dialogView.findViewById(R.id.recyclerViewSubjects);
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose);

        recyclerViewSubjects.setLayoutManager(new LinearLayoutManager(getContext()));
        SubjectComboBoxAdapter subjectAdapter = new SubjectComboBoxAdapter(getContext(), subjects);
        recyclerViewSubjects.setAdapter(subjectAdapter);
        AlertDialog dialog = builder.create();
        subjectAdapter.setOnItemClickListener(subjectItem -> {
            txtSubject.setText(subjectItem.getTitle());
            subjectId = subjectItem.getId();
            loadChapters(subjectId);
            relativeLayout.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });
        dialog.show();

        btnClose.setOnClickListener(view -> dialog.dismiss());
    }

    private void choseSemesterId() {
        Call<List<SemesterBox>> call = RetrofitClient.getApiService(getContext()).getListSemester("");
        call.enqueue(new Callback<List<SemesterBox>>() {
            @Override
            public void onResponse(@NotNull Call<List<SemesterBox>> call, @NotNull Response<List<SemesterBox>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SemesterBox> semesterBoxes = response.body();
                    if (semesterBoxes != null && !semesterBoxes.isEmpty()) {
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_semester_list);
                        Window window = dialog.getWindow();
                        if (window != null) {
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int dialogWidth = (int) (displayMetrics.widthPixels * 0.8);
                            window.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                        TextView textViewDialogTitle = dialog.findViewById(R.id.textViewDialogTitle);
                        textViewDialogTitle.setText("Học kì");

                        RecyclerView recyclerViewSemesters = dialog.findViewById(R.id.recyclerViewSemesters);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerViewSemesters.setLayoutManager(layoutManager);

                        SemesterAdapter adapter = new SemesterAdapter(semesterBoxes,getContext());
                        recyclerViewSemesters.setAdapter(adapter);
                        adapter.setOnItemClickListener(semesterLists -> {
                            txtSemester.setText(semesterLists.getName());
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

    private boolean validateForm() {
        boolean isValid = true;

        String testDay = Objects.requireNonNull(txtTimeEndStart.getText()).toString();
        if (testDay.isEmpty()) {
            txtTimeEndStart.setError("Please enter test day !");
            isValid = false;
        } else {
            txtTimeEndStart.setError(null);
        }

        int totalPoint;
        try {
            totalPoint = Integer.parseInt(Objects.requireNonNull(txtPoint.getText()).toString());
            if (totalPoint < 1 || totalPoint > 100) {
                txtPoint.setError("Total point must be between 1 and 100 !");
                isValid = false;
            } else {
                txtPoint.setError(null);
            }
        } catch (NumberFormatException e) {
            txtPoint.setError("Please enter a valid number for total point !");
            isValid = false;
        }

        String testTime = Objects.requireNonNull(txtTimeEnd.getText()).toString();
        if (testTime.isEmpty()) {
            txtTimeEnd.setError("Please enter test time !");
            isValid = false;
        } else {
            txtTimeEnd.setError(null);
        }

        String subjectCode = Objects.requireNonNull(txtSubject.getText()).toString();
        if (subjectCode.isEmpty()) {
            txtSubject.setError("Please enter subject code !");
            isValid = false;
        } else {
            txtSubject.setError(null);
        }

        int duration;
        try {
            duration = Integer.parseInt(txtDua.getText().toString());
            if (duration < 1) {
                txtDua.setError("Duration must be greater than 0 !");
                isValid = false;
            } else {
                txtDua.setError(null);
            }
        } catch (NumberFormatException e) {
            txtDua.setError("Please enter a valid number for duration !");
            isValid = false;
        }

        return isValid;
    }

    private void resetInputs() {


        if (!TextUtils.isEmpty(txtEasy.getText())) {
            txtEasy.setText("");
        }

        if (!TextUtils.isEmpty(txtMedium.getText())) {
            txtMedium.setText("");
        }

        if (!TextUtils.isEmpty(txtHard.getText())) {
            txtHard.setText("");
        }
    }



    private void loadChapters(Long subjectId) {
        Call<List<ICommonIdCodeName>> call = RetrofitClient.getApiService(getContext()).getAllSubjectChapters(subjectId);
        call.enqueue(new Callback<List<ICommonIdCodeName>>() {
            @Override
            public void onResponse(@NotNull Call<List<ICommonIdCodeName>> call,@NotNull Response<List<ICommonIdCodeName>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<ICommonIdCodeName> chapters = response.body();
                    chapterAdapter.setChapters(chapters);

                } else {
                    Toast.makeText(getContext(), "thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ICommonIdCodeName>> call,@NotNull Throwable t) {

            }
        });
    }

    private void createRandomTest() {
        List<ICommonIdCodeName> selectedChapters = chapterAdapter.getSelectedChapters();

        TestClassRequest testRequest = new TestClassRequest();
        testRequest.setName(Objects.requireNonNull(textName.getText()).toString());
        testRequest.setSubjectId(subjectId);
        testRequest.setQuestionQuantity(Integer.parseInt(Objects.requireNonNull(txtQuestion.getText()).toString()));
        testRequest.setStartTime(Objects.requireNonNull(txtTimeEndStart.getText()).toString());
        testRequest.setEndTime(Objects.requireNonNull(txtTimeEnd.getText()).toString());
        testRequest.setTotalPoint(Integer.parseInt(Objects.requireNonNull(txtPoint.getText()).toString()));
        testRequest.setDuration(Integer.parseInt(Objects.requireNonNull(txtDua.getText()).toString()));
        testRequest.setSemesterId(semesterId);
        testRequest.setGenerateConfig(testConfigDTO);

        List<Long> chapterOrders = new ArrayList<>();
        for (ICommonIdCodeName selectedChapter : selectedChapters) {
            chapterOrders.add(selectedChapter.getId());
        }
        testRequest.setChapterIds(chapterOrders);

        Call<Long> call = RetrofitClient.getApiService(getContext()).createRandomTest(testRequest);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(@NotNull Call<Long> call,@NotNull Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "thành công!", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    progressDialog.dismiss();
                    getActivity().finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<Long> call,@NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Lỗi gọi", Toast.LENGTH_SHORT).show();
            }
        });
    }

}