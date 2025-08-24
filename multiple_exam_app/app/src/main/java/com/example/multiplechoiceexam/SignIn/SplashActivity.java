package com.example.multiplechoiceexam.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiplechoiceexam.R;
import com.example.multiplechoiceexam.Teacher.TeacherMainActivity;
import com.example.multiplechoiceexam.Utils.MemoryData;
import com.example.multiplechoiceexam.Utils.Utility;

import java.util.Objects;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progress_bar_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String accessToken = MemoryData.getAccessToken(SplashActivity.this);
                if(accessToken != null) {
                    if(Objects.equals(accessToken, "") ) {
                        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                        finish();
                    } else if (isTokenExpired(accessToken)) {
                        Utility.showToast(SplashActivity.this, "Phiên đăng nhập của bạn đã hết hạn vui lòng đăng nhập lại!");
                        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, TeacherMainActivity.class));
                        finish();
                    }
                }
            }
        },1000);
    }
    public static boolean isTokenExpired(String authToken) {
        if (authToken != null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey("ELEARNINGKsu295JdkjKHJJHW2349gfbnaf23cheh34HIMAde24")
                        .parseClaimsJws(authToken)
                        .getBody();

                long expirationTime = Long.parseLong(claims.get("exp").toString());
                long currentTime = System.currentTimeMillis() / 1000;

                return currentTime > expirationTime;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}