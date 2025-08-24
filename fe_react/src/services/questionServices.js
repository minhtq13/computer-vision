import {
	postRequest,
	getRequest,
	deleteRequest,
	putRequest,
} from "../api/apiCaller";
import { apiPath } from "../config/apiPath";
export const addQuestionService = async (
	params,
	successCallback,
	errorCallback
) => {
	await postRequest(
		`${apiPath.addQuestion}`,
		params,
		successCallback,
		errorCallback
	);
};
export const getQuestionService = async (
	subjectId,
	subjectCode,
	chapterCode,
	chapterIds,
	level,
	search,
	testId,
	fetchSize,
	successCallback,
	errorCallback
) => {
	const params = {
		level,
	};

	if (subjectId) {
		params.subjectId = subjectId;
	}

	if (chapterIds) {
		params.chapterIds = chapterIds;
	}

	if (search) {
		params.search = search;
	}

	if (testId) {
		params.testId = testId;
	}

	if (fetchSize) {
		params.fetchSize = fetchSize;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.getQuestionbyCode}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const deleteQuesionsService = async (
	questionId,
	params,
	successCallback,
	errorCallback
) => {
	await deleteRequest(
		`${apiPath.deleteQuestion}/${questionId}`,
		params,
		successCallback,
		errorCallback
	);
};
export const updateQuesionsService = async (
	questionId,
	params,
	successCallback,
	errorCallback
) => {
	await putRequest(
		`${apiPath.updateQuestion}/${questionId}`,
		params,
		successCallback,
		errorCallback
	);
};
export const getQuestionDetailsService = async (
	questionId,
	params,
	successCallback,
	errorCallback
) => {
	await getRequest(
		`${apiPath.getQuestionDetail}/${questionId}`,
		params,
		successCallback,
		errorCallback
	);
};
