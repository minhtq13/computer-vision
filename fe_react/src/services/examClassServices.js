import {
	getRequest,
	postRequest,
	putRequest,
	deleteRequest,
} from "../api/apiCaller";
import { apiPath } from "../config/apiPath";
export const getPagingExamClassService = async (
	code,
	subjectId,
	semesterId,
	page,
	size,
	sort,
	successCallback,
	errorCallback
) => {
	const params = {
		page,
		size,
		sort,
	};

	if (code) {
		params.code = code;
	}

	if (subjectId) {
		params.subjectId = subjectId;
	}

	if (semesterId) {
		params.semesterId = semesterId;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.pageExamClasses}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};
export const examClassDetails = async (
	classId,
	params,
	successCallback,
	errorCallback
) => {
	await putRequest(
		`${apiPath.examClassDetail}/${classId}`,
		params,
		successCallback,
		errorCallback
	);
};
export const addExamClassService = async (
	params,
	successCallback,
	errorCallback
) => {
	await postRequest(
		`${apiPath.examClassCreate}`,
		params,
		successCallback,
		errorCallback
	);
};
export const deleteExamClassService = async (
	classId,
	params,
	successCallback,
	errorCallback
) => {
	await deleteRequest(
		`${apiPath.disableExamClass}/${classId}`,
		params,
		successCallback,
		errorCallback
	);
};
export const getExamClassDetailService = async (
	classId,
	successCallback,
	errorCallback
) => {
	await getRequest(
		`${apiPath.examClassDetail}/${classId}`,
		{},
		successCallback,
		errorCallback
	);
};
export const updateExamClassService = async (
	classId,
	params,
	successCallback,
	errorCallback
) => {
	await putRequest(
		`${apiPath.updateExamClass}/${classId}`,
		params,
		successCallback,
		errorCallback
	);
};
export const getParticipantServices = async (
	classId,
	roleType,
	successCallback,
	errorCallback
) => {
	const params = { roleType };
	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.getParticipant}/${classId}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const getExamClassResultService = async (
	examClassCode,
	params,
	successCallback,
	errorCallback
) => {
	await getRequest(
		`${apiPath.getExamClassResult}/${examClassCode}`,
		params,
		successCallback,
		errorCallback
	);
};

/**
 * Assign test-set to students in exam class
 */
export const assignStudentTestSetService = async (
    examClassId,
    successCallback,
	errorCallback) => {
	await putRequest(`${apiPath.assignTestSet}/${examClassId}`, {}, successCallback, errorCallback);
};


/**
 * Publish test-set to students in exam class
 */
export const publishStudentTestSetService = async (
	examClassId,
	isPublished,
	successCallback,
	errorCallback) => {
	const params = {isPublished}
	const queryStr = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");
	await putRequest(`${apiPath.publishTestSet}/${examClassId}?${queryStr}`, {}, successCallback, errorCallback);
};

/**
 * Publish test-set to students in exam class
 */
export const sendEmailExamClassResultService = async (
	examClassId,
	payload,
	successCallback,
	errorCallback) => {
	await postRequest(`${apiPath.sendEmailExamClassResult}/${examClassId}`, payload, successCallback, errorCallback);
};