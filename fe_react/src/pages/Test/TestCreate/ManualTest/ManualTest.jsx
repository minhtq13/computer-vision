import { DatePicker, Input, Select } from "antd";
import debounce from "lodash.debounce";
import { useEffect, useState } from "react";
import useCombo from "../../../../hooks/useCombo";
import { levelOptions, searchTimeDebounce } from "../../../../utils/constant";
import "./ManualTest.scss";
import TestView from "./TestView/TestView";
import { regexPattern } from "../../../../common/regexPattern";

const ManualTest = ({ questionList, quesLoading, subjectId, subjectOptions, onSelectLevel, onChangeSearch, levelQuestions }) => {
  const [startTime, setStartTime] = useState(null);
  const [endTime, setEndTime] = useState(null);
  const [name, setName] = useState("");
  const [duration, setDuration] = useState(null);
  const [questionQuantity, setQuestionQuantity] = useState(null);
  const [easyNumber, setEasyNumber] = useState(null);
  const [mediumNumber, setMediumNumber] = useState(null);
  const [testType, setTestType] = useState(null);
  const [hardNumber, setHardNumber] = useState(null);
  // eslint-disable-next-line no-unused-vars
  const [semesterId, setSemesterId] = useState(null);
  const [config, setConfig] = useState({ 0: 0, 1: 0, 2: 0 });
  const [filter, setFilter] = useState({ level: "ALL", search: "" });
  const { allSemester, semesterLoading, getAllSemesters } = useCombo();
  useEffect(() => {
    getAllSemesters({ search: "" });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const errorMessage = "Không đủ số lượng câu hỏi trong ngân hàng!";
  const options =
    allSemester && allSemester.length > 0
      ? allSemester.map((item) => {
          return { value: item.id, label: item.name };
        })
      : [];
  const levelOnchange = (value) => {
    setFilter({ ...filter, level: value });
    onSelectLevel(value);
  };

  // Option chọn hình thức thi testType
  const testTypeOptions = [
    {
      value: "OFFLINE",
      label: "Offline",
    },
    {
      value: "ONLINE",
      label: "Online",
    },
  ];

  const onSearch = (value, _e) => {
    onChangeSearch(value);
  };
  const onChange = debounce((_e) => {
    setFilter({ ...filter, search: _e.target.value });
    onChangeSearch(_e.target.value);
  }, searchTimeDebounce);
  return (
    <div className="manual-test">
      <div className="manual-select">
        <div className="manual-test-left">
          <div className="manual-item">
            <span className="manual-item-label">Tên kỳ thi:</span>
            <Input placeholder="Nhập tên kỳ thi" onChange={(e) => setName(e.target.value)} />
          </div>
          <div className="manual-item">
            <span className="manual-item-label">Học kỳ:</span>
            <Select loading={semesterLoading} placeholder="Chọn học kỳ" options={options} onChange={(value) => setSemesterId(value)} />
          </div>
          <div className="manual-item manual-duration">
            <span className="manual-item-label">Thời gian thi (phút): </span>
            <Input type="text" pattern={regexPattern.NUMBER_REGEX} placeholder="Nhập thời gian thi" onChange={(_e) => setDuration(_e.target.value)} />
          </div>
          <div className="manual-item manual-test-type">
            <span className="manual-item-label">Hình thức thi:</span>
            <Select placeholder="Chọn hình thức thi" options={testTypeOptions} onChange={(value) => setTestType(value)} />
          </div>
          <div className="manual-item manual-date">
            <span className="manual-item-label">Thời gian bắt đầu:</span>
            <DatePicker
              format={"YYYY-MM-DD HH:mm"}
              showTime={{ format: "HH:mm" }}
              // disabledDate={disabledDate}
              onChange={(value) => setStartTime(value)}
              placeholder="Chọn thời gian bắt đầu"
            ></DatePicker>
          </div>
          <div className="manual-item manual-date">
            <span className="manual-item-label">Thời gian kết thúc:</span>
            <DatePicker
              format={"YYYY-MM-DD HH:mm"}
              showTime={{ format: "HH:mm" }}
              disabled={!startTime}
              disabledDate={(current) => current < startTime}
              onChange={(value) => setEndTime(value)}
              placeholder="Chọn thời gian kết thúc"
            ></DatePicker>
          </div>
        </div>
        <div className="manual-test-right">
          <div className="manual-config-item">
            <div className="manual-item manual-totalQues">
              <span className="manual-item-label">Số câu hỏi: </span>
              <div className="manual-config-item">
                <Input
                  type="text"
                  pattern={regexPattern.NUMBER_REGEX}
                  placeholder="Nhập số câu hỏi"
                  onChange={(_e) => setQuestionQuantity(_e.target.value)}
                  required={true}
                />
                {questionQuantity > questionList.length && <div className="error-message">{errorMessage}</div>}
              </div>
            </div>
          </div>
          <div className="manual-item manual-config">
            <span className="manual-item-label">Phân loại:</span>
            <div className="manual-config-details">
              <div className="manual-config-item">
                <Input type="text" pattern={regexPattern.NUMBER_REGEX} placeholder="Số câu dễ" onChange={(_e) => setEasyNumber(_e.target.value)} />
                {easyNumber > levelQuestions[0] && <div className="error-message">{errorMessage}</div>}
              </div>
              <div className="manual-config-item">
                <Input
                  type="text"
                  pattern={regexPattern.NUMBER_REGEX}
                  placeholder="Số câu trung bình"
                  onChange={(_e) => setMediumNumber(_e.target.value)}
                />
                {mediumNumber > levelQuestions[1] && <div className="error-message">{errorMessage}</div>}
              </div>
              <div className="manual-config-item">
                <Input type="text" pattern={regexPattern.NUMBER_REGEX} placeholder="Số câu khó" onChange={(_e) => setHardNumber(_e.target.value)} />
              </div>
              {hardNumber > levelQuestions[2] && <div className="error-message">{errorMessage}</div>}
              {Number(questionQuantity) !== Number(easyNumber) + Number(mediumNumber) + Number(hardNumber) &&
                easyNumber &&
                mediumNumber &&
                hardNumber && <div className="error-message">Tổng số câu dễ, trung bình, khó phải bằng tổng số câu hỏi.</div>}
            </div>
          </div>
        </div>
      </div>
      <div className="level-search">
        <div className="list-search">
          <span className="list-search-filter-label">Tìm kiếm:</span>
          <Input.Search placeholder="Nhập nội dung câu hỏi" enterButton onSearch={onSearch} allowClear onChange={onChange} />
        </div>
        <div className="test-level">
          <span className="select-label">Mức độ:</span>
          <Select className="select-level-q" defaultValue={"ALL"} optionLabelProp="label" options={levelOptions} onChange={levelOnchange} />
        </div>
      </div>
      <div className="test-re-view">
        <TestView
          questionList={questionList}
          startTime={startTime}
          endTime={endTime}
          duration={duration}
          totalPoint={10}
          name={name}
          subjectId={subjectId}
          semesterId={semesterId}
          generateConfig={{
            numTotalQuestion: questionQuantity,
            numEasyQuestion: easyNumber,
            numMediumQuestion: mediumNumber,
            numHardQuestion: hardNumber,
          }}
          subjectOptions={subjectOptions}
          semesterOptions={options}
          quesLoading={quesLoading}
          onSelectConfigLevelQuestion={(item) => setConfig(item)}
          levelQuestion={levelQuestions}
          filter={filter}
          testType={testType}
        />
      </div>
    </div>
  );
};
export default ManualTest;
