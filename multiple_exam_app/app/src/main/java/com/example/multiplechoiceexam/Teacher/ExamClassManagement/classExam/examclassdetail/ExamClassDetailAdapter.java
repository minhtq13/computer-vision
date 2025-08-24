package com.example.multiplechoiceexam.Teacher.ExamClassManagement.classExam.examclassdetail;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.dto.examClass.IExamClassParticipantDTO;
import com.example.multiplechoiceexam.dto.studentTest.StudentTestSetResultDTO;
import com.example.multiplechoiceexam.viewmodel.ExamClassDetailStudentViewModel;

import java.util.List;

public class ExamClassDetailAdapter extends RecyclerView.Adapter<ExamClassDetailAdapter.ExamClassDetailViewHolder> {

    private final List<IExamClassParticipantDTO> iExamClassParticipantDTOS;
    private final ExamClassDetailStudentViewModel viewModel;
    Context context;
    private final Integer flag;

    public ExamClassDetailAdapter(List<IExamClassParticipantDTO> iExamClassParticipantDTOS, Context context, Integer flag) {
        this.iExamClassParticipantDTOS = iExamClassParticipantDTOS;
        this.context = context;
        this.flag = flag;
        this.viewModel = new ExamClassDetailStudentViewModel((Application) context.getApplicationContext());
    }

    @NonNull
    @Override
    public ExamClassDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exam_class_detail_item, parent, false);
        return new ExamClassDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamClassDetailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtId.setText(String.valueOf(position + 1));
        holder.txtName.setText(iExamClassParticipantDTOS.get(position).getName());
        holder.txtMssv.setText(iExamClassParticipantDTOS.get(position).getCode());
        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(context);
        List<String> userRoles = accountSharedPreferences.getRoles();
        if(flag == 1){
            holder.textView2.setVisibility(View.INVISIBLE);
            holder.textTestSet.setVisibility(View.GONE);
            holder.txtGrade.setVisibility(View.GONE);
            holder.textViewTeacherGrade.setVisibility(View.GONE);
            holder.textViewTeacherTest.setVisibility(View.GONE);
            holder.textView10.setVisibility(View.VISIBLE);
        }
        if(userRoles.contains("ROLE_STUDENT") ){
            holder.textTestSet.setVisibility(View.GONE);
            holder.txtGrade.setVisibility(View.GONE);
            holder.textViewTeacherGrade.setVisibility(View.GONE);
            holder.textViewTeacherTest.setVisibility(View.GONE);
        }

        holder.txtExamClassCode.setText(iExamClassParticipantDTOS.get(position).getExamClassCode());

        Integer studentId = iExamClassParticipantDTOS.get(position).getId();
        viewModel.getStudentTestSetResult(iExamClassParticipantDTOS.get(position).getExamClassCode())
                .observeForever(studentTestSetResultDTOList -> {
                    if (studentTestSetResultDTOList != null && !studentTestSetResultDTOList.isEmpty()) {
                        for (StudentTestSetResultDTO resultDTO : studentTestSetResultDTOList) {
                            if (resultDTO.getStudentId().equals(Long.valueOf(studentId))) {
                                holder.textTestSet.setText(resultDTO.getTestSetCode());
                                holder.txtGrade.setText(String.format("%.2f", resultDTO.getTotalPoints()));
                                break;
                            }
                        }
                    }
                });
//        viewModel.getStudentTestSetResult(iExamClassParticipantDTOS.get(position).getExamClassCode())
//                .observeForever(studentTestSetResultDTOList -> {
//                    if (studentTestSetResultDTOList != null && !studentTestSetResultDTOList.isEmpty()) {
//                        StudentTestSetResultDTO resultDTO = studentTestSetResultDTOList.get(position);
//
//                        holder.textTestSet.setText(resultDTO.getTestSetCode());
//                        holder.txtGrade.setText(String.valueOf(resultDTO.getTotalPoints()));
//                    }
//                });
    }

    @Override
    public int getItemCount() {
        if (iExamClassParticipantDTOS != null) {
            return iExamClassParticipantDTOS.size();
        } else {
            return 0;
        }
    }

    public class ExamClassDetailViewHolder extends RecyclerView.ViewHolder {

        TextView txtId, txtName, txtMssv, txtExamClassCode, textTestSet, txtGrade;
        TextView textView2, textView10,textViewTeacherTest,textViewTeacherGrade;
        public ExamClassDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.student_exam_id);
            txtName = itemView.findViewById(R.id.student_exam_name);
            txtMssv = itemView.findViewById(R.id.student_exam_mssv);
            txtExamClassCode = itemView.findViewById(R.id.student_exam_date_test);
            textTestSet = itemView.findViewById(R.id.student_exam_state);
            txtGrade = itemView.findViewById(R.id.student_exam_grade);
            textView2 =itemView.findViewById(R.id.textviewStudent);
            textView10 = itemView.findViewById(R.id.textviewTeacher);
            textViewTeacherTest =itemView.findViewById(R.id.textviewTeacherTest);
            textViewTeacherGrade = itemView.findViewById(R.id.textviewTeacherGrade);
        }
    }
}

