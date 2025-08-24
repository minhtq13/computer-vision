package com.elearning.elearning_support.enums.importFile;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudentImportFieldMapping {

    USERNAME("Tên đăng nhập", "username"),
    EMAIL("Email*", "email"),
    PASSWORD("Mật khẩu", "passwordRaw"),
    FULL_NAME("Họ và tên*", "fullNameRaw"),
    BIRTHDAY("Ngày sinh*", "birthDateRaw"),
    GENDER("Giới tính*", "genderRaw"),
    PHONE("Số điện thoại", "phoneNumber"),
    CODE("MSSV*", "code"),
    COURSE("Khóa*", "courseRaw"),
    DEPARTMENT("Mã đơn vị quản lý*", "departmentCode");

    private final String excelColumnKey;

    private final String objectFieldKey;

    private final static Map<String, StudentImportFieldMapping> mapFields = new LinkedHashMap<>();

    static {
        for (StudentImportFieldMapping fieldMap : StudentImportFieldMapping.values()){
            mapFields.put(fieldMap.excelColumnKey, fieldMap);
        }
    }

    /**
     * Lấy object keymap khi import
     */
    public static String getObjectFieldByColumnKey(String columnKey) {
        return Objects.requireNonNull(mapFields.get(columnKey)).getObjectFieldKey();
    }
}
