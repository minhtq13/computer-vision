import axios from "axios";
import { apiPath, BASE_URL } from "../config/apiPath";
import useNotify from "./useNotify";
import { useState } from "react";
import { capitalizeFirstLetter } from "../utils/tools";

const useImportExport = () => {
  const notify = useNotify();
  const [loadingExport, setLoadingExport] = useState(false);
  const [loadingImport, setLoadingImport] = useState(false);

  const importList = (file, object, refreshData, param) => {
    console.log(file);
    setLoadingImport(true);
    axios
      .post(`${BASE_URL}/user/${object}/import`, file)
      .then(() => {
        notify.success("Tải file thành công!");
        setLoadingImport(false);
        refreshData(param);
      })
      .catch(() => {
        // notify.error(
        // 	capitalizeFirstLetter("Đã tồn tại user trong hệ thống!")
        // );
        setLoadingImport(false);
      });
  };

  const exportList = (params, object) => {
    // object = "student" or "teacher"
    axios({
      url: `${BASE_URL}/user/${object}/export`,
      method: "GET",
      responseType: "blob",
      params,
    })
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response?.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${object}-${Date.now()}.xlsx`);
        document.body.appendChild(link);
        link.click();
      })
      .catch((error) => {
        notify.error(capitalizeFirstLetter(error?.response?.data?.message));
      });
  };
  const exportTestList = (params, nameFile) => {
    // object = "test-set"
    setLoadingExport(true);
    axios({
      url: `${BASE_URL}/test-set/export`,
      method: "POST",
      responseType: "blob",
      data: params,
    })
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `Test-${nameFile}-${Date.now()}.docx`);
        document.body.appendChild(link);
        link.click();
        setLoadingExport(false);
      })
      .catch((error) => {
        notify.error(capitalizeFirstLetter(error?.response?.data?.message));
        setLoadingExport(false);
      });
  };
  const exportExamClass = (semesterId, subjectId, object) => {
    // object = "exam-class"
    setLoadingExport(true);
    axios({
      url: `${BASE_URL}/${object}/export`,
      method: "GET",
      responseType: "blob",
      params: {
        semesterId: semesterId,
        subjectId: subjectId,
      },
    })
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${object}-${Date.now()}.xlsx`);
        document.body.appendChild(link);
        link.click();
        setLoadingExport(false);
        notify.success("Tải danh sách lớp thi thành công!");
      })
      .catch(() => {
        notify.error("Lỗi tải danh sách lớp thi!");
        setLoadingExport(false);
      });
  };
  const exportExamClassStudent = (classCode) => {
    // object = "exam-class"
    setLoadingExport(true);
    axios({
      url: `${BASE_URL}/std-test-set/result/export/${classCode}`,
      method: "GET",
      responseType: "blob",
    })
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${classCode}-${Date.now()}.xlsx`);
        document.body.appendChild(link);
        link.click();
        setLoadingExport(false);
        // notify.success("Tải xuống kết quả thi thành công!");
      })
      .catch(() => {
        notify.error("Lỗi tải kết quả thi!");
        setLoadingExport(false);
      });
  };
  const importStudent = (file, classId, refreshData, roleType) => {
    setLoadingImport(true);
    axios
      .post(`${BASE_URL}/exam-class/participant/student/import/${classId}`, file)
      .then(() => {
        notify.success("Tải file thành công!");
        setLoadingImport(false);
        refreshData(classId, roleType);
      })
      .catch(() => {
        notify.error("Lỗi tải file!");
        setLoadingImport(false);
      });
  };

  const importQuestion = (file) => {
    setLoadingImport(true);
    axios
      .post(apiPath.importQuestion, file)
      .then(() => {
        notify.success("Import câu hỏi thành công!");
        setLoadingImport(false);
      })
      .catch(() => {
        notify.error("Lỗi import câu hỏi!");
        setLoadingImport(false);
      });
  };

  return {
    importList,
    loadingImport,
    exportList,
    loadingExport,
    exportTestList,
    exportExamClass,
    exportExamClassStudent,
    importStudent,
    importQuestion,
  };
};
export default useImportExport;
