export const TestHeaderEn = ({ testDetail, testNo }) => {
  return (
    <div className="test-preview" style={{ display: "flex", fontSize: "14px", lineHeight: "1.5", fontFamily: "Times New Roman" }}>
      <div className="left" style={{ width: "50%" }}>
        {/* Left Section - Top Box */}
        <div className="first-left" style={{ border: "1px solid black", padding: "0px 8px", height: "200px" }}>
          <div style={{ display: "flex", flexDirection: "column", alignItems: "center", fontSize: "16px !important" }}>
            <div style={{ fontSize: "16px", fontWeight: "bold", marginTop: "4px", fontFamily: "Times New Roman" }}>
              <div style={{ textAlign: "center", fontWeight: "bold", lineHeight: "1.2", marginBottom: "12px" }}>
                HANOI UNIVERSITY OF SCIENCE AND TECHNOLOGY
              </div>
            </div>
          </div>
          <div className="test-header-content-left">
            <div style={{ fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
              Duration:{" "}
              <span
                style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}
              >{`${testDetail?.duration} minutes`}</span>
            </div>
            <div style={{ marginBottom: "12px", fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
              Test Code: <span style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}>{`${testNo}`}</span>
            </div>
            <div
              style={{ marginBottom: "10px", fontFamily: "Times New Roman" }}
            >{`Fullname:.............................................................................`}</div>
            <div
              style={{ fontFamily: "Times New Roman" }}
            >{`Student ID:..........................................................................`}</div>
          </div>
        </div>
        {/* Left Section - Bottom Box */}
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
              Approved by
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
            Instructor in charge of exam:
          </div>
        </div>
      </div>
      {/* Right Section */}
      <div style={{ width: "50%" }}>
        {/* Right Section - Top Box */}
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
            <div style={{ fontSize: "16px", fontWeight: "bold", fontFamily: "Times New Roman" }}>{`EXAM FOR ${testDetail?.testName?.toUpperCase()} ${
              testDetail?.semester
            }`}</div>
          </div>
          <div style={{ marginBottom: "10px", fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
            Course Code:{" "}
            <span
              style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}
            >{`${testDetail.subjectCode}`}</span>
          </div>
          <div style={{ marginBottom: "10px", fontSize: "16px", display: "flex", alignItems: "center", fontFamily: "Times New Roman" }}>
            Course Name:{" "}
            <span
              style={{ fontWeight: "bold", marginLeft: "4px", fontSize: "16px", fontFamily: "Times New Roman" }}
            >{`${testDetail?.subjectTitle}`}</span>
          </div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>- Do not use computers or mobile phones.</div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>
            - {testDetail?.isAllowedUsingDocuments ? "Allowed to use paper documents." : "Not allowed to use paper documents."}
          </div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>- Answer on the provided multiple-choice sheet.</div>
          <div style={{ fontSize: "14px", fontStyle: "italic", fontFamily: "Times New Roman" }}>- Hand in the exam paper.</div>
        </div>
        {/* Right Section - Bottom Box */}
        <div
          style={{
            borderRight: "1px solid black",
            borderBottom: "1px solid black",
            height: "60px",
            padding: "0px 8px",
            fontFamily: "Times New Roman",
          }}
        >
          Head of Academic Department:
        </div>
      </div>
    </div>
  );
};

export default TestHeaderEn;
