package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.dto.common.ICommonIdCodeName;

import java.util.ArrayList;
import java.util.List;

public class ChapterAutoCompleteAdapter extends RecyclerView.Adapter<ChapterAutoCompleteAdapter.ChapterAutoCompleteViewHolder> {

    Context context;
    private List<ICommonIdCodeName> chapters;
    private List<ICommonIdCodeName> selectedChapters;

    public ChapterAutoCompleteAdapter(Context context) {
        this.context = context;
        chapters = new ArrayList<>();
        selectedChapters = new ArrayList<>();
    }
    public void setChapters(List<ICommonIdCodeName> chapters) {
        this.chapters = chapters;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterAutoCompleteAdapter.ChapterAutoCompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_multi_item, parent, false);
        return new ChapterAutoCompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAutoCompleteAdapter.ChapterAutoCompleteViewHolder holder, int position) {

        ICommonIdCodeName chapter = chapters.get(position);
        holder.chapterTitle.setText(chapter.getName());
        holder.chapterCheckBox.setChecked(selectedChapters.contains(chapter));
    }

    @Override
    public int getItemCount() {
        if (chapters != null) {
            return chapters.size();
        } else {
            return 0;
        }
    }

    public void toggleSelection(int position) {
        ICommonIdCodeName chapter = chapters.get(position);
        if (selectedChapters.contains(chapter)) {
            selectedChapters.remove(chapter);
        } else {
            selectedChapters.add(chapter);
        }
        notifyItemChanged(position);
    }

    public List<ICommonIdCodeName> getSelectedChapters() {
        return selectedChapters;
    }

    public class ChapterAutoCompleteViewHolder extends RecyclerView.ViewHolder {

        private TextView chapterTitle;
        private CheckBox chapterCheckBox;
        public ChapterAutoCompleteViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.chapterTitle);
            chapterCheckBox = itemView.findViewById(R.id.chapterCheckBox);

            chapterCheckBox.setOnClickListener(view -> {
                int position = getAdapterPosition();
                ICommonIdCodeName chapter = chapters.get(position);
                if (selectedChapters.contains(chapter)) {
                    selectedChapters.remove(chapter);
                } else {
                    selectedChapters.add(chapter);
                }
            });
        }
    }
}
