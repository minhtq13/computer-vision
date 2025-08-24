package com.example.multiplechoiceexam.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreferences {

    private static final String PREF_NAME = "auth_pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FINGERPRINT_OPTION_CLICKED = "fingerprint_option_clicked";
    private static final String KEY_FINGERPRINT_USERNAME = "fingerprint_username";
    private static final String KEY_FINGERPRINT_PASSWORD = "fingerprint_password";

    private final SharedPreferences sharedPreferences;

    public AuthPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString(KEY_PASSWORD, password).apply();
    }

    public boolean isFingerprintOptionClicked() {
        return sharedPreferences.getBoolean(KEY_FINGERPRINT_OPTION_CLICKED, false);
    }

    public void setFingerprintOptionClicked(boolean clicked) {
        sharedPreferences.edit().putBoolean(KEY_FINGERPRINT_OPTION_CLICKED, clicked).apply();
    }

    public String getFingerprintUsername() {
        return sharedPreferences.getString(KEY_FINGERPRINT_USERNAME, "");
    }

    public void setFingerprintUsername(String username) {
        sharedPreferences.edit().putString(KEY_FINGERPRINT_USERNAME, username).apply();
    }

    public String getFingerprintPassword() {
        return sharedPreferences.getString(KEY_FINGERPRINT_PASSWORD, "");
    }

    public void setFingerprintPassword(String password) {
        sharedPreferences.edit().putString(KEY_FINGERPRINT_PASSWORD, password).apply();
    }
}
