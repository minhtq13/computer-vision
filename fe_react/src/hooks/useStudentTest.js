import {useState} from "react";
import {
    getListStudentTestService,
    getStudentTestSetDetailsService,
    loadTestSetDetailsService,
    startAttemptService,
    saveTempSubmissionService,
    submitService
} from "../services/studentTestService";
import {HttpStatusCode} from "axios";
import useNotify from "./useNotify";
import {processApiError} from "../utils/apiUtils";
import {useNavigate} from "react-router-dom";
import {appPath} from "../config/appPath";

const useStudentTest = () => {

    const notify = useNotify();
    const [allOpeningStudentTest, setAllOpeningStudentTest] = useState([]);
    const [listLoading, setListLoading] = useState(false);
    const [pagination, setPagination] = useState({current: 1, pageSize: 10, total: 100});
    const [stdTestSetDetails, setStdTestSetDetails] = useState(null);
    const [stdTestSetDetailsLoading, setStdTestSetDetailsLoading] = useState(false);
    const [testSetDetails, setTestSetDetails] = useState([]);
    const [testSetDetailsLoading, setTestSetDetailsLoading] = useState(false);
    const [tempSavedData, setTempSavedData] = useState(null);
    const [tempSaveLoading, setTempSaveLoading] = useState(false);
    const [submitLoading, setSubmitLoading] = useState(false);
    const navigate = useNavigate();

    const getOpeningStudentTestList = (params) => {
        setListLoading(true);
        getListStudentTestService(params,
            (response) => {
                setListLoading(false);
                setPagination({
                    current: response?.data?.pageable.pageNumber + 1,
                    pageSize: response?.data?.pageable.pageSize,
                    total: response?.data?.totalElements,
                });
                setAllOpeningStudentTest(response?.data?.content);
            }, (error) => {
                setListLoading(false);
                if (error?.status === HttpStatusCode.Forbidden) {
                    notify.error("Không có quyền truy cập!")
                } else {
                    notify.error("Lỗi lấy danh sách bài thi!")
                }
            })
            .then(() => {
            });
    }

    /**
     * get student test details
     */
    const getStudentTestSetDetails = (params) => {
        setStdTestSetDetailsLoading(true);
        getStudentTestSetDetailsService({id: params?.id}, (response) => {
            setStdTestSetDetailsLoading(false);
            setStdTestSetDetails(response?.data);
        }, (error) => {
            notify.error(processApiError(error));
            setStdTestSetDetailsLoading(false);
        }).then(() => {
        });
    }

    /**
     * get student test details
     */
    const loadTestSetDetails = (params) => {
        setTestSetDetailsLoading(true);
        loadTestSetDetailsService(params, (response) => {
            setTestSetDetailsLoading(false);
            setTestSetDetails(response?.data);
        }, (error) => {
            notify.error(processApiError(error));
            setTestSetDetailsLoading(false);
        }).then(() => {});
    }

    /**
     * start test
     */
    const startAttemptTest = (submissionData) => {
        setTempSaveLoading(true);
        startAttemptService(submissionData, (response) => {
            setTempSaveLoading(false);
            setTempSavedData(response?.data);
        }, (error) => {
            setTempSaveLoading(false);
            notify.error(processApiError(error));
        }).then(() => {})
    }

    /**
     * save temp submission
     */
    const saveTempSubmission = (submissionData) => {
        setTempSaveLoading(true);
        saveTempSubmissionService(submissionData, (response) => {
            setTempSaveLoading(false);
            setTempSavedData(response?.data);
        }, (error) => {
            setTempSaveLoading(false);
            notify.error(processApiError(error));
        }).then(() => {})
    }

    /**
     * submit current submission
     */
    const submitTest = (submissionData) => {
        setSubmitLoading(true);
        submitService(submissionData, (response) => {
            setSubmitLoading(false);
            setTempSavedData(response?.data);
            notify.success("Nộp bài thi Online thành công!");
            // if successful -> navigate to list
            navigate(appPath.studentTestList);
        }, (error) => {
            setSubmitLoading(false);
            notify.error(processApiError(error));
        }).then(() => {})
    }

    return {
        pagination,
        allOpeningStudentTest,
        getOpeningStudentTestList,
        listLoading,
        getStudentTestSetDetails,
        stdTestSetDetails,
        stdTestSetDetailsLoading,
        testSetDetailsLoading,
        testSetDetails,
        loadTestSetDetails,
        tempSavedData,
        tempSaveLoading,
        startAttemptTest,
        saveTempSubmission,
        submitTest,
        submitLoading
    }

}
export default useStudentTest;