package com.elearning.elearning_support.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.constants.CharacterConstants;
import com.elearning.elearning_support.constants.FileConstants;
import com.elearning.elearning_support.constants.FileConstants.Extension;
import com.elearning.elearning_support.constants.FileConstants.Extension.Video;
import com.elearning.elearning_support.constants.SystemConstants;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.FileAttach;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.exceptions.FileUploadException;
import com.elearning.elearning_support.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

    private final String IMAGES_RESOURCE_URL_PATH = "/resources/upload/files/images/";

    public static final String IMAGES_STORED_LOCATION = "/upload/files/images/";

    public static final String DOCUMENTS_RESOURCE_URL_PATH = "/resources/upload/files/docs/";

    public static final String DOCUMENTS_STORED_LOCATION = "/upload/files/docs/";

    public static final Integer MAX_UPDATE_FILE_SIZE = 100 * 1024 * 1024; // 100MB

    public static final Integer MAX_FILENAME_LENGTH = 256; // 256 characters including the file's ext

    public static final Integer AVATAR_FILE_TYPE = 0;

    public static final Integer IMAGES_FILE_TYPE = 1;

    public static final Integer DOCUMENT_FILE_TYPE = 2;

    public static final Integer VIDEOS_FILE_TYPE = 3;

    public static File covertMultipartToFile(String tempPath, MultipartFile multipartFile) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.FORMAT_DATE_YYYY_MMDD_HHMMSS);
        try {
            if (Objects.isNull(multipartFile) || Objects.isNull(multipartFile.getOriginalFilename())) {
                return null;
            }
            String orgFileName = multipartFile.getOriginalFilename().replace(" ", "_");
            File convertedFile = new File(
                tempPath + getFileBodyName(orgFileName) + "_" + formatter.format(new Date()) + "." + getFileExt(orgFileName));
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            return convertedFile;
        } catch (Exception e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
            return null;
        }
    }


    /**
     * Get extension index = last index of '.'
     */
    public static String getFileExt(File file) {
        if (file.exists() && file.isFile()) {
            int extIndex = file.getName().lastIndexOf('.');
            return file.getName().substring(extIndex + 1);
        }
        return "";
    }

    /**
     * Get the file's extension from fileName
     */
    public static String getFileExt(String fileName) {
        if (ObjectUtils.isEmpty(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * Get the file's name without extension (arg = file)
     */
    public static String getFileBodyName(File file) {
        if (file.exists() && file.isFile()) {
            int extIndex = file.getName().lastIndexOf('.');
            return file.getName().substring(0, extIndex);
        }
        return "";
    }

    /**
     * Get file's name without extension (arg = fileName)
     */
    public static String getFileBodyName(String fileName) {
        if (ObjectUtils.isEmpty(fileName)) {
            return CharacterConstants.BLANK;
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static Integer getFileType(String fileExtension) {
        int type;
        switch (fileExtension) {
            case FileConstants.Extension.Image.GIF:
            case Extension.Image.JPG:
            case Extension.Image.JPEG:
            case Extension.Image.PNG:
            case Extension.Image.WEBP:
            case Extension.Image.TIFF:
            case Extension.Image.JFIF:
                type = IMAGES_FILE_TYPE;
                break;
            case Video.MKV:
            case Video.FLV:
            case Video.AVI:
            case Video.MP4:
            case Video.MOV:
            case Video.WMV:
            case Video.VOB:
                type = VIDEOS_FILE_TYPE;
                break;
            default:
                type = -1;
                break;
        }
        return type;
    }

    /**
     * Validate fileSize and fileExt
     */
    public static void validateUploadFile(MultipartFile file, List<String> targetExtensions) {
        // Validate file null
        if (Objects.isNull(file)) {
            throw new FileUploadException(FileAttach.UPLOAD_ERROR_CODE, Resources.FILE_ATTACHED, MessageConst.UPLOAD_FAILED);
        }

        // Validate fileSize
        if (file.getSize() > MAX_UPDATE_FILE_SIZE) {
            throw new FileUploadException(FileAttach.FILE_EXCESS_SIZE_ERROR_CODE, Resources.FILE_ATTACHED, MessageConst.UPLOAD_FAILED);
        }

        // Validate fileName length
        if (Objects.requireNonNull(file.getOriginalFilename()).length() > MAX_FILENAME_LENGTH) {
            throw new FileUploadException(FileAttach.FILE_EXCESS_FILENAME_LENGTH_ERROR_CODE, Resources.FILE_ATTACHED,
                MessageConst.UPLOAD_FAILED);
        }

        // Validate fileExt
        if (!targetExtensions.contains(FilenameUtils.getExtension(file.getOriginalFilename()))) {
            throw new FileUploadException(FileAttach.FILE_INVALID_EXTENSION_ERROR_CODE, Resources.FILE_ATTACHED, MessageConst.UPLOAD_FAILED);
        }
    }

    public static class Excel {

        public static final String[] EXTENSIONS = new String[]{
            "xlsx", "xls"
        };
        public static final String[] CONTENT_TYPES = new String[]{
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"
        };
    }

    public static class Word {

        public static final String[] EXTENSIONS = new String[]{
            "docx", "doc"
        };
        public static final String[] CONTENT_TYPES = new String[]{
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"
        };
    }

    public static String getSharedAppDirectoryDataPath() {
        String sharedAppDataPath;
        if (SystemConstants.IS_WINDOWS) {
            sharedAppDataPath = SystemConstants.WINDOWS_SHARED_DIR + "/data";
            log.info("Windows's shared app data {}", sharedAppDataPath);
        } else {
            sharedAppDataPath = SystemConstants.LINUX_SHARED_DIR + "/data";
            log.info("Linux's shared app data {}", sharedAppDataPath);
        }
        return sharedAppDataPath;
    }

    public static String getSharedAppDirectoryPath() {
        String sharedAppSourcePath;
        if (SystemConstants.IS_WINDOWS) {
            sharedAppSourcePath = SystemConstants.WINDOWS_SHARED_DIR;
            log.info("Windows's shared app source {}", sharedAppSourcePath);
        } else {
            sharedAppSourcePath = SystemConstants.LINUX_SHARED_DIR;
            log.info("Linux's shared app source {}", sharedAppSourcePath);
        }
        return sharedAppSourcePath;
    }

    /**
     * copy an input stream to file
     */
    public static File copyInputStreamToFile(String filePath, InputStream inputStream) {
        try {
            File file = new File(filePath);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
            return file;
        } catch (IOException e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
            return null;
        }
    }

    /**
     * create a directory from a path
     */
    public static void createDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException ioException) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ioException.getMessage(), ioException.getCause(), ioException);
        }
    }


}
