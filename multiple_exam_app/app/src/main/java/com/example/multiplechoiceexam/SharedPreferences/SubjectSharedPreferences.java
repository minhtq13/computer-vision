package com.example.multiplechoiceexam.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.multiplechoiceexam.dto.subject.SubjectCode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubjectSharedPreferences {
    private static final String PREF_NAME = "subject_preferences";
    private static final String SUBJECT_LIST_KEY = "subject_list";

    private SharedPreferences sharedPreferences;

    public SubjectSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSubjectList(List<SubjectCode> subjectList) {
        Gson gson = new Gson();
        String subjectListJson = gson.toJson(subjectList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SUBJECT_LIST_KEY, subjectListJson);
        editor.apply();
    }

    public List<SubjectCode> getSubjectList() {
        String subjectListJson = sharedPreferences.getString(SUBJECT_LIST_KEY, null);
        if (subjectListJson != null) {
            Type type = new TypeToken<List<SubjectCode>>() {}.getType();
            return new Gson().fromJson(subjectListJson, type);
        }
        return new ArrayList<>();
    }
}
