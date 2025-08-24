package com.elearning.elearning_support.utils.excelFile;

import org.apache.logging.log4j.util.Strings;

public class ExcelValidationUtils {

    public static final String REGEX_EMAIL = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9]{2,}(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+$";

    public static final String REGEX_PHONE = "^([+]|[0-9]{3})[0-9]{0,11}$|^$";

    public static final String REGEX_USER_POSITION = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]{0,50}$";

    public static final String REGEX_USER_LAST_FIRST_NAME = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]{0,20}$";

    public static String validateLastName(String lastName){
        if (Strings.isBlank(lastName)) {
            return "Thiếu trường thông tin Họ; ";
        } else if (lastName.length() > 20) {
            return "Định dạng của Họ không hợp lệ; ";
        } else {
            return null;
        }
    }

    public static String validateFirstName(String firstName){
        if (Strings.isBlank(firstName)) {
            return "Thiếu trường thông tin Tên; ";
        } else if (firstName.length() > 20) {
            return "Định dạng của Tên không hợp lệ; ";
        } else {
            return null;
        }
    }

    public static String validateEmail(String email) {
        if (Strings.isBlank(email)) {
            return "Thiếu trường thông tin Email; ";
        } else if (!email.matches(REGEX_EMAIL)) {
            return "Sai định dạng Email; ";
        }else if (email.length() > 100) {
            return "Độ dài của Email không hợp lệ; ";
        } else {
            return null;
        }
    }

    public static String validatePhoneNumber(String phoneNumber){
        if (phoneNumber != null && !phoneNumber.matches(REGEX_PHONE)) {
            return "Sai định dạng SĐT; ";
        }
        return null;
    }


    public static String validateAddress(String address){
        if (address != null && address.length() > 250) {
            return "Độ dài Địa chỉ nơi ở không hợp lệ; ";
        }
        return null;
    }


    public static String validateUserName(String name) {
        if (name != null && name.length() > 500) {
            return "Độ dài họ tên không hợp lệ; ";
        }
        return null;
    }

}
