import {
	deleteRequest,
	getRequest,
	postRequest,
	putRequest,
} from "../api/apiCaller";
import { apiPath } from "../config/apiPath";
import {buildUrlParamQuery} from "../utils/apiUtils";
export const testRandomService = async (
	params,
	successCallback,
	errorCallback
) => {
	await postRequest(
		`${apiPath.testRandomCreate}`,
		params,
		successCallback,
		errorCallback
	);
};
export const testService = async (params, successCallback, errorCallback) => {
	await postRequest(
		`${apiPath.testCreate}`,
		params,
		successCallback,
		errorCallback
	);
};
export const testSetCreateService = async (
	params,
	successCallback,
	errorCallback
) => {
	await postRequest(
		`${apiPath.testSetCreate}`,
		params,
		successCallback,
		errorCallback
	);
};
export const testSetDetailService = async (
	params,
	successCallback,
	errorCallback
) => {
	await postRequest(
		`${apiPath.testSetDetail}`,
		params,
		successCallback,
		errorCallback
	);
};
export const getTestsService = async (
	subjectId,
	semesterId,
	testType,
	page,
	size,
	successCallback,
	errorCallback
) => {
	const params = { page, size, subjectId, semesterId, testType };
	const queryString = buildUrlParamQuery(params);

	const apiUrl = `${apiPath.allTest}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};
export const deleteTestService = async (
	testId,
	params,
	successCallback,
	errorCallback
) => {
	await deleteRequest(
		`${apiPath.deleteTest}/${testId}`,
		params,
		successCallback,
		errorCallback
	);
};
export const updateTestSetService = async (
	params,
	successCallback,
	errorCallback
) => {
	await putRequest(
		`${apiPath.updateTestSet}`,
		params,
		successCallback,
		errorCallback
	);
};
export const createTestSetService = async (
	params,
	successCallback,
	errorCallback
) => {
	await postRequest(
		`${apiPath.testSetManual}`,
		params,
		successCallback,
		errorCallback
	);
};

export const getListQuestionAllowedInTestService = async (
	testId,
	successCallback,
	errorCallback
) => {
	const apiUrl = `${apiPath.listAllowedQuestions}/${testId}`;

	await getRequest(apiUrl, {}, successCallback, errorCallback);
};

export const getTestDetailService = async (
	testId,
	successCallback,
	errorCallback
) => {
	const apiUrl = `${apiPath.detailsTest}/${testId}`;

	await getRequest(apiUrl, {}, successCallback, errorCallback);
};

export const updateTestService = async (
	testId,
	payload,
	successCallback,
	errorCallback
) => {
	const apiUrl = `${apiPath.updateTest}/${testId}`;

	await putRequest(apiUrl, payload, successCallback, errorCallback);
};

export const getListTestSetService = async (
	testId,
	successCallback,
	errorCallback
) => {
	const apiUrl = `${apiPath.testSetList}?testId=${testId}`;

	await getRequest(apiUrl, {}, successCallback, errorCallback);
};

export const deleteTestSetService = async (
	testSetId,
	successCallback,
	errorCallback
) => {
	const apiUrl = `${apiPath.deleteTestSet}/${testSetId}`;

	await deleteRequest(apiUrl, {}, successCallback, errorCallback);
};
