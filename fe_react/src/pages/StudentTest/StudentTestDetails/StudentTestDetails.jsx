import React, {useEffect, useState} from "react";
import "../StudentTestDetails/StudentTestDetails.scss";
import {useLocation, useNavigate} from "react-router-dom";
import useStudentTest from "../../../hooks/useStudentTest";
import {Button, Checkbox, Modal, Radio, Spin, Tag, Tooltip} from "antd";
import {appPath} from "../../../config/appPath";
import {dateTimePattern, studentTestStatusEnum, studentTestStatusMap} from "../../../utils/constant";
import ReactQuill from "react-quill";
import {BiExport, BiSave} from "react-icons/bi";
import Countdown from "react-countdown";
import useNotify from "../../../hooks/useNotify";
import {BsArrowLeft} from "react-icons/bs";
import moment from "moment-timezone";
import {ArrowLeftOutlined, ArrowRightOutlined, SaveFilled, WarningFilled} from "@ant-design/icons";
import {removeAllSet} from "../../../utils/collectionUtils";
import {parseStringToDate} from "../../../utils/dateTimeUtils";

const StudentTestDetails = () => {

    const location = useLocation();
    const studentTestSetId = location.pathname.substring(location.pathname.lastIndexOf("/") + 1);
    const {getStudentTestSetDetails, stdTestSetDetailsLoading, stdTestSetDetails} = useStudentTest();
    const {loadTestSetDetails, testSetDetails, testSetDetailsLoading} = useStudentTest();
    const {tempSavedData, tempSaveLoading, saveTempSubmission, startAttemptTest, submitTest} = useStudentTest();
    const [startAttempt, setStartAttempt] = useState(false);
    const [openModal, setOpenModal] = useState(!!studentTestSetId);
    const [openSubmitModal, setOpenSubmitModal] = useState(false);
    const [openLeaveModal, setOpenLeaveModal] = useState(false);
    const [accept, setAccept] = useState(false);
    const [questions, setQuestions] = useState([]);
    const [currentQuestionIdx, setCurrentQuestionIdx] = useState(0);
    const [mapQuestionAnswer, setMapQuestionAnswer] = useState(new Map([]));
    const [checkedAnswers, setCheckedAnswers] = useState(new Set([]));
    const [savedTime, setSavedTime] = useState(null);
    const [remainingTime, setRemainingTime] = useState(0); // remaining time in milliseconds
    const navigate = useNavigate();
    const notify = useNotify();


    // Generate unique answerKey
    const genAnswerItemKey = (testSetQuestionId, answerNo) => testSetQuestionId * 10 + answerNo;

    // if cancel model pop up => navigate list
    const handleCancelModal = () => {
        setOpenModal(false);
        setAccept(false);
        navigate(appPath.studentTestList);
    }

    // handle submit modal
    const handleSubmitModal = () => {
        setOpenSubmitModal(false);
        if (stdTestSetDetails?.status === studentTestStatusEnum.IN_PROGRESS) {
            let tempData = {
                studentTestSetId: stdTestSetDetails?.studentTestSetId,
                submittedTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
                submissionData: [...mapQuestionAnswer?.values()],
                submissionNote: ""
            };
            submitTest(tempData);
        }
    }

    // handle leave modal
    const handleLeaveModal = () => {
        setOpenLeaveModal(false);
        if (stdTestSetDetails?.status === studentTestStatusEnum.IN_PROGRESS) {
            let tempData = {
                studentTestSetId: studentTestSetId,
                saveTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
                submissionData: [...mapQuestionAnswer?.values()],
                submissionNote: ""
            };
            saveTempSubmission(tempData);
        }
        navigate(appPath.studentTestList);
    }

    // handle when end testing time
    const handleEndTime = () => {
        notify.warning("Hết thời gian làm bài!");
        // auto submit
        if (stdTestSetDetails?.status === studentTestStatusEnum.IN_PROGRESS) {
            let tempData = {
                studentTestSetId: stdTestSetDetails?.studentTestSetId,
                submittedTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
                submissionData: [...mapQuestionAnswer?.values()],
                submissionNote: ""
            };
            submitTest(tempData);
        }
        setRemainingTime(0);
        navigate(appPath.studentTestList);
    }

    //if accept => update status and present details
    const handleAcceptModal = () => {
        // if status = 'OPEN' => call api start attempt
        if (stdTestSetDetails?.status === studentTestStatusEnum.OPEN) {
            let tempData = {
                studentTestSetId: stdTestSetDetails?.studentTestSetId,
                startedTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
                submissionData: [],
                submissionNote: ""
            };
            startAttemptTest(tempData);
        }
        // close modal => navigate to test details
        setOpenModal(false);
        setAccept(true);
    }

    // handle onChange checkbox
    const handleOnCheckedMultiAnswers = (e, answerKey) => {
        let result = [...checkedAnswers];
        if (e.target.checked) {
            result.push(answerKey);
        } else {
            result = result.filter(val => val !== answerKey);
        }
        setCheckedAnswers(new Set([...result]));
    }

    // handle onChange radio box
    const handleOnCheckedAnAnswer = (e, testSetQuestionId, checkedAnswerNo, answers) => {
        let result = new Set([...checkedAnswers]);
        // gen removed keys
        let removedAnswers = new Set([...answers.map(ans => genAnswerItemKey(testSetQuestionId, ans?.answerNo))]);
        if (e.target.checked) {
            removeAllSet(result, removedAnswers)
            result.add(genAnswerItemKey(testSetQuestionId, checkedAnswerNo));
        }
        setCheckedAnswers(result);
    }
    const handleSaveTempSubmission = () => {
        let tempData = {
            studentTestSetId: studentTestSetId,
            saveTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
            submissionData: [...mapQuestionAnswer?.values()],
            submissionNote: ""
        };
        saveTempSubmission(tempData);
    }

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
        const unloadCallback = (event) => {
            // saved current temp submission
            let tempData = {
                studentTestSetId: studentTestSetId,
                saveTime: moment(new Date()).format(dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS),
                submissionData: [...mapQuestionAnswer?.values()]
            };
            saveTempSubmission(tempData);

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

    // load student test details
    useEffect(() => {
        if (studentTestSetId) {
            getStudentTestSetDetails({id: studentTestSetId});
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // check saveTempData
    useEffect(() => {
        setSavedTime(tempSavedData?.savedTime);
    }, [tempSavedData, tempSaveLoading]);

    useEffect(() => {
        setSavedTime(stdTestSetDetails?.savedTime);
        if (accept) {
            loadTestSetDetails({
                studentTestSetId: stdTestSetDetails?.studentTestSetId,
                testSetId: stdTestSetDetails?.testSetId
            });
        }
    }, [stdTestSetDetails, accept]);


    // process logic render current checked answers
    useEffect(() => {
        setStartAttempt(stdTestSetDetails?.status === 0);

        // convert temporary_sub to map
        stdTestSetDetails?.temporarySubmission.forEach(value => {
            setMapQuestionAnswer((prev) => {
                prev.set(value?.testSetQuestionId, value);
                return prev;
            });
            value?.selectedAnswers.map(checkedNo => genAnswerItemKey(value?.testSetQuestionId, checkedNo));
            setCheckedAnswers((prev) => new Set([...prev, ...(value?.selectedAnswers.map(checkedNo => value?.testSetQuestionId * 10 + checkedNo))]));
        });
    }, [stdTestSetDetails, stdTestSetDetailsLoading]);

    useEffect(() => {
        setQuestions(testSetDetails);
    }, [testSetDetailsLoading])

    // Renderer callback with condition
    const countDownRenderer = ({days, hours, minutes, seconds, completed}) => {
        // convert days => hours
        hours = hours + days * 24;
        if (completed) {
            return <span>00:00:00</span>
        } else {
            // Render a countdown
            return <span>{hours < 10 ? "0" + hours : hours}:{minutes < 10 ? "0" + minutes : minutes}:{seconds < 10 ? "0" + seconds : seconds}</span>;
        }
    };

    const questionRender = (index) => {
        let item = questions[index];
        return (
            <div
                className="question-items"
                key={item?.testSetQuestionId}
            >
                <div className="topic-level">
                    <div className="question-topic">
                        <div className="question-number">
                            {` Câu ${item?.questionNo}: `}
                        </div>
                        <ReactQuill
                            key={index}
                            value={item.content}
                            readOnly={true}
                            theme="snow"
                            modules={{toolbar: false}}
                        />
                    </div>
                </div>
                {item?.answers &&
                    item?.answers.map((ans, ansNo) => {
                        return (
                            <div
                                className="answer-items"
                                key={ans?.answerId}>
                                {
                                    item?.isMultipleAnswers ?
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
                                                        selectedAnswers: Array.from(currentAnswerChecked)
                                                    });
                                                    return prev;
                                                });
                                                handleOnCheckedMultiAnswers(e, genAnswerItemKey(item?.testSetQuestionId, ans?.answerNo));
                                            }}
                                        />
                                        : <Radio // Only one answer
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
                                                        selectedAnswers: Array.from(newAnswerChecked)
                                                    });
                                                    return prev;
                                                });
                                                handleOnCheckedAnAnswer(e, item?.testSetQuestionId, ans?.answerNo, item?.answers);
                                            }}
                                        />
                                }
                                <span>{`${String.fromCharCode(65 + ansNo)}. `}</span>
                                <ReactQuill
                                    key={ansNo}
                                    value={ans.content}
                                    readOnly={true}
                                    theme="snow"
                                    modules={{toolbar: false}}
                                />
                            </div>);
                    })}
            </div>
        )
    }

    return (
        <Spin spinning={stdTestSetDetailsLoading}>
            <Modal
                className="handle-check-modal"
                open={openModal}
                title="Thông tin bài thi Online"
                onOk={() => setOpenModal(false)}
                onCancel={handleCancelModal}
                maskClosable={true}
                centered={true}
                footer={[
                    <Button
                        key="cancel"
                        type="primary"
                        onClick={handleCancelModal}
                    >
                        Trở về
                    </Button>,
                    <Button
                        key="accept"
                        type="primary"
                        onClick={handleAcceptModal}
                        disabled={!stdTestSetDetails || stdTestSetDetails?.status > 1 ||
                            parseStringToDate(stdTestSetDetails?.allowedStartTime, dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS).getTime() > Date.now() ||
                            parseStringToDate(stdTestSetDetails?.allowedSubmitTime, dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS).getTime() < Date.now()}
                    >
                        {startAttempt ? "Bắt đầu" : "Tiếp tục"}
                    </Button>,

                ]}
            >
                <div className="modal-container">
                    <div className="content-item">
                        <span>Bài thi học phần:</span>
                        <p>{stdTestSetDetails?.subjectTitle} - {stdTestSetDetails?.subjectCode}</p>
                    </div>
                    <div className="content-item">
                        <span>Kỳ thi:</span>
                        <p>{stdTestSetDetails?.testName} - {stdTestSetDetails?.semester}</p>
                    </div>
                    <div className="content-item">
                        <span>Thời gian làm bài:</span>
                        <p>{stdTestSetDetails?.duration} phút</p>
                    </div>
                    <div className="content-item">
                        <span>Trạng thái:</span>
                        <p style={{color: studentTestStatusMap.get(stdTestSetDetails?.status)?.color}}> {studentTestStatusMap.get(stdTestSetDetails?.status)?.label}</p>
                    </div>
                    <div className="content-item">
                        <span>Thời gian bắt đầu:</span>
                        <p>{stdTestSetDetails?.allowedStartTime}</p>
                    </div>
                    <div className="content-item">
                        <span>Hạn nộp bài:</span>
                        <p>{stdTestSetDetails?.allowedSubmitTime}</p>
                    </div>
                    <div className="content-item">
                        <span>Thời gian lưu lần cuối:</span>
                        <p>{stdTestSetDetails?.savedTime}</p>
                    </div>
                    {
                        [studentTestStatusEnum.OPEN, studentTestStatusEnum.IN_PROGRESS].includes(stdTestSetDetails?.status) &&
                        <div className="content-item">
                            <span>Số câu hỏi đã chọn:</span>
                            <p>{[...stdTestSetDetails?.temporarySubmission].filter(item => item?.selectedAnswers.length > 0).length} / {stdTestSetDetails?.questionQuantity}</p>
                        </div>
                    }
                    {
                        stdTestSetDetails?.status > 1 &&
                        <div className="content-item">
                            <span>Điểm bài thi:</span>
                            <p>{Math.round(stdTestSetDetails?.totalPoints * 100) / 100} </p>
                        </div>
                    }
                </div>
                {
                    [studentTestStatusEnum.OPEN, studentTestStatusEnum.IN_PROGRESS].includes(stdTestSetDetails?.status) &&
                    <p style={{fontStyle: "italic", marginTop: "4px"}}>Bạn có
                        muốn {startAttempt ? "bắt đầu" : "tiếp tục"} làm bài ?</p>
                }
            </Modal>
            {
                accept &&
                <div className="student-test-details">
                    <div className="header-block">
                        <Button
                            style={{color: "var(--hust-color)"}}
                            onClick={() => setOpenLeaveModal(true)}>
                            <BsArrowLeft/>
                        </Button>
                        <p>Bài thi "{stdTestSetDetails?.testName}"
                            - {stdTestSetDetails?.subjectTitle} - {stdTestSetDetails?.subjectCode}</p>
                    </div>
                    <div className="content-block">
                        <div className="side-block">
                            <div className="test-timer">
                                <p>THỜI GIAN</p>
                                <Countdown
                                    date={moment(stdTestSetDetails?.allowedSubmitTime, dateTimePattern.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS).toDate().getTime()}
                                    renderer={countDownRenderer}
                                    onComplete={handleEndTime}
                                    onTick={({hours, minutes, seconds}) => {
                                        setRemainingTime(hours * 3600 + minutes * 60 + seconds);
                                    }}
                                />
                            </div>
                            <div className="question-nav">
                                <p>DANH MỤC CÂU HỎI</p>
                                <div className="questions-nav-items">
                                    {
                                        questions.map((value, index) => {
                                            return (
                                                <Tag
                                                    key={index}
                                                    style={currentQuestionIdx === index ? {
                                                        color: '#ffffff',
                                                        backgroundColor: 'var(--hust-color)'
                                                    } : {}}
                                                    onClick={() => setCurrentQuestionIdx(index)}>
                                                    <span
                                                        style={mapQuestionAnswer.get(value?.testSetQuestionId)?.selectedAnswers.length > 0 ? {borderBottom: '2px solid var(--hust-color)'} : {}}>
                                                        {index + 1}
                                                    </span>
                                                </Tag>
                                            );
                                        })
                                    }
                                </div>
                                <div className="submit-block">
                                    <Tooltip title="Lưu bài làm tạm thời">
                                        <Button
                                            onClick={handleSaveTempSubmission} loading={tempSaveLoading}>
                                            <BiSave style={{color: "var(--hust-color)"}}/>
                                            Lưu
                                        </Button>
                                    </Tooltip>
                                    <Tooltip title="Nộp bài thi">
                                        <Button
                                            onClick={() => setOpenSubmitModal(true)}
                                        >
                                            <BiExport style={{color: "var(--hust-color)"}}/>
                                            Nộp bài
                                        </Button>
                                    </Tooltip>
                                </div>
                            </div>
                        </div>
                        <div className="question-block">
                            <div className="next-prev-btn-block">
                                <span
                                    style={{fontStyle: "italic"}}>Đã lưu lần cuối vào {savedTime} </span>
                                {
                                    currentQuestionIdx < questions?.length && currentQuestionIdx > 0 &&
                                    <Button onClick={() => {
                                        let nextIdx = currentQuestionIdx - 1;
                                        setCurrentQuestionIdx(nextIdx);
                                    }}>
                                        <ArrowLeftOutlined style={{color: "var(--hust-color)"}}/>
                                        Câu trước
                                    </Button>
                                }
                                {
                                    currentQuestionIdx < questions?.length - 1 &&
                                    <Button onClick={() => {
                                        let nextIdx = currentQuestionIdx + 1;
                                        setCurrentQuestionIdx(nextIdx);
                                    }}>
                                        Câu tiếp
                                        <ArrowRightOutlined style={{color: "var(--hust-color)"}}/>
                                    </Button>
                                }
                            </div>
                            <Spin tip="Đang tải đề thi" spinning={testSetDetailsLoading} size="large">
                                <div className="question-items">
                                    {
                                        questions?.length > 0 ? questionRender(currentQuestionIdx) : <span></span>
                                    }
                                </div>
                            </Spin>
                        </div>
                    </div>
                </div>
            }
            <Modal
                className="submit-modal"
                open={openSubmitModal}
                title="Xác nhận nộp bài"
                onOk={handleSubmitModal}
                onCancel={() => setOpenSubmitModal(false)}
                maskClosable={true}
                centered={true}
                footer={[
                    <Button
                        key="cancel"
                        type="primary"
                        onClick={() => setOpenSubmitModal(false)}
                    >
                        Hủy
                    </Button>,
                    <Button
                        key="accept"
                        type="primary"
                        onClick={handleSubmitModal}
                    >
                        Nộp bài
                    </Button>,
                ]}
            >
                <div className="modal-container">
                    <SaveFilled style={{color: 'var(--hust-color)', marginRight: '4px'}}/>
                    <span className="italic">Bạn chắc chắn muốn nộp bài thi?</span>
                </div>
            </Modal>
            <Modal
                className="leave-modal"
                open={openLeaveModal}
                title="Xác nhận rời khỏi bài thi"
                maskClosable={true}
                centered={true}
                footer={[
                    <Button
                        key="cancel"
                        type="primary"
                        onClick={() => setOpenLeaveModal(false)}
                    >
                        Hủy
                    </Button>,
                    <Button
                        key="accept"
                        type="primary"
                        onClick={handleLeaveModal}
                    >
                        Đồng ý
                    </Button>,
                ]}
            >
                <div className="modal-container">
                    <WarningFilled style={{color: 'var(--hust-color)', marginRight: '4px'}}/>
                    <span className="italic">Dữ liệu bài thi sẽ được lưu tại thời điểm bạn rời khỏi !</span>
                </div>
            </Modal>
        </Spin>
    );
};
export default StudentTestDetails;