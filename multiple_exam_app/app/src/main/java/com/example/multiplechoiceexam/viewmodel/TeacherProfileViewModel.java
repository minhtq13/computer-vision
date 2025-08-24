package com.example.multiplechoiceexam.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.multiplechoiceexam.Api.ApiService;
import com.example.multiplechoiceexam.Api.RetrofitClient;
import com.example.multiplechoiceexam.dto.auth.ProfileUserDTO;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherProfileViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<ProfileUserDTO> teacherProfileLiveData = new MutableLiveData<>();

    public TeacherProfileViewModel(@NonNull Application application) {
        super(application);
        apiService= RetrofitClient.getApiService(application);

    }

    public LiveData<ProfileUserDTO> getTeacherProfileLiveData() {
        return teacherProfileLiveData;
    }

    public void getTeacherProfile() {
        apiService.userProfile().enqueue(new Callback<ProfileUserDTO>() {
            @Override
            public void onResponse(@NotNull Call<ProfileUserDTO> call, @NotNull Response<ProfileUserDTO> response) {
                if (response.isSuccessful()) {
                    teacherProfileLiveData.setValue(response.body());
                } else {
                    teacherProfileLiveData.setValue(null);
                }
            }
            @Override
            public void onFailure(@NotNull Call<ProfileUserDTO> call,@NotNull Throwable t) {
                teacherProfileLiveData.setValue(null);
            }
        });
    }
}