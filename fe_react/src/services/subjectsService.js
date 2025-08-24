import {deleteRequest, getRequest, postRequest, putRequest,} from "../api/apiCaller";
import {apiPath} from "../config/apiPath";
import {buildGraphQLQuery} from "../utils/apiUtils";

export const getPagingSubjectsService = async (
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

    const apiUrl = `${apiPath.allSubjects}?${queryString}`;

    await getRequest(apiUrl, null, successCallback, errorCallback);
};

export const queryAllSubjectsService = async (
    search,
    page,
    size,
    sort,
    fields,
    successCallback,
    errorCallback
) => {
    // build request query
    const queryName = `subjectList(search: "${search ?? ""}", departmentId: -1, departmentName: "", page: ${page}, size: ${size}, sort: "${sort}")`
    const contentQuery = `content {${fields}}`;
    const reqQuery = {
        query: buildGraphQLQuery(queryName, contentQuery, true)
    }
    await postRequest(apiPath.graphqlEndpoint, reqQuery, successCallback, errorCallback);
};

export const queryAllSubjectDetail = async (
    id,
    fields,
    successCallback,
    errorCallback
) => {
    // build request query
    const queryName = `subjectDetail(subjectId: ${id})`
    const reqQuery = {
        query: buildGraphQLQuery(queryName, fields, false)
    }
    await postRequest(apiPath.graphqlEndpoint, reqQuery, successCallback, errorCallback);
};

export const getDetailSubjectService = async (
    id,
    params,
    successCallback,
    errorCallback
) => {
    await getRequest(
        `${apiPath.detailSubject}/${id}`,
        params,
        successCallback,
        errorCallback
    );
};
export const updateSubjectsService = async (
    subjectId,
    params,
    successCallback,
    errorCallback
) => {
    await putRequest(
        `${apiPath.updateSubject}/${subjectId}`,
        params,
        successCallback,
        errorCallback
    );
};
export const addSubjectsService = async (
    params,
    successCallback,
    errorCallback
) => {
    await postRequest(
        `${apiPath.addSubject}`,
        params,
        successCallback,
        errorCallback
    );
};
export const deleteSubjectsService = async (
    subjectId,
    params,
    successCallback,
    errorCallback
) => {
    await deleteRequest(
        `${apiPath.deleteSubject}/${subjectId}`,
        params,
        successCallback,
        errorCallback
    );
};

export const deleteChaptersService = async (
    code,
    params,
    successCallback,
    errorCallback
) => {
    await deleteRequest(
        `${apiPath.disableChapter}/${code}`,
        params,
        successCallback,
        errorCallback
    );
};
export const addChapterService = async (
    param,
    successCallback,
    errorCallback
) => {
    await postRequest(
        `${apiPath.addChapter}`,
        param,
        successCallback,
        errorCallback
    );
};
export const getChaptersService = async (
    code,
    param,
    successCallback,
    errorCallback
) => {
    await getRequest(
        `${apiPath.addChapters}/${code}/chapter/list`,
        param,
        successCallback,
        errorCallback
    );
};
