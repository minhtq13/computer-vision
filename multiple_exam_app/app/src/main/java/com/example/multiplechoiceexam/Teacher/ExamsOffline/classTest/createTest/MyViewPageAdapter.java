package com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.autoTest.ClassAutoTestFragment;
import com.example.multiplechoiceexam.Teacher.ExamsOffline.classTest.createTest.manualTest.ClassManualTestFragment;

public class MyViewPageAdapter extends FragmentStateAdapter {

    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MyViewPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MyViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ClassManualTestFragment();
            case 0:
            default:
                return new ClassAutoTestFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
