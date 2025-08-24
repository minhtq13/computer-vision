package com.elearning.elearning_support.dtos;

import org.springframework.core.io.InputStreamResource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomInputStreamResource {

    String fileName;

    InputStreamResource resource;

}
