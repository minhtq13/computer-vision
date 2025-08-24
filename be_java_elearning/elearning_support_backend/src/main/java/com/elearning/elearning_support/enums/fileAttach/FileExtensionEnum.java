package com.elearning.elearning_support.enums.fileAttach;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileExtensionEnum {

    // File image
    JPG("jpg", FileTypeEnum.IMAGE),
    PNG("png", FileTypeEnum.IMAGE),
    // File doc
    DOC("doc", FileTypeEnum.DOCUMENT),
    DOCX("docx", FileTypeEnum.DOCUMENT),
    XLS("xls", FileTypeEnum.DOCUMENT),
    XLSX("xlsx", FileTypeEnum.DOCUMENT),
    PDF("pdf", FileTypeEnum.DOCUMENT);

    private final String fileExt;

    private FileTypeEnum fileType;

    private static Map<String, FileExtensionEnum> mapFileExt = new HashMap<>();

    static {
        for (FileExtensionEnum ext : FileExtensionEnum.values()) {
            mapFileExt.put(ext.fileExt, ext);
        }
    }

    public static FileExtensionEnum getFileExtOf(String fileExt) {
        return mapFileExt.get(fileExt);
    }
}

