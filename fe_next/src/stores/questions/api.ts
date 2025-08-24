import { baseQueryApi } from "@/libs/redux/base-query";
import { RPaginationQuestions, RQuestions, RTag } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const questionsApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getQuestions: build.query<any, RQuestions>({
      query: (params) => ({
        url: API_PATH.GET_QUESTIONS,
        method: "GET",
        params,
      }),
    }),
    getPaginationQuestions: build.query<any, RPaginationQuestions>({
      query: (params) => ({
        url: API_PATH.GET_PAGINATION_QUESTIONS,
        method: "GET",
        params,
      }),
      providesTags: ["questions"],
    }),
    deleteQuestion: build.mutation<
      any,
      {
        questionsId: number;
      }
    >({
      query: ({ questionsId }) => ({
        url: API_PATH.DELETE_QUESTION(questionsId),
        method: "DELETE",
      }),
      invalidatesTags: ["questions"],
    }),
    addQuestion: build.mutation<any, any>({
      query: (params) => ({
        url: API_PATH.ADD_QUESTION,
        method: "POST",
        body: params,
      }),
    }),
    updateQuestion: build.mutation<any, any>({
      query: (params) => {
        const { questionsId, ...rest } = params;
        return {
          url: API_PATH.UPDATE_QUESTION(questionsId),
          method: "PUT",
          body: rest,
        };
      },
    }),
    getQuestionDetail: build.query<any, { questionId: number }>({
      query: ({ questionId }) => ({
        url: API_PATH.GET_QUESTION_DETAIL(questionId),
        method: "GET",
      }),
    }),
    addTag: build.mutation<any, RTag>({
      query: (params) => ({
        url: API_PATH.ADD_TAG,
        method: "POST",
        body: params,
      }),
    }),
  }),
});

export const {
  useGetQuestionsQuery,
  useGetPaginationQuestionsQuery,
  useDeleteQuestionMutation,
  useAddQuestionMutation,
  useUpdateQuestionMutation,
  useGetQuestionDetailQuery,
  useAddTagMutation,
} = questionsApi;
