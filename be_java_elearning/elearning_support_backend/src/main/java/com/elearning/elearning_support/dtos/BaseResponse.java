package com.elearning.elearning_support.dtos;

import java.util.Date;
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
public class BaseResponse {

    Integer code;

    String message;

    Date timestamp;

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = new Date();
    }
}
