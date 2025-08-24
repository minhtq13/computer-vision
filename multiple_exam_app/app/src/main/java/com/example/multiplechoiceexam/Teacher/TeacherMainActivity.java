package com.example.multiplechoiceexam.Teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.multiplechoiceexam.Models.TabItem;
import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.SharedPreferences.AuthPreferences;
import com.example.multiplechoiceexam.SignIn.SignInActivity;
import com.example.multiplechoiceexam.Utils.MemoryData;
import com.example.multiplechoiceexam.Utils.Utility;
import com.example.multiplechoiceexam.databinding.ActivityEntryAppWithDrawermenuBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class TeacherMainActivity extends AppCompatActivity {
    List<String> rolesList;
    private AuthPreferences authPreferences;
    private boolean doubleBackToExitPressedOnce = false;
    private ActivityEntryAppWithDrawermenuBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryAppWithDrawermenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Inflate the custom toolbar layout
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout customToolbarLayout = (RelativeLayout) inflater.inflate(R.layout.custom_toolbar_layout, null);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        Toolbar toolbar =   binding.toolbar;
        toolbar.addView(customToolbarLayout);
        authPreferences = new AuthPreferences(getApplicationContext());
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(getApplicationContext());
        rolesList = accountSharedPreferences.getRoles();
        ViewPager2 viewPager = findViewById(R.id.viewpager_main_entry);
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(this);
        List<TabItem> tabItems = new ArrayList<>();
        tabItems.add(new TabItem("Trang chủ", R.drawable.ic_home_24));
        tabItems.add(new TabItem("Môn thi", R.drawable.ic_note_alt_24));
        tabItems.add(new TabItem("Tài khoản", R.drawable.ic_account_circle_24));
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout_main_header);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            TabItem tabItem = tabItems.get(position);
            tab.setText(tabItem.getTitle());
            tab.setIcon(tabItem.getIconResId());
        }
        ).attach();
        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_fingerprint){
                showFingerprintSetup();
            }
            if(item.getItemId() == R.id.nav_logout){
                showAlertDialog(TeacherMainActivity.this);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item);
    }

    public void updating(View view) {
        Utility.showToast(getApplicationContext(), "Tính năng đang được cập nhật!!");
    }


    private void showFingerprintSetup() {
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, new Executor() {
            @Override
            public void execute(Runnable command) {
                new Handler(Looper.getMainLooper()).post(command);
            }
        }, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                handleFingerprintSetupSuccess();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                handleFingerprintSetupError(errorCode);
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Setup")
                .setDescription("Use your fingerprint to log in")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void handleFingerprintSetupSuccess() {
        authPreferences.setFingerprintOptionClicked(true);
        authPreferences.setFingerprintUsername(authPreferences.getUsername());
        authPreferences.setFingerprintPassword(authPreferences.getPassword());
        Utility.showToast(this, "Fingerprint setup successful!");
    }

    private void handleFingerprintSetupError(int errorCode) {
        Utility.showToast(this, "Fingerprint setup error: " + errorCode);
    }


    private void showAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Xác nhận")
                .setMessage("Bạn chắc chắn muốn đăng xuất chứ?")
                .setCancelable(true)
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    AccountSharedPreferences accountSharedPreferences = new AccountSharedPreferences(TeacherMainActivity.this);
                    accountSharedPreferences.clear();
                    MemoryData.saveAccessToken("", context);
                    context.startActivity(new Intent(context, SignInActivity.class));
                    Utility.showToast(context, "Đăng xuất thành công !");
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("Hủy bỏ", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}