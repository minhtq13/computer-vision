import { useState } from "react";
import {
	getComboChapService,
	getComboExamClassService,
	getComboSemesterServices,
	getComboStudentServices,
	getComboSubjectService,
	getComboViewableSubjectService,
	getComboTeacherServices,
	getComboTestService, getComboDepartmentService,
} from "../services/comboServices";
import useNotify from "./useNotify";
import {HttpStatusCode} from "axios";
const useCombo = () => {
	const [allSubjects, setAllSubjects] = useState([]);
	const [allChapters, setAllChapters] = useState([]);
	const [subLoading, setSubLoading] = useState(false);
	const [chapterLoading, setChapterLoading] = useState(false);
	const [allSemester, setAllSemester] = useState([]);
	const [semesterLoading, setSemesterLoading] = useState(false);
	const [allStudent, setAllStudent] = useState([]);
	const [studentLoading, setStudentLoading] = useState(false);
	const [allTeacher, setAllTeacher] = useState([]);
	const [teacherLoading, setTeacherLoading] = useState(false);
	const [allTest, setAllTest] = useState([]);
	const [testLoading, setTestLoading] = useState(false);
	const [examClass, setExamClass] = useState([]);
	const [examClassLoading, setExamClassLoading] = useState(false);
	const [allDepartment, setAllDepartment] = useState([]);
	const [departmentLoading, setDepartmentLoading] = useState(false);

	const notify = useNotify();
	const getAllSubjects = (payload) => {
		setSubLoading(true);
		getComboSubjectService(
			payload.subjectCode,
			payload.subjectTitle,
			payload.departmentIds,
			(res) => {
				setAllSubjects(res?.data);
				setSubLoading(false);
			},
			(error) => {
				if (error?.status === HttpStatusCode.Unauthorized) {
					notify.error("Token hết hạn");
				} else {
					notify.error("Lỗi lấy danh sách học phần!");
				}
				setSubLoading(false);
			}
		).then(() => {});
	};

	const getAllViewableSubject = (payload) => {
		setSubLoading(true);
		getComboViewableSubjectService(
			payload?.subjectCode,
			payload?.subjectTitle,
			payload?.targetObject,
			(res) => {
				setAllSubjects(res?.data);
				setSubLoading(false);
			},
			(error) => {
				if (error?.status === HttpStatusCode.Unauthorized) {
					notify.error("Token hết hạn");
				} else {
					notify.error("Lỗi lấy danh sách học phần!");
				}
				setSubLoading(false);
			}
		).then(() => {});
	};

	const getAllChapters = (payload) => {
		setChapterLoading(true);
		getComboChapService(
			payload.subjectId,
			payload.chapterCode,
			payload.chapterTitle,
			(res) => {
				setAllChapters(res?.data);
				setChapterLoading(false);
			},
			() => {
				setChapterLoading(false);
				notify.error("Không thể lấy danh sách các chương!");
			}
		).then(() => {});
	};
	const getAllSemesters = (payload) => {
		setSemesterLoading(true);
		getComboSemesterServices(
			payload.search,
			(res) => {
				setAllSemester(res?.data);
				setSemesterLoading(false);
			},
			() => {
				setSemesterLoading(false);
				notify.error("Không thể lấy danh sách kỳ học!");
			}
		).then(() => {});
	};

	const getAllStudent = (payload) => {
		setStudentLoading(true);
		getComboStudentServices(
			payload.studentName,
			payload.studentCode,
			(res) => {
				setAllStudent(res?.data);
				setStudentLoading(false);
			},
			() => {
				setStudentLoading(false);
				notify.error("Không thể lấy danh sách sinh viên!");
			}
		).then(() => {});
	};

	const getAllTeacher = (payload) => {
		setTeacherLoading(true);
		getComboTeacherServices(
			payload.teacherName,
			payload.teacherCode,
			(res) => {
				setAllTeacher(res?.data);
				setTeacherLoading(false);
			},
			() => {
				setTeacherLoading(false);
				notify.error("Không thể lấy danh sách giảng viên!");
			}
		).then(() => {});
	};
	const getAllTest = (payload) => {
		setTestLoading(true);
		getComboTestService(
			payload.testName,
			payload.testCode,
			(res) => {
				setAllTest(res?.data);
				setTestLoading(false);
			},
			() => {
				notify.error("Không thể lấy danh sách kỳ thi!");
			}
		).then(() => {});
	};
	const getAllExamClass = (semesterId, subjectId, testType, search, payload) => {
		setExamClassLoading(true);
		getComboExamClassService(
			semesterId,
			subjectId,
			testType,
			search,
			payload,
			(res) => {
				setExamClass(res?.data);
				setExamClassLoading(false);
			},
			() => {
				notify.error("Không thể lấy danh sách lớp thi!");
			}
		).then(() => {});
	};

	const getAllDepartment = (search) => {
		setDepartmentLoading(true);
		getComboDepartmentService(
			{
				search: search,
			},
			(res) => {
				setAllDepartment(res?.data);
				setDepartmentLoading(false);
			},
			() => {
				notify.error("Không thể lấy danh sách đơn vị quản lý!");
			}
		).then(() => {});
	};

	return {
		subLoading,
		chapterLoading,
		allSubjects,
		getAllSubjects,
		getAllViewableSubject,
		allChapters,
		getAllChapters,
		getAllSemesters,
		allSemester,
		semesterLoading,
		getAllStudent,
		studentLoading,
		teacherLoading,
		allStudent,
		allTeacher,
		getAllTeacher,
		getAllTest,
		testLoading,
		allTest,
		getAllExamClass,
		examClass,
		examClassLoading,
		allDepartment,
		getAllDepartment,
		departmentLoading
	};
};
export default useCombo;
