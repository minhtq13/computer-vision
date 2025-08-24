import { Button, Form, Select, Space, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import iconArrow from "../../assets/images/svg/arrow-under-header.svg";
import confirmIcon from "../../assets/images/svg/confirm.svg";
import ModalPopup from "../../components/ModalPopup/ModalPopup";
import useAI from "../../hooks/useAI";
import useNotify from "../../hooks/useNotify";
import "./AutomaticScoring.scss";
import AutoScoringHeaderSelect from "./AutoScoringHeaderSelect";
import ModalSelectedImage from "./ModalSelectedImage";
import TableResult from "./TableResult";
import MayBeWrong from "./MayBeWrong";
import { numberAnswerOption } from "../../utils/constant";
import { getStaticFile } from "../../utils/tools";
import { apiPath } from "../../config/apiPath";
import { FaSheetPlastic } from "react-icons/fa6";
import useExamClass from "../../hooks/useExamClass";
import ExportCorrectAnswer from "./ExportCorrectAnswer";

const { Option } = Select;

const formItemLayout = {
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 14,
  },
};

const AutomaticScoring = () => {
  const notify = useNotify();
  const { refreshTableImage } = useSelector((state) => state.refreshReducer);
  const {
    getModelAI,
    resultAI,
    mayBeWrong,
    setMayBeWrong,
    loading,
    loadingSaveResult,
    resetTableResult,
    setResultAI,
    saveTableResult,
    imgInFolder,
    getImgInFolder,
    setImgInFolder,
    loadLatestTempScoredData,
    examClassStudentCodes,
    setExamClassStudentCodes,
  } = useAI();
  const { examClassCode } = useSelector((state) => state.appReducer);
  const [listExamClassCode, setListExamClassCode] = useState([]);
  const [listMSSV, setListMSSV] = useState([]);
  const [numberAnswer, setNumberAnswer] = useState(60);
  const [examClassId, setExamClassId] = useState(null);
  const { examClassInfo, getExamClassDetail } = useExamClass();

  useEffect(() => {
    if (examClassCode) {
      getImgInFolder(examClassCode, {});
    } else {
      setImgInFolder([]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [examClassCode, refreshTableImage]);

  // firstly, load the latest scored data of an exam-class
  useEffect(() => {
    if (examClassCode) {
      setResultAI([]);
      setExamClassStudentCodes([]);
      loadLatestTempScoredData(examClassCode, ["ALL"]);
    }
  }, [examClassCode]);

  // get exam-class info
  useEffect(() => {
    if (examClassId) {
      getExamClassDetail(examClassId);
    }
  }, [examClassId]);

  const handleSubmit = () => {
    if (imgInFolder.length > 0) {
      resetTableResult({
        examClassCode: examClassCode
      }, false);
      setResultAI([]);
      setExamClassStudentCodes([]);
      setMayBeWrong([]);
      getModelAI(examClassCode, {examClassCode: examClassCode, selectedImages: ["ALL"]});
    } else {
      notify.error("Vui lòng tải ảnh lên!");
    }
  };
  const onFinish = () => {};
  const handleReset = () => {
    resetTableResult({
      examClassCode: examClassCode
    });
    setResultAI([]);
    setMayBeWrong([]);
  };
  const handleSaveResult = () => {
    saveTableResult({
      examClassCode: examClassCode
    });
  };
  const handleSelectMDT = () => {};
  const handleSelectMSSV = (value) => {
    loadLatestTempScoredData(examClassCode, [value ?? "ALL"]);
  };
  const handleSelectNumberAnswer = (value) => {
    setNumberAnswer(value);
  };
  return (
    <div className="automatic-scoring-wrapper">
      <div className="header-automatic-scoring">
        <h2>Chấm điểm tự động - Bài thi Offline</h2>
      </div>
      <AutoScoringHeaderSelect onSelectExamClass={(id) => setExamClassId(id)} />
      <div className="content-automatic-scoring">
        <Form name="validate_other" {...formItemLayout} onFinish={onFinish}>
          <div className="option">
            {examClassInfo?.hasScoringPermission && <ModalSelectedImage loading={loading} imgInFolder={imgInFolder} />}
            <ModalPopup
              buttonOpenModal={
                <Button
                  type="primary"
                  loading={loading}
                  className="button-submit-ai"
                  disabled={!examClassCode || imgInFolder.length === 0 || !examClassInfo?.hasScoringPermission}
                >
                  Chấm điểm
                </Button>
              }
              icon={confirmIcon}
              title="Chấm điểm"
              message={"Bạn chắc chắn muốn chấm điểm những ảnh đã chọn? "}
              confirmMessage={"Quá trình này có thể mất một khoảng thời gian, bạn có thể xem lại các ảnh được chấm ở bên cạnh nút chấm điểm!"}
              ok={"Đồng ý"}
              onAccept={handleSubmit}
            />
            <MayBeWrong mayBeWrong={mayBeWrong} examClassCode={examClassCode} />
            <Button
              onClick={handleReset}
              className="button-reset-table-result"
              disabled={resultAI.length === 0 || !examClassInfo?.hasScoringPermission}
            >
              Đặt lại
            </Button>
          </div>
          <div className="filter-table-ai">
            <Space>
              <div className="detail-button">MSSV:</div>
              <Select
                optionLabelProp="label"
                onChange={handleSelectMSSV}
                className="custom-select-antd"
                suffixIcon={<img src={iconArrow} alt="" />}
                style={{ width: 230 }}
                placeholder="Nhập MSSV"
                showSearch
                allowClear
              >
                {listMSSV.map((item, index) => {
                  return (
                    <Option value={item.value} label={item.text} key={index}>
                      <div className="d-flex item_DropBar dropdown-option">
                        <div className="dropdown-option-item text-14">{item.value}</div>
                      </div>
                    </Option>
                  );
                })}
              </Select>
            </Space>
            <Space>
              <div className="detail-button">Mã đề thi:</div>
              <Select
                optionLabelProp="label"
                onChange={handleSelectMDT}
                className="custom-select-antd"
                suffixIcon={<img src={iconArrow} alt="" />}
                style={{ width: 230 }}
                placeholder="Nhập mã đề thi"
                showSearch
                allowClear
              >
                {listExamClassCode.map((item, index) => {
                  return (
                    <Option value={item.value} label={item.text} key={index}>
                      <div className="d-flex item_DropBar dropdown-option">
                        <div className="dropdown-option-item text-14">{item.value}</div>
                      </div>
                    </Option>
                  );
                })}
              </Select>
            </Space>
            <Space>
              <div className="detail-button">Hiển thị:</div>
              <Select
                optionLabelProp="label"
                onChange={handleSelectNumberAnswer}
                className="custom-select-antd"
                suffixIcon={<img src={iconArrow} alt="" />}
                style={{ width: 150 }}
                placeholder="Nhập số câu muốn hiển thị"
                showSearch
                defaultValue={60}
              >
                {numberAnswerOption.map((item, index) => {
                  return (
                    <Option value={item.value} label={item.text} key={index}>
                      <div className="d-flex item_DropBar dropdown-option">
                        <div className="dropdown-option-item text-14">{item.text}</div>
                      </div>
                    </Option>
                  );
                })}
              </Select>
            </Space>
            <Space>
              <Tooltip className="options" title="Phiếu trả lời mẫu">
                <Button className="cursor-pointer" type="primary" onClick={() => getStaticFile(apiPath.answerSheetTemplate)}>
                  <FaSheetPlastic style={{ color: "#ffffff" }} />
                </Button>
              </Tooltip>
            </Space>

            <ExportCorrectAnswer data={resultAI} />
          </div>
          <div className="result-ai">
            <TableResult
              numberAnswer={numberAnswer}
              resultAI={resultAI}
              loadingTable={loading}
              setListExamClassCode={setListExamClassCode}
              setListMSSV={setListMSSV}
              examClassStudentCodes={examClassStudentCodes}
            />
          </div>
          <div className="button-footer">
            <ModalPopup
              buttonOpenModal={
                <Button type="primary" loading={loadingSaveResult} disabled={resultAI.length === 0} className="button-submit-ai">
                  Lưu kết quả
                </Button>
              }
              icon={confirmIcon}
              title="Lưu kết quả chấm thi"
              message={"Bạn chắc chắn muốn lưu kết quả chấm thi này?"}
              ok={"Đồng ý"}
              onAccept={handleSaveResult}
            />
          </div>
        </Form>
      </div>
    </div>
  );
};

export default AutomaticScoring;
