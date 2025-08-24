import { baseQueryApi } from "@/libs/redux/base-query";
import { LoginRes, PLogin } from "./type";
import { IParamsListsQuery, IParamsListUser } from "@/types/http";
import { API_PATH } from "@/constants/apiPath";

export const userApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    login: build.mutation<LoginRes, PLogin>({
      query: (payload) => {
        return {
          url: API_PATH.LOGIN,
          method: "POST",
          body: payload,
        };
      },
    }),
    getUserInfo: build.query<any, { userId: string }>({
      query: (params) => {
        return {
          url: API_PATH.GET_USER_INFO(params.userId),
          method: "GET",
        };
      },
    }),
    postImage: build.mutation<any, any>({
      query: (payload) => {
        return {
          url: API_PATH.POST_IMAGE,
          method: "POST",
          body: payload,
        };
      },
    }),
    getProfile: build.query<any, any>({
      query: () => {
        return {
          url: API_PATH.GET_PROFILE,
          method: "GET",
        };
      },
      providesTags: ["profile"],
    }),
    updateProfile: build.mutation<any, any>({
      query: (payload) => {
        return {
          url: API_PATH.UPDATE_PROFILE,
          method: "PUT",
          body: payload,
        };
      },
      invalidatesTags: ["profile"],
    }),
    updateUser: build.mutation<any, { userId: string; payload: any }>({
      query: (payload) => {
        return {
          url: API_PATH.UPDATE_USER(payload.userId),
          method: "PUT",
          body: payload.payload,
        };
      },
    }),
    createUser: build.mutation<any, any>({
      query: (payload) => {
        return {
          url: API_PATH.CREATE_USER,
          method: "POST",
          body: payload,
        };
      },
    }),
    deleteUser: build.mutation<any, { userType: string; id: string }>({
      query: (payload) => {
        return {
          url: API_PATH.DELETE_USER(payload.id, payload.userType),
          method: "DELETE",
        };
      },
    }),
    getAllAdmin: build.query<any, IParamsListsQuery>({
      query: (params) => {
        return {
          url: API_PATH.GET_ALL_ADMIN,
          method: "GET",
          params,
        };
      },
    }),
    updatePassword: build.mutation<any, { userId: string; newPassword: string; changeType: string }>({
      query: (payload) => {
        return {
          url: API_PATH.UPDATE_PASSWORD,
          method: "PUT",
          body: payload,
        };
      },
    }),
    getAllUser: build.query<any, IParamsListUser>({
      query: (params) => {
        return {
          url: API_PATH.GET_ALL_USER,
          method: "GET",
          params,
        };
      },
      providesTags: ["allUsers"],
    }),
  }),
});

export const {
  useLoginMutation,
  useGetUserInfoQuery,
  usePostImageMutation,
  useGetProfileQuery,
  useUpdateProfileMutation,
  useUpdateUserMutation,
  useCreateUserMutation,
  useDeleteUserMutation,
  useGetAllAdminQuery,
  useUpdatePasswordMutation,
  useGetAllUserQuery,
} = userApi;
