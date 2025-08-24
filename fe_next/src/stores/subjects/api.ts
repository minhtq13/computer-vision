import { baseQueryApi } from "@/libs/redux/base-query";
import { APIPageResponse } from "@/types/http";
import { RAddChapter, RAddSubject, RSubjects, RUpdateChapter, RUpdateSubject, TSubjectDetail, TSubjects } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const subjectsApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getSubjects: build.query<APIPageResponse<TSubjects>, RSubjects>({
      query: (params) => ({
        url: API_PATH.GET_SUBJECTS,
        method: "GET",
        params,
      }),
    }),
    getSubjectDetail: build.query<TSubjectDetail, { id: string }>({
      query: (params) => ({
        url: API_PATH.GET_SUBJECT_DETAIL(params.id),
        method: "GET",
      }),
    }),
    addSubject: build.mutation<any, RAddSubject>({
      query: (payload) => ({
        url: API_PATH.ADD_SUBJECT,
        method: "POST",
        body: payload,
      }),
    }),
    deleteChapter: build.mutation<any, any>({
      query: (payload) => ({
        url: API_PATH.DELETE_CHAPTER(payload.id),
        method: "POST",
      }),
    }),
    updateSubject: build.mutation<any, RUpdateSubject>({
      query: ({ subjectId, ...payload }) => ({
        url: API_PATH.UPDATE_SUBJECT(subjectId),
        method: "PUT",
        body: payload,
      }),
    }),
    updateChapter: build.mutation<any, RUpdateChapter>({
      query: ({ chapterId, ...payload }) => ({
        url: API_PATH.UPDATE_CHAPTER(chapterId),
        method: "PUT",
        body: payload,
      }),
    }),
    addChapter: build.mutation<any, RAddChapter>({
      query: (payload) => ({
        url: API_PATH.ADD_CHAPTER,
        method: "POST",
        body: payload,
      }),
    }),
  }),
});

export const {
  useGetSubjectsQuery,
  useGetSubjectDetailQuery,
  useAddSubjectMutation,
  useDeleteChapterMutation,
  useUpdateSubjectMutation,
  useUpdateChapterMutation,
  useAddChapterMutation,
} = subjectsApi;
