import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import "./TestPreview.scss";
import NewTestHeader from "./NewTestHeader";
const TestPreview = ({ questions, testDetail, testNo }) => {
  return (
    <div className="test-preview" style={{ paddingTop: "5vh" }}>
      {/*<div className="test-top">*/}
      {/*	<div className="test-bgd">*/}
      {/*		<p className="text-bold">BỘ GIÁO DỤC VÀ ĐÀO TẠO</p>*/}
      {/*		<p className="text-bold">ĐẠI HỌC BÁCH KHOA HÀ NỘI</p>*/}
      {/*	</div>*/}
      {/*	<div className="test-semester">*/}
      {/*		<p className="text-bold text-uppercase">{`ĐỀ THI ${testDetail?.testName}`}</p>*/}
      {/*		<p className="text-bold">*/}
      {/*			{`HỌC KỲ: ${testDetail.semester}`}{" "}*/}
      {/*		</p>*/}
      {/*	</div>*/}
      {/*</div>*/}
      {/*<div className="test-header-content">*/}
      {/*	<div className="test-header-content-left">*/}
      {/*		<p>Hình thức tổ chức thi: Trắc nghiệm</p>*/}
      {/*		<p>{`Mã học phần: ${testDetail.subjectCode}`}</p>*/}
      {/*		<p>{`Tên học phần: ${testDetail.subjectTitle}`}</p>*/}
      {/*	</div>*/}
      {/*	<div className="test-header-content-right">*/}
      {/*		<p className="text-bold">{`Thời gian làm bài: ${testDetail.duration} phút`}</p>*/}
      {/*		<p className="non-ref font-italic">({`${testDetail?.isAllowedUsingDocuments ? "Được" : "Không"} sử dụng tài liệu`})</p>*/}
      {/*		<p className="text-bold">{`Mã đề: ${testNo}`}</p>*/}
      {/*	</div>*/}
      {/*</div>*/}
      <NewTestHeader testDetail={testDetail} testNo={testNo} />
      {/*gap*/}
      <div style={{ marginTop: "2vh", marginBottom: "2vh" }}></div>
      {questions.map((item, index) => {
        return (
          <div className="question-items" key={index}>
            <div className="question-topic">
              <span className="question-number">{`Câu ${index + 1}:`}</span>
              <ReactQuill
                key={index}
                value={item.content}
                readOnly={true} // Đặt readOnly để ngăn người dùng chỉnh sửa nội dung
                theme="snow"
                modules={{ toolbar: false }}
              />
            </div>
            {item.answers &&
              item.answers.map((ans, ansNo) => {
                return (
                  <div className={ans.isCorrect ? "answer-items corrected" : "answer-items"} key={`answer${ansNo}`}>
                    <span>{`${String.fromCharCode(65 + ansNo)}.`}</span>
                    <ReactQuill
                      key={ansNo}
                      value={ans.content}
                      readOnly={true} // Đặt readOnly để ngăn người dùng chỉnh sửa nội dung
                      theme="snow"
                      modules={{ toolbar: false }}
                    />
                  </div>
                );
              })}
          </div>
        );
      })}
      {/*COMMENT ON 08/07/2024 - unused footer*/}
      {/*<div className="test-footer">*/}
      {/*	<div className="test-end">*/}
      {/*		<p>(Cán bộ coi thi không giải thích gì thêm)</p>*/}
      {/*		<p className="text-bold">HẾT</p>*/}
      {/*	</div>*/}
      {/*	<div className="test-sig">*/}
      {/*		<div className="sig-left">*/}
      {/*			<p className="text-bold">DUYỆT CỦA KHOA/BỘ MÔN</p>*/}
      {/*			<p>(Ký tên, ghi rõ họ tên)</p>*/}
      {/*		</div>*/}
      {/*		<div className="sig-right">*/}
      {/*			<p>Hà Nội, ngày ..... tháng ..... năm ......</p>*/}
      {/*			<p className="text-bold">GIẢNG VIÊN RA ĐỀ</p>*/}
      {/*			<p>(Ký tên, ghi rõ họ tên)</p>*/}
      {/*		</div>*/}
      {/*	</div>*/}
      {/*	<div className="test-note">*/}
      {/*		<p className="text-bold text-note">Lưu ý:</p>*/}
      {/*		<p>{`-	Sử dụng khổ giấy A4;`}</p>*/}
      {/*		<p>{`-	Phiếu trả lời trắc nghiệm theo mẫu của TTKT;`}</p>*/}
      {/*		<p>{`-	Phải thể hiện số thứ tự trang nếu số trang lớn hơn 1;`}</p>*/}
      {/*		<p>{`-	Thí sinh không được sử dụng tài liệu, mọi thắc mắc về đề thi vui lòng hỏi giám thị coi thi.`}</p>*/}
      {/*	</div>*/}
      {/*</div>*/}
    </div>
  );
};
export default TestPreview;
