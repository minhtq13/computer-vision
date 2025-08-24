import { baseQueryApi } from "@/libs/redux/base-query";
import { IParamsListsQuery } from "@/types/http";
import { API_PATH } from "@/constants/apiPath";

export const notificationApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getUserNotification: build.query({
      query: () => ({
        url: API_PATH.GET_USER_NOTIFICATION,
        method: "GET",
      }),
    }),
    getPageNotification: build.query<any, IParamsListsQuery>({
      query: (params: any) => ({
        url: API_PATH.GET_PAGE_NOTIFICATION,
        method: "GET",
        params,
      }),
    }),
    registerFCMToken: build.mutation({
      query: (body: any) => ({
        url: API_PATH.REGISTER_FCM_TOKEN,
        method: "PUT",
        body,
      }),
    }),
    updateNewNotification: build.mutation({
      query: (body: any) => ({
        url: API_PATH.UPDATE_NEW_NOTIFICATION,
        method: "PUT",
        body,
      }),
    }),
    getCountNewNotifications: build.query({
      query: (params: any) => ({
        url: API_PATH.GET_COUNT_NEW_NOTIFICATIONS,
        method: "GET",
        params,
      }),
    }),
  }),
});

export const {
  useGetUserNotificationQuery,
  useGetPageNotificationQuery,
  useRegisterFCMTokenMutation,
  useUpdateNewNotificationMutation,
  useGetCountNewNotificationsQuery,
} = notificationApi;
