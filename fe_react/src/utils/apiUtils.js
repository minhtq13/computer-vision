import {HttpStatusCode} from "axios";
import {errorCodeMap, System, UNKNOWN_ERROR_MSG, User} from "../common/apiErrorCode";

export const processApiError = (error, defaultMessage) => {
    let response = error?.response;
    if (response?.status === HttpStatusCode.BadGateway) {
        return System.BAD_GATEWAY;
    } else if (response?.status === HttpStatusCode.InternalServerError) {
        return System.SERVER_ERROR.message;
    } else if (response?.status === HttpStatusCode.Forbidden) {
        return System.FORBIDDEN.message;
    } else if (response?.status === HttpStatusCode.Unauthorized) {
        if (response?.data?.code === User.USER_NAME_NOT_FOUND.code) {
            return User.USER_NAME_NOT_FOUND.message;
        } else if (response?.data?.code === System.BAD_CREDENTIALS.code) {
            return System.BAD_CREDENTIALS.message;
        }
        return System.UNAUTHORIZED.message;
    } else if (errorCodeMap.get(response?.data?.code) !== undefined) {
        return errorCodeMap.get(response?.data?.code)?.message;
    } else {
        return defaultMessage ?? UNKNOWN_ERROR_MSG;
    }
}

export const buildUrlParamQuery = (params) => {
    Object.keys(params).forEach(key => (params[key] === undefined || params[key] === null) && delete params[key]);
    return Object.entries(params).map(([key, value]) => `${key}=${encodeURIComponent(value)}`).join("&");
}

/**
 *
 * @returns
 * @param queryName tên query
 * @param contentQuery nội dung query dữ liệu
 * @param paging có phân trang hay không
 */
export const buildGraphQLQuery = (queryName, contentQuery, paging = false) => {
    return `query {
        ${queryName} {
            ${contentQuery} \n
            ${paging ? queryPageGraphQL : ""}
        }
    } `
}

export const queryPageGraphQL =
    "pageable { \n" +
    "        offset, pageNumber, pageSize, \n" +
    "        sort { \n" +
    "            empty, sorted \n" +
    "        }, paged, unpaged \n" +
    "}, \n" +
    "empty, first, last, number, numberOfElements, totalPages, totalElements \n";