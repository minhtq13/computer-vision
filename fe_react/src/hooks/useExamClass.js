import {useState} from "react";
import useNotify from "./useNotify";
import {
    getExamClassDetailService,
    getPagingExamClassService,
    getParticipantServices,
    getExamClassResultService,
    assignStudentTestSetService, publishStudentTestSetService, sendEmailExamClassResultService
} from "../services/examClassServices";
import {HttpStatusCode} from "axios";
import {Test, ExamClass, StudentTestSet, System} from "../common/apiErrorCode"
import {processApiError} from "../utils/apiUtils";

const useExamClasses = () => {
    const notify = useNotify();
    const [allExamClasses, setAllExamClasses] = useState([]);
    const [tableLoading, setTableLoading] = useState(true);
    const [pagination, setPagination] = useState({});
    const [examClassInfo, setExamClassInfo] = useState({});
    const [infoLoading, setInfoLoading] = useState(true);
    const [participants, setParticipants] = useState([]);
    const [partiLoading, setPartiLoading] = useState(true);
    const [resultLoading, setResultLoading] = useState(false);
    const [isPublishedAll, setIsPublishedAll] = useState(false);
    const [isHandled, setIsHandled] = useState(false);
    const [resultData, setResultData] = useState([]);
    const [dataPieChart, setPieDataChart] = useState([]);
    const [dataColumnChart, setColumnDataChart] = useState([]);
    const [assignLoading, setAssignLoading] = useState(false);
    const [publishLoading, setPublishLoading] = useState(false);
    const [sendEmailLoading, setSendEmailLoading] = useState(false);

    const getAllExamClasses = (payload) => {
        setTableLoading(true);
        getPagingExamClassService(
            payload.code,
            payload.subjectId,
            payload.semesterId,
            payload.page,
            payload.size,
            payload.sort,
            (res) => {
                setAllExamClasses(res?.data?.content);
                setTableLoading(false);
                setPagination({
                    current: res.data?.pageable.pageNumber + 1,
                    pageSize: res.data?.pageable.pageSize,
                    total: res.data?.totalElements,
                });
            },
            (err) => {
                setTableLoading(true);
                notify.error(processApiError(err));
            }
        ).then(() => {});
    };
    const getExamClassDetail = (classId) => {
        setInfoLoading(true);
        getExamClassDetailService(
            classId,
            (res) => {
                setExamClassInfo(res?.data);
                setInfoLoading(false);
            },
            () => {
                notify.error("Không thể lấy thông tin lớp thi!");
            }
        ).then(() => {});
    };

    const getParticipants = (classId, roleType) => {
        setPartiLoading(true);
        getParticipantServices(
            classId,
            roleType,
            (res) => {
                setPartiLoading(false);
                setParticipants(res?.data);
            },
            () => {
                notify.error("Không thể lấy danh sách người tham gia!");
                setPartiLoading(false);
            }
        ).then(() => {});
    };

    const getResult = (examClassCode, param) => {
        setResultLoading(true);
        getExamClassResultService(
            examClassCode,
            param,
            (res) => {
                setResultData(res?.data?.results);
                setColumnDataChart(res?.data?.columnChart);
                setPieDataChart(res?.data?.pieChart);
                setIsPublishedAll(res?.data?.isPublishedAll);
                setIsHandled(res?.data?.isHandled);
                setResultLoading(false);
            },
            (error) => {
                if (error?.response?.status !== HttpStatusCode.Forbidden) {
                    notify.error("Không thể lấy kết quả thi!");
                }
                setResultLoading(false);
            }
        ).then(() => {});
    };

    /**
     * assign test-set
     */
    const assignStudentTestSet = (examClassId) => {
        setAssignLoading(true);
        assignStudentTestSetService(examClassId,
            (res) => {
                if (res?.status === HttpStatusCode.Ok) {
                    setAssignLoading(false)
                    notify.success("Giao bài thi cho lớp thi thành công!")
                }
            },
            (error) => {
                let data = error?.data;
                if (error?.status === HttpStatusCode.Forbidden) {
                    notify.error(System.FORBIDDEN.message)
                } else if (data?.code === StudentTestSet.EXISTED_NOT_OPEN_IN_EXAM_CLASS.code) {
                    notify.error(StudentTestSet.EXISTED_NOT_OPEN_IN_EXAM_CLASS.message);
                } else if (data?.code === Test.NOT_FOUND_BY_TYPE.code) {
                    notify.error(Test.NOT_FOUND_BY_TYPE.message);
                } else if (data?.code === ExamClass.NOT_FOUND_BY_TYPE.code) {
                    notify.error(`${ExamClass.NOT_FOUND_BY_TYPE.message} ${error?.response?.fieldError}`);
                } else {
                    notify.error("Đã có lỗi xảy ra!")
                }
                setAssignLoading(false);
            }).then (() => {});
    };

    /**
     * publish test-set
     */
    const publishStudentTestSet = (examClassId, isPublished) => {
        setPublishLoading(true);
        publishStudentTestSetService(examClassId, isPublished,
            (res) => {
                if (res?.status === HttpStatusCode.Ok) {
                    setPublishLoading(false)
                    notify.success("Mở bài thi thành cho lớp thi thành công!")
                }
            },
            (error) => {
                if (error?.status === HttpStatusCode.Forbidden) {
                    notify.error(System.FORBIDDEN.message)
                } else {
                    notify.error("Đã có lỗi xảy ra!")
                }
                setPublishLoading(false);
            }).then(() => {
        });
    };

    /**
     * send exam class result to email
     */
    const sendEmailResultExamClass = (examClassId, payload) => {
        setSendEmailLoading(true);
        sendEmailExamClassResultService(examClassId, payload,
            (res) => {
                if (res?.status === HttpStatusCode.Ok) {
                    setSendEmailLoading(false)
                    notify.success("Gửi email danh sách lớp thi thành công !")
                }
            },
            (error) => {
                setSendEmailLoading(false);
                notify.error(processApiError(error));
            }).then(() => {
        });
    };

    return {
        allExamClasses,
        getAllExamClasses,
        tableLoading,
        pagination,
        getExamClassDetail,
        examClassInfo,
        infoLoading,
        getParticipants,
        participants,
        partiLoading,
        getResult,
        resultLoading,
        resultData,
        dataPieChart,
        dataColumnChart,
        isPublishedAll,
        isHandled,
        assignLoading,
        assignStudentTestSet,
        publishLoading,
        publishStudentTestSet,
        sendEmailLoading,
        sendEmailResultExamClass
    };
};

export default useExamClasses;
