package com.example.multiplechoiceexam.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

public class DateUtils {


    public static String generateRandomDateTime() {
        Random random = new Random();

        // Tạo ngẫu nhiên năm trong khoảng từ 2020 đến 2023
        int year = random.nextInt(4) + 2020;

        // Tạo ngẫu nhiên tháng trong khoảng từ 1 đến 12
        int month = random.nextInt(12) + 1;

        // Tạo ngẫu nhiên ngày trong khoảng từ 1 đến 28 (giả sử tháng có 28 ngày)
        int day = random.nextInt(28) + 1;

        // Tạo ngẫu nhiên giờ trong khoảng từ 0 đến 23
        int hour = random.nextInt(24);

        // Tạo ngẫu nhiên phút trong khoảng từ 0 đến 59
        int minute = random.nextInt(60);

        // Tạo một đối tượng Calendar và đặt các giá trị ngày, giờ, phút
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute);

        // Format đối tượng Calendar thành chuỗi ngày giờ
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
        return sdf.format(calendar.getTime());
    }
    public static String formatDateTime(String inputDateTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(inputDateTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDateTime; // Trả về nguyên bản nếu có lỗi chuyển đổi
        }
    }
    public static void main(String[] args) {
        String randomDateTime = generateRandomDateTime();
        System.out.println("Random DateTime: " + randomDateTime);
    }
}

