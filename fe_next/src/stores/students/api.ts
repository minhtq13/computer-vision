import { baseQueryApi } from "@/libs/redux/base-query";
import { DeleteStudentParams, GetStudentsParams, PExportStudent } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const studentsApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getStudents: build.query({
      query: (params: GetStudentsParams) => ({
        url: API_PATH.GET_STUDENTS,
        method: "GET",
        params,
      }),
    }),
    deleteStudent: build.mutation({
      query: (params: DeleteStudentParams) => ({
        url: API_PATH.DELETE_STUDENT(params.userId, params.userType),
        method: "DELETE",
      }),
    }),
    exportStudent: build.mutation({
      query: (body: PExportStudent) => ({
        url: API_PATH.EXPORT_STUDENT,
        method: "GET",
        // responseType: "blob",
        body,
      }),
    }),
  }),
});

export const { useGetStudentsQuery, useDeleteStudentMutation, useExportStudentMutation } = studentsApi;
