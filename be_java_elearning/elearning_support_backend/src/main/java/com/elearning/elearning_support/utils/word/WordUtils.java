package com.elearning.elearning_support.utils.word;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.deepoove.poi.XWPFTemplate;
import com.elearning.elearning_support.constants.SystemConstants;
import com.elearning.elearning_support.dtos.test.testSet.TestSetDetailDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetResDTO;

public class WordUtils {

    public static ByteArrayInputStream exportTestToWord(TestSetDetailDTO content) throws IOException {
        try {
            XWPFDocument document = new XWPFDocument();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Mapping và render dữ liệu
            TestSetResDTO testSet = new TestSetResDTO(content.getTestSet());
            XWPFTemplate.compile(SystemConstants.RESOURCE_PATH + "/wordTemplate/TestSet_Template.docx").render(new HashMap<String, Object>() {{
                put("testSet", testSet);
                put("semester", testSet.getSemester());
                put("duration", testSet.getDuration());
                put("testSetCode", testSet.getTestSetCode());
                put("questions", content.getLstQuestion());
            }}).writeAndClose(outputStream);
            document.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

}
