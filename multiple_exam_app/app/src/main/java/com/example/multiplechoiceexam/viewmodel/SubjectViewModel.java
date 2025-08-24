package com.example.multiplechoiceexam.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.multiplechoiceexam.dto.subject.SubjectResponse;

public class SubjectViewModel extends AndroidViewModel {

    private final MutableLiveData<Long> selectedSubjectId = new MutableLiveData<>();
    private final MutableLiveData<SubjectResponse.SubjectItem> selectedSubject = new MutableLiveData<>();
    private final MutableLiveData<Integer> deletedSubjectPosition = new MutableLiveData<>();

    public SubjectViewModel(@NonNull Application application) {
        super(application);
    }

    public void onSubjectClicked(long id) {
        selectedSubjectId.setValue(id);
    }

    public void deleteSubject(SubjectResponse.SubjectItem subject, int position) {
        // Perform delete operation
        // After deletion, notify the adapter
        // For example:
        // subjectRepository.deleteSubject(subject.getId(), new Callback() {
        //     @Override
        //     public void onSuccess() {
        //         deletedSubjectPosition.setValue(position);
        //     }
        //     @Override
        //     public void onError() {
        //         // Handle error
        //     }
        // });
    }

    public void updateSubject(SubjectResponse.SubjectItem subject) {
        selectedSubject.setValue(subject);
    }

    public MutableLiveData<Long> getSelectedSubjectId() {
        return selectedSubjectId;
    }

    public MutableLiveData<SubjectResponse.SubjectItem> getSelectedSubject() {
        return selectedSubject;
    }

    public MutableLiveData<Integer> getDeletedSubjectPosition() {
        return deletedSubjectPosition;
    }
}
