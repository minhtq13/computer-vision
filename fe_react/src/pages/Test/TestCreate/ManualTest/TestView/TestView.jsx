import React, { useEffect, useState } from "react";
import "./TestView.scss";
import { Checkbox, Button, Modal, Tag, Spin } from "antd";
import { useNavigate } from "react-router-dom";
import { testService } from "../../../../../services/testServices";
import dayjs from "dayjs";
import useNotify from "../../../../../hooks/useNotify";
import { appPath } from "../../../../../config/appPath";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { setDetailTest } from "../../../../../utils/storage";
import { renderTag, tagRender } from "../../../../../utils/tools";
import ScrollToTop from "../../../../../components/ScrollToTop/ScrollToTop";

const TestView = ({
      questionList,
      endTime,
      startTime,
      duration,
      name,
      subjectId,
      semesterId,
      generateConfig,
      subjectOptions,
      semesterOptions,
      quesLoading,
      onSelectConfigLevelQuestion,
      levelQuestion,
      filter,
      testType,
      isAllowedUsingDocuments
}) => {
  const [checkedItems, setCheckedItems] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [testId, setTestId] = useState(null);
  const [isCheckAll, setIsCheckAll] = useState(false);
  const getLevelCounts = (arr) => {
    let levelCount = { 0: 0, 1: 0, 2: 0 };

    arr.forEach(item => {
      levelCount[item.level]++;
    });
    return levelCount;
  };
  useEffect(() => {
    setIsCheckAll(false);
  }, [questionList]);
  const navigate = useNavigate();
  const notify = useNotify();
  const onCreate = () => {
    setLoading(true);
    testService(
        {
            subjectId: subjectId,
            name: name,
            startTime: dayjs(startTime).format("DD/MM/YYYY HH:mm"),
            endTime: dayjs(endTime).format("DD/MM/YYYY HH:mm"),
            duration: Number(duration),
            totalPoint: 10,
            questionIds: checkedItems.map((item) => item.id),
            semesterId: semesterId,
            generateConfig: generateConfig,
            questionQuantity: Number(generateConfig.numTotalQuestion),
            testType: testType,
            isAllowedUsingDocuments: isAllowedUsingDocuments
        },
        (res) => {
            setOpenModal(true);
            setLoading(false);
            setTestId(res.data);
        },
        () => {
            setLoading(false );
            notify.error("Lỗi tạo bộ đề thi");
        }
    ).then(() => {});
  };

  const onChange = (checkValues, item) => {
    let result = [...checkedItems];
    if (checkValues.target.checked) {
      result.push(item);
    } else {
      result = result.filter(existingItem => existingItem.id !== item.id);
    }
    if (result.length === questionList.length) {
      setIsCheckAll(true);
    } else {
      setIsCheckAll(false);
    }
    setCheckedItems(result);
    onSelectConfigLevelQuestion(getLevelCounts(result));
  };
  const selectAllOnchange = (e) => {
    if (e.target.checked) {
      setCheckedItems(questionList);
      onSelectConfigLevelQuestion(getLevelCounts(questionList));
    } else {
      onSelectConfigLevelQuestion({ 0: 0, 1: 0, 2: 0 });
      setCheckedItems([])
    }
    setIsCheckAll(e.target.checked);
  }
  return (
    <div className="test-view">
      <div className="test-wrap">
        <div className="guide-text">
          Chọn bộ câu hỏi dưới đây để sử dụng cho kỳ thi ({questionList.length} câu):
        </div>
        <div className="number-ques">
          <div className="number-ques-item">{`Dễ: ${levelQuestion[0]}`}</div>
          <div className="number-ques-item">{`Trung bình: ${levelQuestion[1]}`}</div>
          <div className="number-ques-item">{`Khó: ${levelQuestion[2]}`}</div>
          <div className="number-ques-item">{`Tổng: ${levelQuestion[0] + levelQuestion[1] + levelQuestion[2]}`}</div>
        </div>
        <ScrollToTop />
        {/*Render question list*/}
        {subjectId &&
            <Spin spinning={quesLoading} tip="Đang tải">
                {questionList.length > 0 && (filter.search === '' && filter.level === "ALL") ?
                    <div className="question-select-all">
                        <Checkbox onChange={(e) => selectAllOnchange(e)} checked={isCheckAll}/>
                        <div className="select-all">Chọn tất cả</div>
                    </div> : ""}
                {
                    questionList.map((item, index) => (
                        <div className="question-items" key={index}>
                            <div className="topic-level">
                                <div className="question-topic">
                                    <Checkbox onChange={(e) => onChange(e, item)}
                                              checked={checkedItems.some(i => i.id === item.id)}/>
                                    <div className="question-number">{`Câu ${index + 1}: `}</div>
                                    <ReactQuill
                                        key={index}
                                        value={item.content}
                                        readOnly={true}
                                        theme="snow"
                                        modules={{toolbar: false}}
                                    />
                                </div>
                                <Tag color={tagRender(item.level)}>
                                    {renderTag(item)}
                                </Tag>
                            </div>
                            {item.lstAnswer &&
                                item.lstAnswer.length > 0 &&
                                item.lstAnswer.map((ans, ansNo) => (
                                    <div
                                        className={
                                            ans.isCorrect
                                                ? "answer-items corrected"
                                                : "answer-items"
                                        }
                                        key={`answer${ansNo}`}
                                    >
                                        <span>{`${String.fromCharCode(65 + ansNo)}. `}</span>
                                        <ReactQuill
                                            key={ansNo}
                                            value={ans.content}
                                            readOnly={true}
                                            theme="snow"
                                            modules={{toolbar: false}}
                                        />
                                    </div>
                                ))}
                        </div>
                    ))
                }
            </Spin>
        }
      </div>
      <Button
        loading={loading}
        type="primary"
        style={{ width: 120, height: 46}}
        onClick={onCreate}
      >
        Tạo kỳ thi
      </Button>
      <Modal
        className="test-set-create-modal"
        open={openModal}
        title="Tạo kỳ thi thành công!"
        onOk={() => {
          navigate(`${appPath.testSetCreate}/${testId}`);
          setDetailTest({
            duration: duration,
            questionQuantity: generateConfig.numTotalQuestion,
            subjectName: subjectOptions && subjectOptions.length > 0 ? (subjectOptions.find(item => item.value === subjectId) || {}).label : null,
            semester: semesterOptions ? semesterOptions.find(item => item.value === semesterId).label : null,
            generateConfig: generateConfig
          })
        }
        }
        onCancel={() => setOpenModal(false)}
      >
        <p>Bạn có muốn tạo tập đề thi không?</p>
      </Modal>
    </div>
  );
};
export default TestView;
