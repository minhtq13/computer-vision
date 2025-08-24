<!DOCTYPE html>
<html>
<head>
    <title>Đề thi cuối kì</title>
</head>
<body>
    <h2>BỘ GIÁO DỤC VÀ ĐÀO TẠO</h2>
    <h3>ĐẠI HỌC BÁCH KHOA HÀ NỘI</h3>
    <h4>ĐỀ THI CUỐI KÌ</h4>

    <#if testSet??>
        <p>Hình thức tổ chức thi: Trắc nghiệm</p>
        <p>Mã học phần: ${testSet.subjectCode}</p>
        <p>Tên học phần: ${testSet.subjectTitle}</p>
        <p>Lớp thi: <!-- Cần thêm trường lớp thi vào TestSetResponse --></p>
        <p>Thời gian làm bài: ${testSet.duration} phút</p>
        <p>Mã đề : ${testSet.testNo}</p>
    </#if>

    <#list questions as question>
        <#if question??>
            <p>Câu ${question.questionNo}: ${question.topicText}</p>
            <#list question.answers as answer>
                <#if answer??>
                    <p>${answer.answerNo}: ${answer.content}</p>
                </#if>
            </#list>
        </#if>
    </#list>

    <p>(Cán bộ coi thi không được giải thích gì thêm)</p>
    <p>HẾT</p>

    <p>DUYỆT CỦA KHOA/BỘ MÔN</p>
    <p>(Ký tên, ghi rõ họ tên) Hà Nội, ngày… tháng… năm…</p>

    <p>GIẢNG VIÊN RA ĐỀ</p>
    <p>(Ký tên, ghi rõ họ tên)</p>

    <p>Lưu ý:</p>
    <p>- Sử dụng khổ giấy A4;</p>
    <p>- Phiếu trả lời trắc nghiệm theo mẫu của TTKT;</p>
    <p>- Phải thể hiện số thứ tự trang nếu số trang lớn hơn 1;</p>
    <p>- Thí sinh không được sử dụng tài liệu, mọi thắc mắc về đề thi vui lòng hỏi giám thị coi thi.</p>
</body>
</html>
