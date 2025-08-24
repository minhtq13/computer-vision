package com.example.multiplechoiceexam.Teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.multiplechoiceexam.Teacher.ExamManagement.ExamManagementFragment;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.ExamsOfflineFragment;
import com.example.multiplechoiceexam.Teacher.ProfileUser.teacher.ProfileTeacherFragment;

class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ExamsOfflineFragment();
            case 1:
                return new ExamManagementFragment();
            case 2:
                return new ProfileTeacherFragment();

        }
        return new ExamsOfflineFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}