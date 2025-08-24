package com.example.multiplechoiceexam.SharedPreferences;

//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class AccountSharedPreferences {
//    private static final String PREF_NAME = "account_prefs";
//    private static final String KEY_ACCESS_TOKEN = "access_token";
//    private static final String KEY_REFRESH_TOKEN = "refresh_token";
//    private static final String KEY_MESSAGE = "message";
//    private static final String KEY_ISSUEDAT = "issuedAt";
//    private static final String KEY_ROLES = "roles";
//
//    private String accessToken;
//    private String refreshToken;
//    private Object message;
//    private String issuedAt;
//    private String roles;
//
//    private SharedPreferences preferences;
//    public AccountSharedPreferences() {
//    }
//
//    public AccountSharedPreferences(Context context) {
//        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        accessToken = preferences.getString(KEY_ACCESS_TOKEN, null);
//        refreshToken = preferences.getString(KEY_REFRESH_TOKEN, null);
//        message = preferences.getString(KEY_MESSAGE, null);
//        issuedAt = preferences.getString(KEY_ISSUEDAT, null);
//        roles = preferences.getString(KEY_ROLES, null);
//    }
//
//    // Các phương thức getter và setter cho các trường
//
//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(KEY_ACCESS_TOKEN, accessToken);
//        editor.apply();
//    }
//
//    public String getRefreshToken() {
//        return refreshToken;
//    }
//
//    public void setRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
//        editor.apply();
//    }
//
//    public Object getMessage() {
//        return message;
//    }
//
//    public void setMessage(Object message) {
//        this.message = message;
//        SharedPreferences.Editor editor = preferences.edit();
//        // Chuyển đối tượng message thành chuỗi và lưu vào SharedPreferences (nếu có thể chuyển đổi)
//        if (message != null) {
//            String messageStr = message.toString();
//            editor.putString(KEY_MESSAGE, messageStr);
//        } else {
//            editor.remove(KEY_MESSAGE); // Xóa trường message nếu nó không tồn tại
//        }
//        editor.apply();
//    }
//
//    public String getIssuedAt() {
//        return issuedAt;
//    }
//
//    public void setIssuedAt(String username) {
//        this.issuedAt = username;
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(KEY_ISSUEDAT, username);
//        editor.apply();
//    }
//
//
//    public List<String> getRoles() {
//        if (roles != null) {
//            return new ArrayList<>(Arrays.asList(roles.split(",")));
//        }
//        return new ArrayList<>();
//    }
//
//    public void setRoles(List<String> roles) {
//        this.roles = TextUtils.join(",", roles); // Chuyển danh sách thành chuỗi và lưu vào SharedPreferences
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(KEY_ROLES, this.roles);
//        editor.apply();
//    }
//    public void clear() {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove(KEY_ACCESS_TOKEN);
//        editor.remove(KEY_REFRESH_TOKEN);
//        editor.remove(KEY_MESSAGE);
//        editor.remove(KEY_ISSUEDAT);
//        editor.remove(KEY_ROLES);
//        editor.apply();
//    }
//}

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;

public class AccountSharedPreferences {

    private static final String PREF_NAME = "account_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ISSUEDAT = "issuedAt";
    private static final String KEY_ROLES = "roles";
    private static final String KEY_ALIAS = "datn";

    private final SharedPreferences preferences;
    private final KeyStore keyStore;

    public AccountSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        keyStore = initKeyStore();
    }

    private KeyStore initKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            createKeyPairIfNotExists(keyStore);
            return keyStore;
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize KeyStore", e);
        }
    }

    private void createKeyPairIfNotExists(KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");

                keyPairGenerator.initialize(
                        new KeyGenParameterSpec.Builder(
                                KEY_ALIAS,
                                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setDigests(KeyProperties.DIGEST_SHA256)
                                .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                .build());

                keyPairGenerator.generateKeyPair();
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | KeyStoreException e) {
            throw new RuntimeException("Failed to create key pair", e);
        }
    }

    private String encryptData(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data", e);
        }
    }

    private String decryptData(String encryptedData) {
        try {
            if (isValidBase64(encryptedData)) {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
                byte[] decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));
                return new String(decryptedBytes);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }


    private boolean isValidBase64(String input) {
        try {
            byte[] decodedBytes = Base64.decode(input, Base64.DEFAULT);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate(KEY_ALIAS).getPublicKey();
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get public key", e);
        }
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RuntimeException("Failed to get private key", e);
        }
    }

    public void setAccessToken(String accessToken) {
        saveToKeystore(KEY_ACCESS_TOKEN,accessToken);
    }

    public String getAccessToken() {
        return readFromKeystore(KEY_ACCESS_TOKEN,null);
    }
    public void setRefreshToken(String refreshToken) {
        saveToKeystore(KEY_REFRESH_TOKEN, refreshToken);
    }

    public String getRefreshToken() {
        return readFromKeystore(KEY_REFRESH_TOKEN, null);
    }

    public void setMessage(Object message) {
        saveToKeystore(KEY_MESSAGE, message != null ? message.toString() : null);
    }

    public Object getMessage() {
        String messageStr = readFromKeystore(KEY_MESSAGE, null);
        return messageStr != null ? messageStr : null;
    }

    public void setIssuedAt(String issuedAt) {
        saveToKeystore(KEY_ISSUEDAT, issuedAt);
    }

    public String getIssuedAt() {
        return readFromKeystore(KEY_ISSUEDAT, null);
    }

    public void setRoles(List<String> roles) {
        String rolesStr = TextUtils.join(",", roles);
        saveToKeystore(KEY_ROLES, rolesStr);
    }

    public List<String> getRoles() {
        String rolesStr = readFromKeystore(KEY_ROLES, null);
        if (rolesStr != null) {
            return new ArrayList<>(Arrays.asList(rolesStr.split(",")));
        }
        return new ArrayList<>();
    }

    private void saveToKeystore(String key, String data) {
        try {
            preferences.edit().putString(key, encryptData(data)).apply();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save data to keystore", e);
        }
    }

    private String readFromKeystore(String key, String defaultValue) {
        String encryptedData = preferences.getString(key, null);
        return (encryptedData != null && isValidBase64(encryptedData)) ? decryptData(encryptedData) : defaultValue;
    }

    public void clear() {
        try {
            preferences.edit().remove(KEY_ACCESS_TOKEN).apply();
            preferences.edit().remove(KEY_REFRESH_TOKEN).apply();
            preferences.edit().remove(KEY_MESSAGE).apply();
            preferences.edit().remove(KEY_ISSUEDAT).apply();
            preferences.edit().remove(KEY_ROLES).apply();

            // Xóa cả trong keystore
            keyStore.deleteEntry(KEY_ALIAS);
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to clear data", e);
        }
    }
}
