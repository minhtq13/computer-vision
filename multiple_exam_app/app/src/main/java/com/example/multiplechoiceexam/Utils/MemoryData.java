package com.example.multiplechoiceexam.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.multiplechoiceexam.Models.Account;
import com.example.multiplechoiceexam.Models.User;
import com.example.multiplechoiceexam.SharedPreferences.AccountSharedPreferences;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemoryData {
    public static void saveData(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("datata.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    // thời gian gửi tin nhắn
    public static void saveLastMessageTimestamp(String data,String chatId, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(chatId+".txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public static void saveAccessToken(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("nameee.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // phone
    public static String getAccessToken(Context context){
        String data ="";
        try {
            FileInputStream fileInputStream = context.openFileInput("nameee.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line ;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public static String getName(Context context){
        String data ="";
        try {
            FileInputStream fileInputStream = context.openFileInput("nameee.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line ;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public static String getLastMessageTimeStamp(Context context,String chatId){
        String data ="0";
        try {
            FileInputStream fileInputStream = context.openFileInput(chatId+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line ;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    // save account to local
    public static void saveNumberOfQuestion(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("numbrrr.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public static String getNumberOfQuestion(Context context){
        String data ="";
        try {
            FileInputStream fileInputStream = context.openFileInput("numbrrr.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line ;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }


    // save account to local
    public static void saveCurrentAccount(AccountSharedPreferences data, Context context) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("Accounttt.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AccountSharedPreferences getCurrentAccount(Context context) {
        Gson gson = new Gson();
        try {
            FileInputStream fileInputStream = context.openFileInput("Accounttt.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fileInputStream.close();
            String jsonString = stringBuilder.toString();
            return gson.fromJson(jsonString, AccountSharedPreferences.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // save bit map to local
    public static File saveImageToFile(Bitmap bitmap, String fileName, Context context) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


}