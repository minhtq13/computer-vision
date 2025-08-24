import { Button, Checkbox, Input, Select, Spin, Tag } from "antd";
import debounce from "lodash.debounce";
import { useEffect, useState } from "react";
import useQuestions from "../../../hooks/useQuestion";
import ReactQuill from "react-quill";
import useNotify from "../../../hooks/useNotify";
import { createTestSetService } from "../../../services/testServices";
import { levelOptions, searchTimeDebounce } from "../../../utils/constant";
import { useNavigate } from "react-router-dom";
import {CheckCircleFilled, PlusOutlined, WarningFilled} from "@ant-design/icons";
import { renderTag, tagRender } from "../../../utils/tools";

const TestSetCreateManual = ({ testId, questionQuantity, lstTest }) => {
  const arrTests = lstTest ? lstTest.split(",") : [];
  const navigate = useNavigate();
  const initialParam = {
    subjectId: null,
    subjectCode: null,
    chapterCode: null,
    chapterIds: [],
    search: null,
    level: "ALL",
    testId: testId
  };

  const onSearch = (value, _e, info) => {
    setParam({ ...param, search: value })
  };
  const onChange = debounce((_e) => {
    setParam({ ...param, search: _e.target.value })
  }, searchTimeDebounce)
  const { getAllQuestions, quesLoading, allQuestions } = useQuestions();
  const [param, setParam] = useState(initialParam);
  const [code, setCode] = useState(null);
  const [loading, setLoading] = useState(false);
  const [checkedItems, setCheckedItems] = useState([]);
  const [errCode, setErrCode] = useState(false);
  const [errorQuantity, setErrorQuantity] = useState(false);
  const [isExist, setIsExist] = useState(false);
  const notify = useNotify();
  useEffect(() => {
    getAllQuestions(param);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [param]);
  const levelOnchange = (option) => {
    setParam({ ...param, level: option })
  }
  const onCheck = (checkValues, item) => {
    let result = [...checkedItems];
    if (checkValues.target.checked) {
      result.push(item);
    } else {
      result = result.filter(existingItem => existingItem.id !== item.id);
    };
    if (result.length !== Number(questionQuantity)) {
      setErrorQuantity(true);
    } else {
      setErrorQuantity(false);
    }
    setCheckedItems(result);
  }
  const questionRender = (item, index, isPreview) => {
    return (
      <div className="question-items" key={index}>
        <div className="topic-level">
          <div className="question-topic">
            {isPreview && <div className="question-number">{`Câu ${index + 1
              }: `}</div>}
            <ReactQuill
              key={index}
              value={item.content}
              readOnly={true}
              theme="snow"
              modules={{ toolbar: false }}
            />
          </div>
          <Tag
            color={tagRender(item.level)}>
            {renderTag(item)}
          </Tag>
        </div>
        {item.lstAnswer &&
          item.lstAnswer.length > 0 &&
          item.lstAnswer.map((ans, ansNo) => {
            return (
              <div
                className={
                  ans.isCorrect
                    ? "answer-items corrected"
                    : "answer-items"
                }
                key={`answer${ansNo}`}
              >
                <span>{`${String.fromCharCode(
                  65 + ansNo
                )}. `}</span>
                <ReactQuill
                  key={ansNo}
                  value={ans.content}
                  readOnly={true}
                  theme="snow"
                  modules={{ toolbar: false }}
                />
              </div>
            );
          })}
      </div>
    )
  }
  const onCreate = () => {
    if (!code) {
      setErrCode(true);
    }
    if (checkedItems.length !== questionQuantity) {
      setErrorQuantity(true);
    }
    if (code && checkedItems.length === Number(questionQuantity) && !isExist) {
      setLoading(true);
      createTestSetService(
          {
            testSetCode: code,
            testId: Number(testId),
            questions: checkedItems.map((ques, quesIndex) => {
              return {
                questionId: ques.id,
                questionNo: quesIndex + 1,
                answers: ques.lstAnswer.map((ans, ansIndex) => {
                  return {
                    answerId: ans.id,
                    answerNo: ansIndex + 1
                  }
                })
              }
            })
          },
          (res) => {
            setLoading(false);
            notify.success(`Bạn đã tạo thành công mã đề thi ${code}`);
            navigate(`/test-list`)
          },
          (error) => {
            setLoading(false);
            notify.error(`Lỗi tạo  mã đề thi ${code}`)
          }
      ).then(() => {})
    }
  }
  const handleChangeTestSetCode = (e) => {
    setCode(e.target.value);
    setErrCode(e.target.value.trim() === "");
    setIsExist(e.target.value !== "" && arrTests.includes(e.target.value));
  }
  return (
    <div className="test-set-create-manual">
      <div className="manual-content">
        <div className="manual-fill">
          <div className="search-level">
            <div className="list-search">
              <span className="list-search-filter-label">Tìm kiếm:</span>
              <Input.Search placeholder="Nhập nội dung câu hỏi" enterButton onSearch={onSearch} allowClear onChange={onChange} />
            </div>
            <div className="test-level">
              <span className="list-search-filter-label">Mức độ:</span>
              <Select
                className="select-level-q"
                defaultValue={"ALL"}
                optionLabelProp="label"
                options={levelOptions}
                onChange={levelOnchange}
              />
            </div>
          </div>
          <div className="header-test-questions">
            <p className="text-bold font-weight-bold justify-content-center pt-4">NGÂN HÀNG CÂU HỎI ĐƯỢC SỬ DỤNG ({allQuestions.length} câu):</p>
          </div>
          <Spin spinning={quesLoading} tip="Đang tải">
            {
              allQuestions.map((item, index) => (
                <div className="question-items" key={index}>
                  <div className="topic-level">
                    <div className="question-topic">
                      <Checkbox onChange={(e) => onCheck(e, item)} checked={checkedItems.some(i => i.id === item.id)} />
                      <div className="question-number">{`Câu ${index + 1}: `}</div>
                      <ReactQuill
                        key={index}
                        value={item.content}
                        readOnly={true}
                        theme="snow"
                        modules={{ toolbar: false }}
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
                          modules={{ toolbar: false }}
                        />
                      </div>
                    ))}
                </div>
              ))
            }
          </Spin>
        </div>
        <div className="manual-preview">
          {isExist && <span className="error-code">{`Mã đề thi đã tồn tại!`}</span>}
          {errCode && <span className="error-code">Vui lòng nhập mã đề thi!</span>}
          <div className="manual-preview-code">
            <span className="manual-preview-code-label" style={{ fontSize: 16 }}>Mã đề thi:</span>
            <div className="manual-preview-code-value">
              <Input style={{ width: 175 }} showCount maxLength={3} onChange={handleChangeTestSetCode} placeholder="Nhập mã đề thi" status={errCode ? "error" : ""} />
            </div>
            <div className={errorQuantity ? "error-quantity" : "correct-quantity"}>{`Đã chọn ${checkedItems.length}/${questionQuantity} câu hỏi`}</div>
            {errorQuantity ? <WarningFilled style={{ color: "red" }} /> : <CheckCircleFilled style={{ color: "#03787c" }} />}
          </div>
          <div className="header-preview-generated-test-set">
            <p className="text-bold font-weight-bold justify-content-center pt-4">XEM TRƯỚC ĐỀ THI:</p>
          </div>
          <div className={isExist || errCode ? "manual-preview-content error" : "manual-preview-content"}>
            {checkedItems.length > 0 ? checkedItems.map((item, index) => {
              return questionRender(item, index, true);
            }) : <div className="preview-noti">Vui lòng chọn câu hỏi và xem trước đề thi ở đây!</div>}
          </div>
        </div>
      </div>
      <div className="btn-save-manual">
        <Button
            type="primary"
            style={{ minWidth: 100, height: 40, marginTop: 64}}
            onClick={onCreate}
            loading={loading}
            icon={<PlusOutlined/>}
        >Tạo đề</Button>
      </div>
    </div>
  )
}
export default TestSetCreateManual;
