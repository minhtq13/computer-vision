import {useState} from "react";
import useNotify from "./useNotify";
import {
    getTestsService,
    testSetDetailService,
    updateTestSetService,
    deleteTestService,
    getListQuestionAllowedInTestService,
    getTestDetailService,
    updateTestService,
    getListTestSetService, deleteTestSetService
} from "../services/testServices";
import {HttpStatusCode} from "axios";
import {errorCodeMap, Test} from "../common/apiErrorCode"
import {useNavigate} from "react-router-dom";
import {appPath} from "../config/appPath";

const useTest = () => {
    const notify = useNotify();
    const navigate = useNavigate();
    const [allTest, setAllTest] = useState([]);
    const [tableLoading, setTableLoading] = useState(true);
    const [pagination, setPagination] = useState({});
    const [testSetDetail, setTestSetDetail] = useState({});
    const [detailLoading, setDetailLoading] = useState(false);
    const [editLoading, setEditLoading] = useState(false);
    const [deleteLoading, setDeleteLoading] = useState(false);
    const [testQuestionLoading, setTestQuestionLoading] = useState(false);
    const [allAllowedQuestions, setAllAllowedQuestions] = useState([]);
    const [testDetailsLoading, setTestDetailsLoading] = useState(false);
    const [testDetails, setTestDetails] = useState(null);
    const [updateTestLoading, setUpdateTestLoading] = useState(false);
    const [listTestSet, setListTestSet] = useState([]);
    const [listTestSetLoading, setListTestSetLoading] = useState(false);
    const [deleteTestSetLoading, setDeleteTestSetLoading] = useState(false);

    const getAllTests = (param) => {
        setTableLoading(true);
        getTestsService(
            param.subjectId,
            param.semesterId,
            param.testType,
            param.page,
            param.size,
            (res) => {
                setAllTest(res?.data.content);
                setPagination({
                    current: res?.data?.pageable.pageNumber + 1,
                    pageSize: res?.data?.pageable.pageSize,
                    total: res?.data?.totalElements,
                });
                setTableLoading(false);
            },
            (err) => {
                setTableLoading(true);
                if (err?.response?.status === 404) {
                    notify.warning(
                        err.response.data.message ||
                        "No information in database"
                    );
                }
            }
        ).then(() => {
        });
    };

    const getListQuestionAllowedInTest = (param) => {
        setTestQuestionLoading(true);
        getListQuestionAllowedInTestService(
            param.testId,
            (res) => {
                setTestQuestionLoading(false);
                setAllAllowedQuestions(res?.data?.questions);
            },
            () => {
                setTestQuestionLoading(false);
                notify.error("Lỗi lấy danh sách câu hỏi!");
            }
        ).then(() => {});
    }

    const getTestDetails = (param) => {
        setTestDetailsLoading(true);
        getTestDetailService(
            param.testId,
            (res) => {
                setTestDetailsLoading(false);
                setTestDetails(res?.data);
            },
            () => {
                setTestDetailsLoading(false);
                notify.error("Lỗi lấy thông tin chi tiết kỳ thi!");
            }
        ).then(() => {});
    }

    const updateTestSet = (param) => {
        setEditLoading(true);
        updateTestSetService(
            param,
            () => {
                setEditLoading(false);
                notify.success("Cập nhật đề thi thành công!");
            },
            () => {
                setEditLoading(false);
                notify.error("Lỗi cập nhật đề thi!");
            }
        ).then(() => {
        });
    };

    const getTestSetDetail = (param) => {
        setDetailLoading(true);
        testSetDetailService(
            param,
            (res) => {
                setTestSetDetail(res?.data);
                setDetailLoading(false);
            },
            () => {
                notify.error("Không thể lấy thông tin đề thi!");
            }
        ).then(() => {
        });
    };

    const deleteTest = (testId) => {
        setDeleteLoading(true);
        deleteTestService(
            testId,
            {},
            () => {
                notify.success("Xóa bộ đề thi thành công!");
                setDeleteLoading(false);
            },
            (error) => {
                let data = error?.data;
                if (error?.status === HttpStatusCode.Forbidden) {
                    notify.error("Bạn không có quyền xóa bộ đề thi");
                } else if (data?.code === Test.NOT_FOUND.code) {
                    notify.error(Test.NOT_FOUND.message);
                } else if (data?.code === Test.USED_IN_EXAM_CLASSES.code) {
                    notify.error(Test.USED_IN_EXAM_CLASSES.message);
                } else if (data?.code === Test.ASSIGNED_OR_HANDLED_BY_STUDENTS.code) {
                    notify.error(Test.ASSIGNED_OR_HANDLED_BY_STUDENTS.message);
                } else {
                    notify.error("Đã có lỗi xảy ra");
                }
                setDeleteLoading(false);
            }
        ).then(() => {
        });
    };

    // update test: config
    const updateTest = (testId, body) => {
        setUpdateTestLoading(true);
        updateTestService(testId, body,
            () => {
                setUpdateTestLoading(false);
                notify.success("Cập nhật kỳ thi thành công!");
                navigate(appPath.testList);
            },
            () => {
                setUpdateTestLoading(false)
                notify.error("Lỗi cập kỳ thi!")
            }
        ).then(() => {
        });
    };

    const getListTestSet = (param) => {
        setListTestSetLoading(true);
        getListTestSetService(
            param.testId,
            (res) => {
                setListTestSetLoading(false);
                setListTestSet(res?.data);
            },
            () => {
                setListTestSetLoading(false);
                notify.error("Lỗi lấy danh sách đề thi!");
            }
        ).then(() => {});
    }

    const deleteTestSet = (param) => {
        setDeleteTestSetLoading(true);
        deleteTestSetService(
            param?.testSetId,
            () => {
                setDeleteTestSetLoading(false);
            },
            (error) => {
                setDeleteTestSetLoading(false);
                let data = error?.response?.data;
                if (error?.response?.status === HttpStatusCode.Forbidden) {
                    notify.error("Không có quyền truy cập");
                } else if (errorCodeMap.get(data?.code) !== undefined) {
                    notify.error(errorCodeMap.get(data?.code)?.message);
                } else {
                    notify.error("Lỗi xóa đề thi!");
                }
            }
        ).then(() => {});
    }

    return {
        allTest,
        tableLoading,
        getAllTests,
        pagination,
        updateTestSet,
        getTestSetDetail,
        testSetDetail,
        detailLoading,
        editLoading,
        deleteLoading,
        deleteTest,
        getListQuestionAllowedInTest,
        testQuestionLoading,
        allAllowedQuestions,
        testDetailsLoading,
        getTestDetails,
        testDetails,
        updateTest,
        updateTestLoading,
        getListTestSet,
        listTestSet,
        listTestSetLoading,
        deleteTestSet,
        deleteTestSetLoading
    };
};
export default useTest;
