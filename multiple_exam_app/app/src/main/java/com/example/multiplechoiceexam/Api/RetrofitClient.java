package com.example.multiplechoiceexam.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.example.multiplechoiceexam.Utils.BaseUrlUtils;
import com.example.multiplechoiceexam.dto.auth.RefreshTokenResDTO;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static ApiService apiService;
    private static AccountSharedPreferences accountSharedPreferences;
    public static ApiService getApiService(Context context) {
        accountSharedPreferences = new AccountSharedPreferences(context);
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrlUtils.BaseUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient(context))
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private static OkHttpClient getOkHttpClient(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String authToken = accountSharedPreferences.getAccessToken();
               // String authToken = getAuthTokenFromSharedPreferences(context);
                if (accountSharedPreferences == null) {
                    accountSharedPreferences = new AccountSharedPreferences(context);
                }
                if (authToken != null && isTokenExpired(authToken)) {
                    refreshToken(context);
                    authToken = accountSharedPreferences.getAccessToken();
                    //authToken = getAuthTokenFromSharedPreferences(context);
                }


                    Request newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + authToken)
                            .build();

                    return chain.proceed(newRequest);
            }
        });
        httpClient.connectTimeout(500, TimeUnit.SECONDS);
        httpClient.readTimeout(500, TimeUnit.SECONDS);
        httpClient.writeTimeout(500, TimeUnit.SECONDS);
        return httpClient.build();
    }

    private static boolean isTokenExpired(String authToken) {
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

    private static void refreshToken(final Context context) {
        ApiService apiService = RetrofitClient.getApiService(context);
        Call<RefreshTokenResDTO> call = apiService.refreshToken(accountSharedPreferences.getRefreshToken());
        call.enqueue(new Callback<RefreshTokenResDTO>() {
            @Override
            public void onResponse(@NotNull Call<RefreshTokenResDTO> call, retrofit2.Response<RefreshTokenResDTO> response) {
                if (response.isSuccessful()) {
                    RefreshTokenResDTO newAuthToken = response.body();
                    assert newAuthToken != null;
                    accountSharedPreferences.setAccessToken(newAuthToken.getAccessToken());
                    accountSharedPreferences.setRefreshToken(newAuthToken.getRefreshToken());
                    //saveAuthTokenToSharedPreferences(newAuthToken.getAccessToken(), context);
                } else {
                    Log.e("Refresh Token", "Failed to refresh token");
                }
            }

            @Override
            public void onFailure(@NotNull Call<RefreshTokenResDTO> call, @NotNull Throwable t) {
                Log.e("Refresh Token", "Network error while refreshing token", t);
            }
        });
    }

//    private static String getAuthTokenFromSharedPreferences(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("account_prefs", Context.MODE_PRIVATE);
//        return sharedPreferences.getString("access_token", null);
//    }
//
//    private static void saveAuthTokenToSharedPreferences(String authToken, Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("account_prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("access_token", authToken);
//        editor.apply();
//    }
//    private static String getRefreshTokenFromSharedPreferences(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("account_prefs", Context.MODE_PRIVATE);
//        return sharedPreferences.getString("refresh_token", null);
//    }
}

