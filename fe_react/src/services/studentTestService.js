import {
    getRequest, putRequest
} from "../api/apiCaller";
import { apiPath } from "../config/apiPath";
import {buildUrlParamQuery} from "../utils/apiUtils";

// get list student test
export const getListStudentTestService = async (
    params,
    successCallback,
    errorCallback
) => {
    let queryString = buildUrlParamQuery(params);
    let url = `${apiPath.studentOpeningTestList}?${queryString}`;
    await getRequest(url, {}, successCallback, errorCallback);
}
// get details studentTestSet => handle view
export const getStudentTestSetDetailsService = async (
    params, successCallback, errorCallback) => {
    let url = `${apiPath.studentTestDetails}/${params?.id}`;
    await getRequest(url, {}, successCallback, errorCallback);
};

// get details test set => handle view
export const loadTestSetDetailsService = async (
    params, successCallback, errorCallback) => {
    let url = `${apiPath.loadStudentTestSet}/${params?.studentTestSetId}/${params?.testSetId}`;
    await getRequest(url, {}, successCallback, errorCallback);
};

// start attempt test
export const startAttemptService = async (data, successCallback, errorCallback) => {
    await putRequest(apiPath.studentTestAttempt, data, successCallback, errorCallback);
};

// saved temp submission
export const saveTempSubmissionService = async (data, successCallback, errorCallback) => {
    await putRequest(apiPath.studentTestSavedTempSubmission, data, successCallback, errorCallback);
};

// submit test
export const submitService = async (data, successCallback, errorCallback) => {
    await putRequest(apiPath.studentTestSubmit, data, successCallback, errorCallback);
};