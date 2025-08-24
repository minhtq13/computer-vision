import axios from "axios";
import {toast} from "react-toastify";
// import { DEFAULT_TIME_AUTO_HIDE_TOASTIFY } from "../constants/appConstants";
import {clearInfoLocalStorage, getToken} from "../utils/storage";
import {appPath, rewriteWithRoutePrefix} from "../config/appPath";

// config default axios
axios.defaults.baseURL = process.env.REACT_APP_BACKEND_HOST;

const notificationWarning = (message, position = "top-right", autoClose) => {
    toast.warning(message, {
        position: position,
        autoClose: autoClose || 3000,
        hideProgressBar: true,
    });
};

const proxy = {
    protocol: "http" || "https"
}

axios.interceptors.request.use((config) => {
    const token = getToken();
    // const tokenType = getTokenType();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    // config.headers["Access-Control-Allow-Origin"] = "*";
    // config.headers["Access-Control-Allow-Methods"] = "*";
    // config.headers["Access-Control-Allow-Headers"] = "*";
    // config.headers["Access-Control-Max-Age"] = 1728000;
    // config.headers["Content-Type"] = "application/json";
    return config;
});

axios.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        return new Promise((resolve, reject) => {
            // const originalReq = error.config;
            // if (
            //   error.response.status === 401 &&
            //   error.config &&
            //   !error.config.__isRetryRequest
            // ) {
            //   originalReq._retry = true;

            //   requestNewToken();
            // }
            return reject(error);
        });
    }
);
export const getRequest = (url = "", params, successCallback, errorCallback, timeout) => {
    return axios
        .get(url, {
            params: params,
            timeout: timeout,
            proxy: proxy
        })
        .then((response) => {
            if (successCallback) {
                try {
                    successCallback(response);
                } catch (error) {
                    console.log(error);
                }
            }
        })
        .catch((error) => {
            if (error) {
                if (
                    error?.response?.data?.message === "Unauthorized"
                ) {
                    notificationWarning("Token hết hạn, vui lòng đăng nhập lại!");
                    setTimeout(() => logOut(), 3000);
                }
            }
            if (errorCallback)
                try {
                    errorCallback(error);
                } finally {
                    console.log(error);
                }
        });
};
export const postRequest = async (url = "", params, successCallback, errorCallback, timeout) => {
    return await axios
        .post(url, params, {
            timeout: timeout,
            proxy: proxy
        })
        .then((response) => {
            if (successCallback) {
                try {
                    successCallback(response);
                } catch (error) {
                    console.log("error", error);
                }
            }
        })
        .catch((error) => {
            if (error) {
                if (
                    error?.response?.data?.message === "Unauthorized"
                ) {
                    notificationWarning("Token hết hạn, vui lòng đăng nhập lại!");
                    setTimeout(() => logOut(), 3000);
                }
            }
            if (errorCallback)
                try {
                    errorCallback(error);
                } finally {
                    console.log(error);
                }
        });
};

// post request with configuration
export const postConfiguredRequest = async (url = "", body, config = {}, successCallback, errorCallback, timeout) => {
    return await axios
        .post(url, body, {
            ...config,
            timeout: timeout,
            proxy: proxy
        })
        .then((response) => {
            if (successCallback) {
                try {
                    successCallback(response);
                } catch (error) {
                    console.log("error", error);
                }
            }
        })
        .catch((error) => {
            if (error) {
                if (
                    error?.response?.data?.message === "Unauthorized"
                ) {
                    notificationWarning("Token hết hạn, vui lòng đăng nhập lại!");
                    setTimeout(() => logOut(), 3000);
                }
            }
            if (errorCallback)
                try {
                    errorCallback(error);
                } finally {
                    console.log(error);
                }
        });
};

export const putRequest = (
    url = "",
    params = {},
    successCallback,
    errorCallback,
    headers = {},
    timeout
) => {
    return axios
        .put(url, params, {
            headers: headers,
            timeout: timeout,
            proxy: proxy
        })
        .then((response) => {
            if (successCallback) {
                try {
                    successCallback(response);
                } catch (error) {
                    console.log(error);
                }
            }
        })
        .catch((error) => {
            if (error) {
                if (
                    error?.response?.data?.message === "Unauthorized"
                ) {
                    notificationWarning("Token hết hạn, vui lòng đăng nhập lại!");
                    setTimeout(() => logOut(), 3000);
                }
            }
            if (errorCallback)
                try {
                    errorCallback(error);
                } finally {
                    console.log(error);
                }
        });
};

export const deleteRequest = (url = "", params = {}, successCallback, errorCallback) => {
    return axios
        .delete(url, {
            params: params,
            proxy: proxy
        })
        .then((response) => {
            if (successCallback) {
                try {
                    successCallback(response);
                } catch (error) {
                    console.log(error);
                }
            }
        })
        .catch((error) => {
            if (error) {
                if (
                    error?.response?.data?.message === "Unauthorized"
                ) {
                    notificationWarning("Token hết hạn, vui lòng đăng nhập lại!");
                    setTimeout(() => logOut(), 3000);
                }
            }
            if (errorCallback)
                try {
                    errorCallback(error);
                } finally {
                    console.log(error);
                }
        });
};

// const requestNewToken = () => {
//   const refreshToken = getRefreshToken();

//   if (!refreshToken) {
//     window.location.href(appPath.login);
//   }

//   const res = fetch(`api?refresh-token=${refreshToken}`, {
//     method: "GET",
//     mode: "cors",
//     cache: "no-cache",
//     credentials: "same-origin",
//     headers: {
//       "Content-Type": "application/json",
//       Accept: "*/*",
//       "Accept-Encoding": "gzip, deflate, br",
//       Connection: "keep-alive",
//     },
//     redirect: "follow",
//     referrer: "no-referrer",
//   })
//     .then((res1) => res1.json())
//     .then((res2) => {
//       if (res2.access_token) {
//         try {
//           setToken(res2.access_token);
//           setRefeshToken(res2.refresh_token);
//         } catch (error) {
//           window.location.href(appPath.login);
//         }
//       } else {
//         window.location.href(appPath.login);
//       }
//     });

//   return res;
// };

export const logOut = () => {
    clearInfoLocalStorage();
    window.location.replace(rewriteWithRoutePrefix(appPath.login));
};
