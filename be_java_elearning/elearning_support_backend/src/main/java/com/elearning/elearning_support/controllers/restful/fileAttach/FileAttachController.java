package com.elearning.elearning_support.controllers.restful.fileAttach;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.dtos.fileAttach.FileUploadResDTO;
import com.elearning.elearning_support.enums.fileAttach.FileTypeEnum;
import com.elearning.elearning_support.services.fileAttach.FileAttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/file-attach")
@Tag(name = "APIs Upload file")
@RequiredArgsConstructor
public class FileAttachController {

    private final FileAttachService fileAttachService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_SCORING_FILE_UPLOAD')")
    @PostMapping(value = "/upload-doc", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Tải lên file tài liệu")
    public FileUploadResDTO uploadDocFile(@RequestPart(name = "file") MultipartFile multipartFile) {
        return fileAttachService.uploadDocument(multipartFile, FileTypeEnum.DOCUMENT);
    }

//    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_SCORING_FILE_UPLOAD')")
    @PostMapping(value = "/upload-img", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Tải lên file ảnh")
    public FileUploadResDTO uploadImageFile(@RequestPart(name = "file") MultipartFile multipartFile) {
        return fileAttachService.uploadMPImageToCloudinary(multipartFile, FileTypeEnum.IMAGE);
    }

}
