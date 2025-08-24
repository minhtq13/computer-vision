import "./TestSetCreate.scss";
import {Spin, Tabs} from "antd";
import TestSetCreateAuto from "./TestSetCreateAuto";
import { useLocation } from "react-router-dom";
import TestSetCreateManual from "./TestSetCreateManual";
import {useEffect, useState} from "react";
import TestDetailsEdit from "../../Test/TestEdit/TestDetailsEdit/TestDetailsEdit";
import useTest from "../../../hooks/useTest";

const TestSetCreate = () => {
  const location = useLocation();
  const {getTestDetails, testDetails} = useTest();
  const testId = location?.pathname?.split("/")[2];
  const [tabs, setTabs] = useState("auto");
  const [loading, setLoading] = useState(true);
  const items = [
    {
      key: "auto",
      label: <h3>Tạo đề tự động</h3>,
      children: <TestSetCreateAuto testId={testId} />,
    },
    {
      key: "manual",
      label: <h3>Tạo đề thủ công</h3>,
      children: (
        <TestSetCreateManual
          testId={testId}
          questionQuantity={testDetails?.questionQuantity}
          lstTest={testDetails?.lstTestSetCode}
        />
      ),
    },
    {
      key: "edit",
      label: <h3>Chỉnh sửa</h3>,
      children: (<TestDetailsEdit
          testId={testId}
          initialValues={
            {
              questionQuantity: testDetails?.questionQuantity,
              numEasyQuestion: testDetails?.generateConfig?.numEasyQuestion,
              numMediumQuestion: testDetails?.generateConfig?.numMediumQuestion,
              numHardQuestion: testDetails?.generateConfig?.numHardQuestion,
              duration: testDetails?.duration,
              totalPoint: testDetails?.totalPoint,
              subjectCode: testDetails?.subjectCode,
              subjectName: testDetails?.subjectName
            }}
      />)
    }
  ];

  // fetch test's detail
  useEffect(() => {
    getTestDetails({testId: testId});
    setLoading(false);
  }, [testId]);

  const handleChange = (e) => {
    setTabs(e);
  };
  const renderLevel = () => {
    if (tabs === "auto") {
      if (testDetails?.generateConfig) {
        return `(${testDetails?.generateConfig?.numEasyQuestion} dễ, 
          ${testDetails?.generateConfig?.numMediumQuestion} trung bình, ${testDetails?.generateConfig?.numHardQuestion} khó)`
      }
    } else return ""
  }
  return (
    loading ? <Spin tip="Đang tải" spinning={loading}/> : (
    <div className="test-set-create">
      <div className="test-set-header">Chi tiết kỳ thi</div>
      <div className="test-create-info">
        <div className="test-create-info-row">
          <span>Học phần:</span>
          <span>{testDetails?.subjectName} - {testDetails?.subjectCode}</span>
        </div>
        <div className="test-create-info-row">
          <span>Kỳ thi: </span>
          <span>{testDetails?.name} - {testDetails?.testType}</span>
        </div>
        <div className="test-create-info-row">
          <span>Học kỳ:</span>
          <span>{testDetails?.semester}</span>
        </div>
      </div>
      <div className="test-create-info">
        <div className="test-create-info-row">
          <span>Số câu hỏi:</span>
          <span>
            {testDetails?.questionQuantity}{" "}
            {renderLevel()}
          </span>
        </div>
        <div className="test-create-info-row">
          <span>Thời gian thi:</span>
          <span>{`${testDetails?.duration} phút`}</span>
        </div>
        <div className="test-create-info-row">
          <span>Bộ đề:</span>
          <span>
            {testDetails?.lstTestSetCode ? testDetails?.lstTestSetCode.split(",").join(", ") : ""}
          </span>
        </div>
      </div>
      <Tabs
        defaultActiveKey={tabs}
        items={items}
        className="test-content"
        onChange={handleChange}
      ></Tabs>
    </div>)
  );
};
export default TestSetCreate;
