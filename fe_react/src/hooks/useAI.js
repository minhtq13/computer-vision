import {useState} from "react";
import {
    deleteImgInFolderService,
    getImgInFolderService,
    getModelAIService,
    resetTableResultService,
    saveTableResultService,
    loadLatestTempScoredDataService
} from "../services/aiServices";
import useNotify from "./useNotify";
import {useDispatch} from "react-redux";
import {setRefreshTableImage} from "../redux/slices/refreshSlice";
import {processApiError} from "../utils/apiUtils";

const useAI = () => {
    const notify = useNotify();
    const dispatch = useDispatch();
    const [resultAI, setResultAI] = useState([]);
    const [tempFileCode, setTempFileCode] = useState();
    const [imgInFolder, setImgInFolder] = useState([]);
    const [mayBeWrong, setMayBeWrong] = useState([]);
    const [loading, setLoading] = useState(false);
    const [loadingSaveResult, setLoadingSaveResult] = useState(false);
    const [firstResultLoading, setFirstResultLoading] = useState(false);
    const [examClassStudentCodes, setExamClassStudentCodes] = useState([]);

    const getModelAI = (examClassCode, payload) => {
        setLoading(true);
        getModelAIService(
            examClassCode,
            payload,
            (res) => {
                setLoading(false);
                setResultAI(res?.data?.previews);
                setTempFileCode(res?.data?.tmpFileCode);
                setMayBeWrong(res?.data?.warningMessages)
            },
            (err) => {
                console.log(err);
                setLoading(false);
                if (err?.response?.data?.code === "error.test.set.not.found") {
                    notify.warning("Không có mã đề thi trong cơ sở dữ liệu!");
                } else if (err?.response?.data?.code === "error.student.exam.class.not_found") {
                    notify.warning("Mã số sinh viên trong ảnh chấm không có trong lớp thi!");
                }
            }
        ).then(() => {});
    };
    const resetTableResult = (payload, noti = true) => {
        if (tempFileCode) {
            resetTableResultService(
                payload?.examClassCode,
                tempFileCode,
                payload,
                () => {
                    if (noti) {
                        notify.success("Đã xóa dữ liệu của tạm thời thành công!");
                    }
                },
                () => {
                    notify.warning("Không tìm thấy dữ liệu");
                }
            ).then(() => {});
        }
    };
    const saveTableResult = (payload) => {
        setLoadingSaveResult(true)
        saveTableResultService(
            payload?.examClassCode,
            tempFileCode,
            payload,
            () => {
                setLoadingSaveResult(false);
                notify.success("Gửi yêu cầu lưu kết quả chấm thành công! Vui lòng chờ vài phút để kết quả được lưu.");
            },
            () => {
                setLoadingSaveResult(false);
                notify.warning("Đã tồn tại sinh viên có điểm!");
            }
        ).then(() => {
        });
    };
    const getImgInFolder = (examClassCode, payload) => {
        getImgInFolderService(
            examClassCode,
            payload,
            (res) => {
                setImgInFolder(res?.data);
            },
            () => {
                notify.warning("Không tìm thấy ảnh trong folder!");
            }
        ).then(() => {
        });
    };

    const loadLatestTempScoredData = (examClassCode, studentCodes) => {
        setFirstResultLoading(true);
        loadLatestTempScoredDataService(
            examClassCode,
            studentCodes,
            (res) => {
                setExamClassStudentCodes(res?.data?.studentCodes);
                setResultAI(res?.data?.previews);
                setTempFileCode(res?.data?.tmpFileCode);
                setMayBeWrong(res?.data?.warningMessages);
                setFirstResultLoading(false);
            },
            (error) => {
                setFirstResultLoading(false);
                notify.error(processApiError(error));
            }
        ).then(() => {
        });
    };
    const deleteImgInFolder = (payload) => {
        deleteImgInFolderService(
            payload,
            () => {
                notify.success("Xoá thành công!");
                dispatch(setRefreshTableImage(Date.now()));
            },
            (err) => {
                notify.warning("Xoá không thành công!");
            }
        ).then(() => {
        });
    };

    return {
        resultAI,
        setResultAI,
        tempFileCode,
        examClassStudentCodes,
        setExamClassStudentCodes,
        getModelAI,
        mayBeWrong,
        setMayBeWrong,
        resetTableResult,
        saveTableResult,
        getImgInFolder,
        imgInFolder,
        setImgInFolder,
        deleteImgInFolder,
        loading,
        loadingSaveResult,
        loadLatestTempScoredData,
        firstResultLoading
    };
};

export default useAI;
