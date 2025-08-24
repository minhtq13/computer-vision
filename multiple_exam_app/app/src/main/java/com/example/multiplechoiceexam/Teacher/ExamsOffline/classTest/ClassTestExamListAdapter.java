package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.test.ClassTestInfo;

import java.util.List;

public class ClassTestExamListAdapter extends RecyclerView.Adapter<ClassTestExamListAdapter.ClassTestExamListViewHolder> {

    Context context;
    private final Integer testId;
    private final List<String> testSetNos;

    public ClassTestExamListAdapter(Integer testId, List<String> testSetNos, Context context) {
        this.testId = testId;
        this.testSetNos = testSetNos;
        this.context = context;
    }

    @NonNull
    @Override
    public ClassTestExamListAdapter.ClassTestExamListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_view_item, parent, false);
        return new ClassTestExamListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassTestExamListAdapter.ClassTestExamListViewHolder holder, int position) {
        holder.testCode.setText(String.valueOf(testSetNos.get(position)));
        holder.testDownload.setOnClickListener(view -> {
            int position1 = holder.getAdapterPosition();
            String testNo = String.valueOf(testSetNos.get(position1));
            ClassTestInfo testInfo = new ClassTestInfo(testNo, Long.valueOf(testId));

            Intent intent = new Intent(context, WordDocumentDowloadActivity.class);
            intent.putExtra("testInfo", testInfo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (testSetNos != null) {
            return testSetNos.size();
        } else {
            return 0;
        }
    }

    public class ClassTestExamListViewHolder extends RecyclerView.ViewHolder {

        TextView testCode, testDownload;

        public ClassTestExamListViewHolder(@NonNull View itemView) {
            super(itemView);
            testCode = itemView.findViewById(R.id.test_ma_de);
            testDownload = itemView.findViewById(R.id.test_download);
        }
    }
}
