import { getCookie, setCookie } from "@/app/actions/cookie";
import { BaseQueryFn, createApi, FetchArgs, fetchBaseQuery, FetchBaseQueryError } from "@reduxjs/toolkit/query/react";
import { Mutex } from "async-mutex";
import Cookies from "js-cookie";
import { API_PATH, BASE_URL_SPRING } from "@/constants/apiPath";

const mutex = new Mutex();
const publicEndpoint = ["refresh", "login"];

// Utility function to handle logout
const handleLogout = () => {
  // Clear all tokens from both server-side and client-side
  setCookie("access_token", "");
  setCookie("refresh_token", "");
  Cookies.remove("access_token");
  Cookies.remove("refresh_token");

  // Redirect to login page
  if (typeof window !== "undefined") {
    window.location.href = "/login";
  }
};

const baseQuery = fetchBaseQuery({
  baseUrl: "",
  prepareHeaders: async (headers, { endpoint }) => {
    // headers.set("x-client-ip", "1.1.1.1");
    // headers.set("timezone", (dayjs().utcOffset() / 60).toString());
    // const accessToken = (await getCookie("access_token"))?.value;
    const accessToken = Cookies.get("access_token");
    if (!publicEndpoint.includes(endpoint)) {
      headers.set("Authorization", `Bearer ${accessToken}`);
    }
    return headers;
  },
});

const baseQueryWithReauth: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (args, api, extraOptions) => {
  // wait until the mutex is available without locking it
  await mutex.waitForUnlock();

  let result = await baseQuery(args, api, extraOptions);

  // Check if server is down or network error
  const isNetworkError = result.error && (result.error.status === "FETCH_ERROR" || result.error.status === "TIMEOUT_ERROR" || !result.error.status); // No status means network/server error

  // If request login, not check authorize
  const isRequestLogin = result.meta?.request?.url?.includes("/auth/login");
  const isAuthorizeError = result.error && result.error.status === 401 && !isRequestLogin;

  // Handle server down/network error - auto logout
  if (isNetworkError && !isRequestLogin) {
    console.log("Server is down or network error, logging out user");
    handleLogout();
    return result;
  }

  if (isAuthorizeError) {
    // checking whether the mutex is locked
    if (!mutex.isLocked()) {
      const release = await mutex.acquire();
      try {
        //TODO:: refresh token
        const refreshToken = (await getCookie("refresh_token"))?.value;
        try {
          const refreshResult: any = await baseQuery(
            {
              // API refresh token
              url: API_PATH.REFRESH_TOKEN,
              method: "POST",
              body: {
                refreshToken,
              },
            },
            api,
            extraOptions
          );
          setCookie("access_token", refreshResult.accessToken);
          setCookie("refresh_token", refreshResult.refreshToken);
          // retry the initial query
          result = await baseQuery(args, api, extraOptions);
        } catch (error) {
          console.log("refresh token error", error);
          // REMOVE TOKEN => LOGOUT
          handleLogout();
        }
      } finally {
        // release must be called once the mutex should be released again.
        release();
      }
    } else {
      // wait until the mutex is available without locking it
      await mutex.waitForUnlock();
      // Retry request với token mới (đã được refresh bởi request khác)
      result = await baseQuery(args, api, extraOptions);
    }
  }

  return result;
};

export const baseQueryApi = createApi({
  baseQuery: baseQueryWithReauth,
  tagTypes: ["image", "profile", "questions", "permissions", "roles", "allUsers"],
  endpoints: () => ({}),
  keepUnusedDataFor: 0,
});
