package com.elearning.elearning_support.enums.importFile;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionImportFieldMapping {

    CONTENT("Nội dung*", "content"),
    LEVEL("Mức độ*", "levelRaw"),
    CHAPTER_NO ("Số thứ tự chương*", "chapterNo"),
    SUBJECT("Mã học phần*", "subjectCode"),
    ANSWER1("Phương án 1*", "firstAnswer"),
    ANSWER2("Phương án 2*", "secondAnswer"),
    ANSWER3("Phương án 3*", "thirdAnswer"),
    ANSWER4("Phương án 4*", "fourthAnswer"),
    CORRECT_ANSWERS("Phương án đúng*", "correctAnswers");

    private final String excelColumnKey;

    private final String objectFieldKey;

    private final static Map<String, QuestionImportFieldMapping> mapFields = new LinkedHashMap<>();

    static {
        for (QuestionImportFieldMapping fieldMap : QuestionImportFieldMapping.values()){
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
