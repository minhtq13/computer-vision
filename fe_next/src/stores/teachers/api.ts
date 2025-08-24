import { baseQueryApi } from "@/libs/redux/base-query";
import { DeleteTeacherParams, GetTeachersParams, TTeacher } from "./type";
import { APIPageResponse } from "@/types/http";
import { API_PATH } from "@/constants/apiPath";

export const teachersApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getTeachers: build.query<APIPageResponse<TTeacher>, GetTeachersParams>({
      query: (params) => ({
        url: API_PATH.GET_TEACHERS,
        method: "GET",
        params,
      }),
    }),
    deleteTeacher: build.mutation({
      query: (params: DeleteTeacherParams) => ({
        url: API_PATH.DELETE_TEACHER(params.userId, params.userType),
        method: "DELETE",
      }),
    }),
    exportTeacher: build.mutation({
      query: (body: any) => ({
        url: API_PATH.EXPORT_TEACHER,
        method: "GET",
        // responseType: "blob",
        body,
      }),
    }),
  }),
});

export const { useGetTeachersQuery, useDeleteTeacherMutation, useExportTeacherMutation } = teachersApi;
