package com.elearning.elearning_support.dtos.test.testSet;


import java.util.Date;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSetResDTO {

    Long testSetId;

    Long testId;

    String testName;

    String subjectTitle;

    String subjectCode;

    Integer questionQuantity;

    Integer duration;

    String testSetCode;

    String semester;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date createdAt;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date modifiedAt;

    public TestSetResDTO(ITestSetResDTO iTestSetResDTO) {
        BeanUtils.copyProperties(iTestSetResDTO, this);
    }

}
