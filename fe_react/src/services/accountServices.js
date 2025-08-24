import axios from "axios";
import {getRequest, logOut, postRequest, putRequest} from "../api/apiCaller";
import {apiPath} from "../config/apiPath";
import {saveInfoToLocalStorage} from "../utils/storage";

export const loginAuthenticService = async (
    parameters,
    successCallback,
    errorCallback
) => {
    await postRequest(
        `${apiPath.login}`,
        parameters,
        successCallback,
        errorCallback
    );
};

export const getProfileUserService = async (
    parameters,
    successCallback,
    errorCallback
) => {
    await getRequest(
        `${apiPath.profile}`,
        parameters,
        successCallback,
        errorCallback
    );
};

export const getUserNotificationService = async (
    params,
    successCallback,
    errorCallback
) => {
    await getRequest(
        `${apiPath.getNotifications}`,
        params,
        successCallback,
        errorCallback
    );
};

export const refreshTokenService = async (refreshToken) => {
    const headers = {
        refreshToken: refreshToken,
    };
    const requestData = {
        //...
    };
    axios
        .post(apiPath.refreshToken, requestData, {headers})
        .then((response) => {
            const {accessToken, refreshToken} = response.data;
            saveInfoToLocalStorage(accessToken, refreshToken);
        })
        .catch((error) => {
            console.error("Error:", error);
            logOut();
        });
};

export const registerFCMTokenService = async (
    body,
    successCallback,
    errorCallback) => {
    await putRequest(apiPath.registerFCMToken, body, successCallback, errorCallback);
}

export const updateNewStatusNotificationService = async (
    body,
    successCallback,
    errorCallback) => {
    await putRequest(apiPath.updateNewNotification, body, successCallback, errorCallback);
}

// count new notifications
export const countNewNotificationsService = async (
    successCallback,
    errorCallback) => {
    await getRequest(apiPath.countNewNotifications, {}, successCallback, errorCallback);
}
