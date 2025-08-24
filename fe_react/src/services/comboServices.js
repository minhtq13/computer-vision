import { getRequest } from "../api/apiCaller";
import { apiPath } from "../config/apiPath";

export const getComboSubjectService = async (
	subjectCode,
	subjectTitle,
	departmentIds,
	successCallback,
	errorCallback
) => {
	const params = {};

	if (subjectCode) {
		params.subjectCode = subjectCode;
	}

	if (subjectTitle) {
		params.subjectTitle = subjectTitle;
	}

	if (departmentIds) {
		params.departmentIds = departmentIds;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.comboSubject}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const getComboViewableSubjectService = async (
	subjectCode,
	subjectTitle,
	targetObject,
	successCallback,
	errorCallback
) => {
	const params = {};

	if (subjectCode) {
		params.subjectCode = subjectCode;
	}

	if (subjectTitle) {
		params.subjectTitle = subjectTitle;
	}

	if (targetObject) {
		params.targetObject = targetObject;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.comboViewableSubject}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const getComboChapService = async (
	subjectId,
	chapterCode,
	chapterTitle,
	successCallback,
	errorCallback
) => {
	const params = { subjectId };

	if (chapterCode) {
		params.chapterCode = chapterCode;
	}

	if (chapterTitle) {
		params.chapterTitle = chapterTitle;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.comboChapter}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const getComboSemesterServices = async (
	search,
	successCallback,
	errorCallback
) => {
	const params = {};

	if (search) {
		params.search = search;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.comboSemester}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const getComboStudentServices = async (
	studentName,
	studentCode,
	successCallback,
	errorCallback
) => {
	const params = {};

	if (studentName) {
		params.studentName = studentName;
	}

	if (studentCode) {
		params.studentCode = studentCode;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.comboStudent}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const getComboTeacherServices = async (
	teacherName,
	teacherCode,
	successCallback,
	errorCallback
) => {
	const params = {};

	if (teacherName) {
		params.teacherName = teacherName;
	}

	if (teacherCode) {
		params.teacherCode = teacherCode;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.comboTeacher}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const getComboTestService = async (params, successCallback, errorCallback) => {
  await getRequest(`${apiPath.comboTest}`, params, successCallback, errorCallback);
};
export const getComboExamClassService = async (semesterId, subjectId, testType, search, params, successCallback, errorCallback) => {
  await getRequest(`${apiPath.comboExamClass}?semesterId=${semesterId}&subjectId=${subjectId}&testType=${testType}&search=${search}`, params, successCallback, errorCallback);
};

export const getComboDepartmentService = async (params, successCallback, errorCallback) => {
	await getRequest(apiPath.comboDepartment, params, successCallback, errorCallback);
};
