import {
	getRequest,
	postRequest,
	putRequest,
	deleteRequest,
} from "../api/apiCaller";
import { apiPath } from "../config/apiPath";
export const getAllTeachersService = async (
	params,
	successCallback,
	errorCallback
) => {
	await getRequest(
		`${apiPath.allTeachers}`,
		params,
		successCallback,
		errorCallback
	);
};
export const getPagingTeachersService = async (
	search,
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

	if (search) {
		params.search = search;
	}

	const queryString = Object.entries(params)
		.map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
		.join("&");

	const apiUrl = `${apiPath.pageTeacher}?${queryString}`;

	await getRequest(apiUrl, null, successCallback, errorCallback);
};
export const updateTeachersService = async (
	teacherId,
	params,
	successCallback,
	errorCallback
) => {
	await putRequest(
		`${apiPath.updateTeacher}/${teacherId}`,
		params,
		successCallback,
		errorCallback
	);
};
export const addTeachersService = async (
	params,
	successCallback,
	errorCallback
) => {
	await postRequest(
		`${apiPath.addTeacher}`,
		params,
		successCallback,
		errorCallback
	);
};
export const deleteTeachersService = async (
	teacherId,
	params,
	successCallback,
	errorCallback
) => {
	await deleteRequest(
		`${apiPath.deleteTeacher}/${teacherId}`,
		params,
		successCallback,
		errorCallback
	);
};
