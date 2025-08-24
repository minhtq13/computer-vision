package com.example.multiplechoiceexam.Utils;

import java.util.List;
import java.util.Map;

public class WordFormFiller {

    public static String generateHtml(Map<String, Object> dataModel) {
        StringBuilder html = new StringBuilder();

        // Mở thẻ html
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Đề thi cuối kì</title>");
        html.append("</head>");
        html.append("<body>");

        // Thêm nội dung từ dataModel
        if (dataModel.containsKey("testSet")) {
            Map<String, Object> testSet = (Map<String, Object>) dataModel.get("testSet");
            html.append("<h2>").append(testSet.get("subjectTitle")).append("</h2>");
            html.append("<h3>").append(testSet.get("subjectCode")).append("</h3>");
            html.append("<p>Mã đề: ").append(testSet.get("testNo")).append("</p>");
            html.append("<p>Thời gian làm bài: ").append(testSet.get("duration")).append(" phút</p>");
            html.append("<p>Kì thi: ").append(testSet.get("testDay")).append("</p>");
        }

        // Thêm câu hỏi và câu trả lời
        if (dataModel.containsKey("questions")) {
            List<Map<String, Object>> questions = (List<Map<String, Object>>) dataModel.get("questions");
            for (Map<String, Object> question : questions) {
                html.append("<p>").append("Câu ").append(question.get("questionNo")).append(": ").append(question.get("topicText")).append("</p>");
                if (question.containsKey("answers")) {
                    List<Map<String, Object>> answers = (List<Map<String, Object>>) question.get("answers");
                    for (Map<String, Object> answer : answers) {
                        html.append("<p>").append(answer.get("answerNo")).append(". ").append(answer.get("content")).append("</p>");
                    }
                }
            }
        }

        // Thêm phần cuối template
        html.append("<p>(Cán bộ coi thi không được giải thích gì thêm)</p>");
        html.append("<p>HẾT</p>");
        html.append("<p>DUYỆT CỦA KHOA/BỘ MÔN</p>");
        html.append("<p>(Ký tên, ghi rõ họ tên) Hà Nội, ngày… tháng… năm…</p>");
        html.append("<p>GIẢNG VIÊN RA ĐỀ</p>");
        html.append("<p>(Ký tên, ghi rõ họ tên)</p>");
        html.append("<p>Lưu ý:</p>");
        html.append("<p>- Sử dụng khổ giấy A4;</p>");
        html.append("<p>- Phiếu trả lời trắc nghiệm theo mẫu của TTKT;</p>");
        html.append("<p>- Phải thể hiện số thứ tự trang nếu số trang lớn hơn 1;</p>");
        html.append("<p>- Thí sinh không được sử dụng tài liệu, mọi thắc mắc về đề thi vui lòng hỏi giám thị coi thi.</p>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

}
