export const NewTestHeader = ({ testDetail, testNo }) => {
  return (
    <div className="test-preview" style={{ display: "flex", fontSize: "14px", lineHeight: "1.5", fontFamily: "Times New Roman" }}>
      <div className="left" style={{ width: "50%" }}>
        <div className="first-left" style={{ border: "1px solid black", padding: "0px 8px", height: "200px" }}>
          <div style={{ display: "flex", flexDirection: "column", alignItems: "center", fontSize: "16px !important" }}>
            <div style={{ fontSize: "18px", fontWeight: "bold", marginTop: "4px", fontFamily: "Times New Roman" }}>ĐẠI HỌC BÁCH KHOA HÀ NỘI</div>
            <div style={{ fontSize: "16px", fontWeight: "bold", marginBottom: "12px", fontFamily: "Times New Roman" }}>
              {(testDetail?.departmentName ?? "").toUpperCase()}
            </div>
          </div>
          <div className="test-header-content-left">
            <div style={{ fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
              Thời gian làm bài:{" "}
              <span
                style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}
              >{`${testDetail?.duration} phút`}</span>
            </div>
            <div style={{ marginBottom: "12px", fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
              Mã đề: <span style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}>{`${testNo}`}</span>
            </div>
            <div
              style={{ marginBottom: "10px", fontFamily: "Times New Roman" }}
            >{`Họ và tên:.....................................................................`}</div>
            <div
              style={{ fontFamily: "Times New Roman" }}
            >{`MSSV:.............................................................................`}</div>
          </div>
        </div>
        <div className="second-left" style={{ display: "flex" }}>
          <div
            style={{
              width: "30%",
              display: "flex",
              justifyContent: "center",
              borderLeft: "1px solid black",
              borderRight: "1px solid black",
              borderBottom: "1px solid black",
              height: "60px",
            }}
          >
            <div className="text-bold" style={{ fontSize: "14px", fontWeight: "bold", fontFamily: "Times New Roman" }}>
              Ký duyệt
            </div>
          </div>
          <div
            style={{
              fontSize: "14px",
              width: "70%",
              borderRight: "1px solid black",
              borderBottom: "1px solid black",
              height: "60px",
              padding: "0px 8px",
              fontFamily: "Times New Roman",
            }}
          >
            CBGD phụ trách đề thi:
          </div>
        </div>
      </div>
      <div style={{ width: "50%" }}>
        <div
          style={{
            borderTop: "1px solid black",
            borderRight: "1px solid black",
            borderBottom: "1px solid black",
            padding: "0px 8px",
            height: "200px",
          }}
        >
          <div style={{ display: "flex", flexDirection: "column", alignItems: "center", marginBottom: "10px", fontFamily: "Times New Roman" }}>
            <div style={{ fontSize: "16px", fontWeight: "bold", fontFamily: "Times New Roman" }}>{`ĐỀ THI ${testDetail?.testName?.toUpperCase()} ${
              testDetail?.semester
            }`}</div>
          </div>
          <div style={{ marginBottom: "10px", fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
            Mã học phần:{" "}
            <span
              style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}
            >{`${testDetail.subjectCode}`}</span>
          </div>
          <div style={{ marginBottom: "10px", fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
            Tên học phần:{" "}
            <span
              style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}
            >{`${testDetail?.subjectTitle}`}</span>
          </div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>
            - Không sử dụng các thiết bị máy tính, điện thoại
          </div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>
            - {testDetail?.isAllowedUsingDocuments ? "Được sử dụng tài liệu giấy" : "Được sử dụng tài liệu giấy"}
          </div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>- Làm bài trên tờ giấy trắc nghiệm được phát</div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>- Nộp lại đề thi</div>
        </div>
        <div
          style={{
            borderRight: "1px solid black",
            borderBottom: "1px solid black",
            height: "60px",
            padding: "0px 8px",
            fontFamily: "Times New Roman",
          }}
        >
          Trưởng nhóm chuyên môn:
        </div>
      </div>
    </div>
  );
};

export default NewTestHeader;
