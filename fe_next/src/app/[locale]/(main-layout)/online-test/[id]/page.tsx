"use client";
import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import AppTooltip from "@/components/app-tooltip";
import HtmlRenderer from "@/components/html-render";
import { PATH_ROUTER } from "@/constants/router";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import {
  useGetStudentTestSetDetailsQuery,
  useLoadStudentTestSetQuery,
  useSaveTempSubmissionMutation,
  useStartAttemptTestMutation,
  useSubmitTestMutation,
} from "@/stores/student-test-set/api";
import { dateTimePattern, studentTestStatusEnum } from "@/constants";
import { ArrowLeftOutlined, ArrowRightOutlined, SaveFilled, WarningFilled } from "@ant-design/icons";
import { Checkbox, Radio, Spin, Tag } from "antd";
import moment from "moment-timezone";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import Countdown from "react-countdown";
import { BiExport, BiSave } from "react-icons/bi";
import { BsArrowLeft } from "react-icons/bs";
import "./StudentTestDetails.scss";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { removeAllSet } from "@/helpers";

const OnlineTest = ({ params }: { params: { id: string } }) => {
  const tOnlineTest = useTranslations("onlineTest");
  const tCommon = useTranslations("common");
  const { studentTestStatusMap } = useLocaleOptions();
  const studentTestSetId = params.id;
  const router = useRouter();
  const notification = useAppNotification();
  const handleError = useHandleError();
  const [startAttempt, setStartAttempt] = useState(false);
  const [openModal, setOpenModal] = useState(!!studentTestSetId);
  const [openSubmitModal, setOpenSubmitModal] = useState(false);
  const [openLeaveModal, setOpenLeaveModal] = useState(false);
  const [accept, setAccept] = useState(false);
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIdx, setCurrentQuestionIdx] = useState(0);
  const [mapQuestionAnswer, setMapQuestionAnswer] = useState<any>(new Map([]));
  const [checkedAnswers, setCheckedAnswers] = useState(new Set([]));
  const [savedTime, setSavedTime] = useState(null);
  const [remainingTime, setRemainingTime] = useState(0); // remaining time in milliseconds
  const [startAttemptTest] = useStartAttemptTestMutation();
  const [submitTest] = useSubmitTestMutation();
  const [saveTempSubmission, { isLoading: tempSaveLoading }] = useSaveTempSubmissionMutation();

  const { data: stdTestSetDetails, isLoading: stdTestSetDetailsLoading } = useGetStudentTestSetDetailsQuery(
    {
      id: studentTestSetId,
    },
    {
      refetchOnMountOrArgChange: true,
      skip: !studentTestSetId,
    }
  );

  const { data: testSetDetails, isLoading: testSetDetailsLoading } = useLoadStudentTestSetQuery(
    {
      studentTestSetId: studentTestSetId,
      testSetId: stdTestSetDetails?.testSetId,
    },
    {
      refetchOnMountOrArgChange: true,
      skip: !studentTestSetId || !stdTestSetDetails?.testSetId || !accept,
    }
  );

  // Generate unique answerKey
  const genAnswerItemKey = (testSetQuestionId, answerNo) => testSetQuestionId * 10 + answerNo;

  // if cancel model pop up => navigate list
  const handleCancelModal = () => {
    setOpenModal(false);
    setAccept(false);
    router.push(PATH_ROUTER.PROTECTED.STUDENT_TEST_LIST);
  };

  // handle submit modal
  const handleSubmitModal = async () => {
    setOpenSubmitModal(false);
    if (stdTestSetDetails?.status === studentTestStatusEnum.IN_PROGRESS) {
      try {
        const res = await submitTest({
          studentTestSetId: stdTestSetDetails?.studentTestSetId,
          submittedTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
          submissionData: Array.from(mapQuestionAnswer.values()),
          submissionNote: "",
        }).unwrap();
        setSavedTime(res?.data?.savedTime);
      } catch (error) {
        handleError(error);
      }
    }
  };

  // handle leave modal
  const handleLeaveModal = async () => {
    setOpenLeaveModal(false);
    if (stdTestSetDetails?.status === studentTestStatusEnum.IN_PROGRESS) {
      try {
        const res = await saveTempSubmission({
          studentTestSetId: studentTestSetId,
          saveTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
          submissionData: Array.from(mapQuestionAnswer.values()),
          submissionNote: "",
        }).unwrap();
        setSavedTime(res?.data?.savedTime);
      } catch (error) {
        handleError(error);
      }
    }
    router.push(PATH_ROUTER.PROTECTED.STUDENT_TEST_LIST);
  };

  // handle when end testing time
  const handleEndTime = async () => {
    notification.warning({
      description: tOnlineTest("timeup"),
    });
    // auto submit
    if (stdTestSetDetails?.status === studentTestStatusEnum.IN_PROGRESS) {
      try {
        const res = await submitTest({
          studentTestSetId: stdTestSetDetails?.studentTestSetId,
          submittedTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
          submissionData: Array.from(mapQuestionAnswer.values()),
          submissionNote: "",
        }).unwrap();
        setSavedTime(res?.data?.savedTime);
      } catch (error) {
        handleError(error);
      }
    }
    setRemainingTime(0);
    router.push(PATH_ROUTER.PROTECTED.STUDENT_TEST_LIST);
  };

  //if accept => update status and present details
  const handleAcceptModal = async () => {
    // if status = 'OPEN' => call api start attempt
    if (stdTestSetDetails?.status === studentTestStatusEnum.OPEN) {
      try {
        const res = await startAttemptTest({
          studentTestSetId: stdTestSetDetails?.studentTestSetId,
          startedTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
          submissionData: [],
          submissionNote: "",
        }).unwrap();
        setSavedTime(res?.data?.savedTime);
      } catch (error) {
        handleError(error);
      }
    }
    // close modal => navigate to test details
    setOpenModal(false);
    setAccept(true);
  };

  // handle onChange checkbox
  const handleOnCheckedMultiAnswers = (e, answerKey) => {
    let result = Array.from(checkedAnswers);
    if (e.target.checked) {
      result.push(answerKey);
    } else {
      result = result.filter((val) => val !== answerKey);
    }
    setCheckedAnswers(new Set([...result]));
  };

  // handle onChange radio box
  const handleOnCheckedAnAnswer = (e, testSetQuestionId, checkedAnswerNo, answers) => {
    let result = Array.from(checkedAnswers);
    // gen removed keys
    let removedAnswers = new Set([...answers.map((ans) => genAnswerItemKey(testSetQuestionId, ans?.answerNo))]);
    if (e.target.checked) {
      removeAllSet(result, removedAnswers);
      result.push(genAnswerItemKey(testSetQuestionId, checkedAnswerNo));
    }
    setCheckedAnswers(new Set([...result]));
  };
  const handleSaveTempSubmission = async () => {
    try {
      const res = await saveTempSubmission({
        studentTestSetId: studentTestSetId,
        saveTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
        submissionData: Array.from(mapQuestionAnswer.values()),
        submissionNote: "",
      }).unwrap();
      setSavedTime(res?.data?.savedTime);
    } catch (error) {
      handleError(error);
    }
  };

  // save Temp Submissions periodically
  useEffect(() => {
    const timeSlice = 5 * 60;
    const timerId = setInterval(() => {
      if (remainingTime === 0 || remainingTime >= timeSlice) {
        handleSaveTempSubmission();
      }
    }, timeSlice * 1000); // update periodically 5min
    return () => clearInterval(timerId);
  }, []);

  // check when reload/closed page
  useEffect(() => {
    const unloadCallback = async (event) => {
      // saved current temp submission
      try {
        const res = await saveTempSubmission({
          studentTestSetId: studentTestSetId,
          saveTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
          submissionData: Array.from(mapQuestionAnswer.values()),
        }).unwrap();
        setSavedTime(res?.data?.savedTime);
      } catch (error) {
        handleError(error);
      }
      // prevent default
      event.preventDefault();
      event.returnValue = "";
      return "";
    };

    // check if testSetDetails is loaded and has a change
    if (testSetDetails || testSetDetails?.length > 0) {
      window.addEventListener("beforeunload", unloadCallback);
      return () => window.removeEventListener("beforeunload", unloadCallback);
    }
  }, []);

  // check saveTempData
  // useEffect(() => {
  //   setSavedTime(tempSavedData?.savedTime);
  // }, [tempSavedData, tempSaveLoading]);

  // process logic render current checked answers
  useEffect(() => {
    setStartAttempt(stdTestSetDetails?.status === 0);

    // convert temporary_sub to map
    stdTestSetDetails?.temporarySubmission.forEach((value) => {
      setMapQuestionAnswer((prev) => {
        prev.set(value?.testSetQuestionId, value);
        return prev;
      });
      value?.selectedAnswers.map((checkedNo) => genAnswerItemKey(value?.testSetQuestionId, checkedNo));
      // setCheckedAnswers((prev) => new Set([...prev, ...value?.selectedAnswers.map((checkedNo) => value?.testSetQuestionId * 10 + checkedNo)]));
    });
  }, [stdTestSetDetails, stdTestSetDetailsLoading]);

  useEffect(() => {
    setQuestions(testSetDetails);
  }, [testSetDetailsLoading]);

  // Renderer callback with condition
  const countDownRenderer = ({ days, hours, minutes, seconds, completed }) => {
    // convert days => hours
    hours = hours + days * 24;
    if (completed) {
      return <span>00:00:00</span>;
    } else {
      // Render a countdown
      return (
        <span>
          {hours < 10 ? "0" + hours : hours}:{minutes < 10 ? "0" + minutes : minutes}:{seconds < 10 ? "0" + seconds : seconds}
        </span>
      );
    }
  };

  const questionRender = (index) => {
    let item = questions[index];
    return (
      <div className="question-items" key={item?.testSetQuestionId}>
        <div className="topic-level">
          <div className="question-topic">
            <div className="question-number">{`${tCommon("question")} ${item?.questionNo}: `}</div>
            <HtmlRenderer htmlContent={item.content} />
          </div>
        </div>
        {item?.answers &&
          item?.answers.map((ans, ansNo) => {
            return (
              <div className="answer-items" key={ans?.answerId}>
                {item?.isMultipleAnswers ? (
                  <Checkbox // Multiple answers
                    checked={checkedAnswers.has(genAnswerItemKey(item?.testSetQuestionId, ans?.answerNo))}
                    onChange={(e) => {
                      let currentAnswerChecked = new Set(mapQuestionAnswer.get(item?.testSetQuestionId)?.selectedAnswers);
                      if (e?.target?.checked) {
                        currentAnswerChecked.add(ans?.answerNo);
                      } else {
                        currentAnswerChecked.delete(ans?.answerNo);
                      }
                      setMapQuestionAnswer((prev) => {
                        prev.set(item?.testSetQuestionId, {
                          testSetQuestionId: item?.testSetQuestionId,
                          questionNo: item?.questionNo,
                          selectedAnswers: Array.from(currentAnswerChecked),
                        });
                        return prev;
                      });
                      handleOnCheckedMultiAnswers(e, genAnswerItemKey(item?.testSetQuestionId, ans?.answerNo));
                    }}
                  />
                ) : (
                  <Radio // Only one answer
                    checked={checkedAnswers.has(genAnswerItemKey(item?.testSetQuestionId, ans?.answerNo))}
                    onChange={(e) => {
                      let newAnswerChecked = new Set([]);
                      if (e?.target?.checked) {
                        newAnswerChecked.add(ans?.answerNo);
                      }
                      setMapQuestionAnswer((prev) => {
                        prev.set(item?.testSetQuestionId, {
                          testSetQuestionId: item?.testSetQuestionId,
                          questionNo: item?.questionNo,
                          selectedAnswers: Array.from(newAnswerChecked),
                        });
                        return prev;
                      });
                      handleOnCheckedAnAnswer(e, item?.testSetQuestionId, ans?.answerNo, item?.answers);
                    }}
                  />
                )}
                <span>{`${String.fromCharCode(65 + ansNo)}. `}</span>
                <HtmlRenderer htmlContent={ans.content} />
              </div>
            );
          })}
      </div>
    );
  };

  return (
    <Spin spinning={stdTestSetDetailsLoading}>
      <AppModal
        className="handle-check-modal"
        open={openModal}
        title={tOnlineTest("infoTest")}
        onOk={() => setOpenModal(false)}
        onCancel={handleCancelModal}
        maskClosable={true}
        centered={true}
        footer={[
          <AppButton key="cancel" type="primary" onClick={handleCancelModal}>
            {tCommon("back")}
          </AppButton>,
          <AppButton key="accept" type="primary" onClick={handleAcceptModal} disabled={!stdTestSetDetails || stdTestSetDetails?.status > 1}>
            {startAttempt ? tCommon("start") : tCommon("continue")}
          </AppButton>,
        ]}
      >
        <div className="modal-container">
          <div className="content-item">
            <span>{tOnlineTest("subject")}:</span>
            <p>
              {stdTestSetDetails?.subjectTitle} - {stdTestSetDetails?.subjectCode}
            </p>
          </div>
          <div className="content-item">
            <span>{tOnlineTest("exam")}:</span>
            <p>
              {stdTestSetDetails?.testName} - {stdTestSetDetails?.semester}
            </p>
          </div>
          <div className="content-item">
            <span>{tOnlineTest("duration")}:</span>
            <p>
              {stdTestSetDetails?.duration} {tCommon("minutes")}
            </p>
          </div>
          <div className="content-item">
            <span>{tOnlineTest("status")}:</span>
            <p style={{ color: studentTestStatusMap.get(stdTestSetDetails?.status)?.color }}>
              {" "}
              {studentTestStatusMap.get(stdTestSetDetails?.status)?.label}
            </p>
          </div>
          <div className="content-item">
            <span>{tOnlineTest("startTime")}:</span>
            <p>{stdTestSetDetails?.allowedStartTime}</p>
          </div>
          <div className="content-item">
            <span>{tOnlineTest("endTime")}:</span>
            <p>{stdTestSetDetails?.allowedSubmitTime}</p>
          </div>
          <div className="content-item">
            <span>{tOnlineTest("savedTime")}:</span>
            <p>{stdTestSetDetails?.savedTime}</p>
          </div>
          {[studentTestStatusEnum.OPEN, studentTestStatusEnum.IN_PROGRESS].includes(stdTestSetDetails?.status) && (
            <div className="content-item">
              <span>{tOnlineTest("selectedQuestions")}:</span>
              <p>
                {[...stdTestSetDetails?.temporarySubmission].filter((item) => item?.selectedAnswers.length > 0).length} /{" "}
                {stdTestSetDetails?.questionQuantity}
              </p>
            </div>
          )}
          {stdTestSetDetails?.status > 1 && (
            <div className="content-item">
              <span>{tOnlineTest("score")}:</span>
              <p>{Math.round(stdTestSetDetails?.totalPoints * 100) / 100} </p>
            </div>
          )}
        </div>
        {[studentTestStatusEnum.OPEN, studentTestStatusEnum.IN_PROGRESS].includes(stdTestSetDetails?.status) && (
          <p className="italic mt-1">{tOnlineTest("doUwant", { status: startAttempt ? tCommon("start") : tCommon("continue") })}</p>
        )}
      </AppModal>
      {accept && (
        <div className="student-test-details">
          <div className="header-block">
            <AppButton rootClassName="!text-text-hust" onClick={() => setOpenLeaveModal(true)}>
              <BsArrowLeft />
            </AppButton>
            <p>
              {tOnlineTest("baithi")} {stdTestSetDetails?.testName} - {stdTestSetDetails?.subjectTitle} - {stdTestSetDetails?.subjectCode}
            </p>
          </div>
          <div className="content-block">
            <div className="side-block">
              <div className="test-timer">
                <p>{tOnlineTest("time")}</p>
                <Countdown
                  date={moment(stdTestSetDetails?.allowedSubmitTime, dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS).toDate().getTime()}
                  renderer={countDownRenderer}
                  onComplete={handleEndTime}
                  onTick={({ hours, minutes, seconds }) => {
                    setRemainingTime(hours * 3600 + minutes * 60 + seconds);
                  }}
                />
              </div>
              <div className="question-nav">
                <p>{tOnlineTest("questionNav")}</p>
                <div className="questions-nav-items">
                  {questions.map((value, index) => {
                    return (
                      <Tag
                        key={index}
                        className={`${currentQuestionIdx === index ? "bg-text-hust text-white" : ""}`}
                        onClick={() => setCurrentQuestionIdx(index)}
                      >
                        <span
                          className={`${
                            mapQuestionAnswer.get(value?.testSetQuestionId)?.selectedAnswers.length > 0 ? "border-b-2 border-hust-color" : ""
                          }`}
                        >
                          {index + 1}
                        </span>
                      </Tag>
                    );
                  })}
                </div>
                <div className="submit-block">
                  <AppTooltip title={tOnlineTest("save")}>
                    <AppButton onClick={handleSaveTempSubmission} loading={tempSaveLoading}>
                      <BiSave style={{ color: "var(--hust-color)" }} />
                      {tCommon("save")}
                    </AppButton>
                  </AppTooltip>
                  <AppTooltip title={tOnlineTest("submit")}>
                    <AppButton onClick={() => setOpenSubmitModal(true)}>
                      <BiExport style={{ color: "var(--hust-color)" }} />
                      {tCommon("submit")}
                    </AppButton>
                  </AppTooltip>
                </div>
              </div>
            </div>
            <div className="question-block">
              <div className="next-prev-btn-block">
                <span className="italic">
                  {tOnlineTest("savedTime")}: {savedTime}
                </span>
                {currentQuestionIdx < questions?.length && currentQuestionIdx > 0 && (
                  <AppButton
                    onClick={() => {
                      let nextIdx = currentQuestionIdx - 1;
                      setCurrentQuestionIdx(nextIdx);
                    }}
                  >
                    <ArrowLeftOutlined style={{ color: "var(--hust-color)" }} />
                    {tOnlineTest("prevQuestion")}
                  </AppButton>
                )}
                {currentQuestionIdx < questions?.length - 1 && (
                  <AppButton
                    onClick={() => {
                      let nextIdx = currentQuestionIdx + 1;
                      setCurrentQuestionIdx(nextIdx);
                    }}
                  >
                    {tOnlineTest("nextQuestion")}
                    <ArrowRightOutlined style={{ color: "var(--hust-color)" }} />
                  </AppButton>
                )}
              </div>
              <Spin tip={tCommon("loading")} spinning={testSetDetailsLoading} size="large">
                <div className="question-items">{questions?.length > 0 ? questionRender(currentQuestionIdx) : <span></span>}</div>
              </Spin>
            </div>
          </div>
        </div>
      )}
      <AppModal
        className="submit-modal"
        open={openSubmitModal}
        title={tOnlineTest("confirmSubmit")}
        onOk={handleSubmitModal}
        onCancel={() => setOpenSubmitModal(false)}
        maskClosable={true}
        centered={true}
        footer={[
          <AppButton key="cancel" type="primary" onClick={() => setOpenSubmitModal(false)}>
            {tCommon("cancel")}
          </AppButton>,
          <AppButton key="accept" type="primary" onClick={handleSubmitModal}>
            {tCommon("submit")}
          </AppButton>,
        ]}
      >
        <div className="modal-container">
          <SaveFilled style={{ color: "var(--hust-color)", marginRight: "4px" }} />
          <span className="italic">{tOnlineTest("confirmSubmit")}</span>
        </div>
      </AppModal>
      <AppModal
        className="leave-modal"
        open={openLeaveModal}
        title={tOnlineTest("confirmLeave")}
        maskClosable={true}
        centered={true}
        footer={[
          <AppButton key="cancel" type="primary" onClick={() => setOpenLeaveModal(false)}>
            {tCommon("cancel")}
          </AppButton>,
          <AppButton key="accept" type="primary" onClick={handleLeaveModal}>
            {tCommon("accept")}
          </AppButton>,
        ]}
      >
        <div className="modal-container">
          <WarningFilled style={{ color: "var(--hust-color)", marginRight: "4px" }} />
          <span className="italic">{tOnlineTest("dataWillBeSaved")}</span>
        </div>
      </AppModal>
    </Spin>
  );
};
export default OnlineTest;
