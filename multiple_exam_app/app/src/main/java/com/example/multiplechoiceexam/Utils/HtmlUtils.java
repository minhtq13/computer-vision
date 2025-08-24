package com.example.multiplechoiceexam.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;


public class HtmlUtils {

    public static void setHtmlTextWithImage(TextView textView, String htmlString) {
        Spanned spannedHtml = Html.fromHtml(htmlString, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                try {
                    if (source == null) {
                        return null;
                    }

                    byte[] data = Base64.decode(source.substring(source.indexOf(",") + 1), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    // Kiểm tra xem bitmap có null hay không
                    if (bitmap != null) {
                        // Scaling bitmap
                        bitmap = Bitmap.createScaledBitmap(bitmap, 600, 400, true);

                        // Tạo drawable từ bitmap
                        Drawable drawable = new BitmapDrawable(textView.getResources(), bitmap);
                        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());

                        return drawable;
                    } else {
                        Log.e("HtmlUtils", "Bitmap là null");
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }, null);

        textView.setText(spannedHtml);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String getTextFromHtml(String htmlString) {
        return Html.fromHtml(htmlString).toString();
    }

}
