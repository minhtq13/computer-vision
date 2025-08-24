package com.elearning.elearning_support.dtos.fileAttach;

import com.elearning.elearning_support.entities.fileAttach.FileAttach;
import com.elearning.elearning_support.enums.fileAttach.FileStoredTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadResDTO {

    Long id;
    String fileName;
    String filePath;
    Integer type;
    FileStoredTypeEnum storedTypeEnum;
    // file attach data in db
    FileAttach fileAttachDB;
}
