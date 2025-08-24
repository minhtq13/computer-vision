import {useState} from "react";
import {
    getChaptersService, getDetailSubjectService,
    getPagingSubjectsService, queryAllSubjectDetail,
    queryAllSubjectsService
} from "../services/subjectsService";
import useNotify from "./useNotify";
import {processApiError} from "../utils/apiUtils";

const useSubjects = () => {
    const notify = useNotify();
    const [allSubjects, setAllSubjects] = useState([]);
    const [tableLoading, setTableLoading] = useState(true);
    const [pagination, setPagination] = useState({});
    const [subjectInfo, setSubjectInfo] = useState({});
    const [infoLoading, setInfoLoading] = useState(true);
    const [allChapters, setAllChapters] = useState([]);
    const [chapterLoading, setChapterLoading] = useState(false);

    const getAllSubjects = (payload) => {
        setTableLoading(true);
        getPagingSubjectsService(
            payload.search,
            payload.page,
            payload.size,
            payload.sort,
            (res) => {
                setAllSubjects(res?.data?.content);
                setPagination({
                    current: res?.data?.pageable?.pageNumber + 1,
                    pageSize: res?.data?.pageable?.pageSize,
                    total: res?.data?.totalElements,
                });
                setTableLoading(false);
            },
            (err) => {
                setTableLoading(false);
                if (err?.response?.status === 404) {
                    notify.warning(
                        err?.response?.data?.message ||
                        "No information in database"
                    );
                }
            }
        ).then(() => {
        });
    };

    const queryAllSubjects = (payload) => {
        setTableLoading(true);
        const fields = ["id", "code", "title", "credit", "departmentName", "modifiedAt"]
        queryAllSubjectsService(
            payload.search,
            payload.page,
            payload.size,
            payload.sort,
            fields.join(", "),
            (res) => {
                const resBody = res?.data?.data?.subjectList;
                setAllSubjects(resBody?.content);
                setPagination({
                    current: resBody?.pageable?.pageNumber + 1,
                    pageSize: resBody?.pageable?.pageSize,
                    total: resBody?.totalElements,
                });
                setTableLoading(false);
            },
            (err) => {
                setTableLoading(false);
                notify.error(processApiError(err));
            }
        ).then(() => {
        });
    };

    const getDetailSubject = (payload = {}, id) => {
        setInfoLoading(true);
        getDetailSubjectService(
            id,
            payload,
            (res) => {
                setSubjectInfo(res?.data);
                setInfoLoading(false);
            },
            (error) => {
                notify.error(processApiError(error, "Không lấy được thông tin chi tiết học phần"));
            }
        ).then(() => {
        });
    };

    const querySubjectDetail = (payload = {}, id) => {
        setInfoLoading(true);
        const fields = `id, code, title, credit, description, departmentId, departmentName, modifiedAt, chapters {id, code, title, orders}`;
        queryAllSubjectDetail(
            id,
            fields,
            (res) => {
                setSubjectInfo(res?.data?.data?.subjectDetail);
                setInfoLoading(false);
            },
            (error) => {
                notify.error(processApiError(error, "Không lấy được thông tin chi tiết học phần"));
            }
        ).then(() => {
        });
    };

    const getAllChapters = (payload = {}, code) => {
        setChapterLoading(true);
        getChaptersService(
            code,
            payload,
            (res) => {
                setAllChapters(res?.data);
                setChapterLoading(false);
            },
            (err) => {
                setChapterLoading(false);
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
    return {
        allSubjects,
        getAllSubjects,
        queryAllSubjects,
        tableLoading,
        subjectInfo,
        getDetailSubject,
        querySubjectDetail,
        infoLoading,
        allChapters,
        chapterLoading,
        getAllChapters,
        pagination,
    };
};

export default useSubjects;
