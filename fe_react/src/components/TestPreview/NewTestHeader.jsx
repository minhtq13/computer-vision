export const NewTestHeader = ({ testDetail, testNo }) => {
  // Đối tượng JavaScript đại diện cho các thuộc tính CSS
  const testBgd = {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  };

  const semester = {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    marginBottom: "10px",
  };

  const textBold = {
    fontWeight: "bold",
  };

  const fontItalic = {
    fontStyle: "italic",
  };

  const textBoldMarginBottom = {
    fontWeight: "bold",
    marginBottom: "12px",
  };

  const bottomBold = {
    fontWeight: "bold",
    marginBottom: "10px",
  };

  const allBorder = {
    border: "1px solid black",
    padding: "0px 8px",
    height: "200px",
  };

  const borderLeftSide = {
    borderTop: "1px solid black",
    borderRight: "1px solid black",
    borderBottom: "1px solid black",
    padding: "0px 8px",
    height: "200px",
  };

  const sign = {
    width: "30%",
    display: "flex",
    justifyContent: "center",
    borderLeft: "1px solid black",
    borderRight: "1px solid black",
    borderBottom: "1px solid black",
    height: "60px",
  };
  const response = {
    width: "70%",
    borderRight: "1px solid black",
    borderBottom: "1px solid black",
    height: "60px",
    padding: "0px 8px",
  };
  const lead = {
    borderRight: "1px solid black",
    borderBottom: "1px solid black",
    height: "60px",
    padding: "0px 8px",
  };
  return (
    <div className="test-preview" style={{ display: "flex" }}>
      <div className="left" style={{ width: "50%" }}>
        <div className="first-left" style={allBorder}>
          <div className="test-bgd" style={testBgd}>
            <div>ĐẠI HỌC BÁCH KHOA HÀ NỘI</div>
            <div className="text-bold" style={textBoldMarginBottom}>
              {(testDetail?.departmentName ?? "").toUpperCase()}
            </div>
          </div>
          <div className="test-header-content-left">
            <div style={bottomBold}>{`Thời gian làm bài: ${testDetail?.duration} phút`}</div>
            <div style={bottomBold}>{`Mã đề: ${testNo}`}</div>
            <div>{`MSSV:........................`}</div>
            <div>{`Họ tên sinh viên:........................`}</div>
          </div>
        </div>
        <div className="second-left" style={{ display: "flex" }}>
          <div style={sign}>
            <div className="text-bold" style={textBold}>
              Ký duyệt
            </div>
          </div>
          <div style={response}>CBGD phụ trách đề thi:</div>
        </div>
      </div>
      <div className="right" style={{ width: "50%" }}>
        <div className="first-right" style={borderLeftSide}>
          <div className="test-semester" style={semester}>
            <div className="text-bold" style={textBold}>
              {`ĐỀ THI ${testDetail?.testName?.toUpperCase()} ${testDetail?.semester}`}
            </div>
          </div>
          <div style={bottomBold}>{`Mã học phần: ${testDetail.subjectCode}`}</div>
          <div className="text-bold" style={bottomBold}>{`Tên học phần: ${testDetail?.subjectTitle}`}</div>
          <div style={fontItalic}>- Không sử dụng các thiết bị máy tính, điện thoại</div>
          <div style={fontItalic}>- {testDetail?.isAllowedUsingDocuments ? "Được sử dụng tài liệu giấy" : "Được sử dụng tài liệu giấy"}</div>
          <div style={fontItalic}>- Làm bài trên tờ giấy trắc nghiệm được phát</div>
          <div style={fontItalic}>- Nộp lại đề thi</div>
        </div>
        <div className="second-right" style={lead}>
          Trưởng nhóm chuyên môn:
        </div>
      </div>
    </div>
  );
};

export default NewTestHeader;
