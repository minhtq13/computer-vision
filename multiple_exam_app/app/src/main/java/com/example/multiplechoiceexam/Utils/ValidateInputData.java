package com.example.multiplechoiceexam.Utils;

import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateInputData {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean email(String email, View view) {
        EditText layoutInput  = (EditText) view;
        Matcher matcher = pattern.matcher(email);
        if(! matcher.matches()){
            layoutInput.setError("Kiểm tra lại email");
            return false;
        }
        return true;
    }
    public static boolean stringNotNull(String input, View view) {
        EditText layoutInput  = (EditText) view;
        if(input.isEmpty()){
            layoutInput.setError("Thông tin này không được bỏ trống");
            return false;
        }
        return true;
    }
    public static boolean isUnicode(String input) {
        String regex = "^[^\\s]*[áàảãạăắằẳẵặâấầẩẫậđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ][^\\s]*$";

        return input.trim().matches(regex);
    }
}