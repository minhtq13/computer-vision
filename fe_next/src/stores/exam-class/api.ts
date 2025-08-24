import { baseQueryApi } from "@/libs/redux/base-query";
import { APIPageResponse } from "@/types/http";
import { RCreateExamClass, RExamClass, RSendEmailResultExamClass, RUpdateExamClass } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const ExamClassApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getExamClasses: build.query<APIPageResponse<any>, RExamClass>({
      query: (params) => ({
        url: API_PATH.GET_EXAM_CLASSES,
        method: "GET",
        params,
      }),
    }),
    getParticipants: build.query<any, { examClassId: string; roleType: string }>({
      query: (params) => ({
        url: API_PATH.GET_PARTICIPANTS(params.examClassId),
        method: "GET",
        params: {
          roleType: params.roleType,
        },
      }),
    }),
    getExamClassDetail: build.query<any, { examClassId: string }>({
      query: (params) => ({
        url: API_PATH.GET_EXAM_CLASS_DETAIL(params.examClassId),
        method: "GET",
      }),
    }),
    createExamClass: build.mutation<any, RCreateExamClass>({
      query: (params) => ({
        url: API_PATH.CREATE_EXAM_CLASS,
        method: "POST",
        body: params,
      }),
    }),
    updateExamClass: build.mutation<any, RUpdateExamClass>({
      query: (params) => {
        const { id, ...rest } = params;
        return {
          url: API_PATH.UPDATE_EXAM_CLASS(id),
          method: "PUT",
          body: rest,
        };
      },
    }),
    sendEmailResultExamClass: build.mutation<any, RSendEmailResultExamClass>({
      query: (params) => {
        const { examClassId, ...rest } = params;
        return {
          url: API_PATH.SEND_EMAIL_RESULT_EXAM_CLASS(examClassId),
          method: "POST",
          body: rest,
        };
      },
    }),
  }),
});

export const {
  useGetExamClassesQuery,
  useGetParticipantsQuery,
  useGetExamClassDetailQuery,
  useCreateExamClassMutation,
  useUpdateExamClassMutation,
  useSendEmailResultExamClassMutation,
} = ExamClassApi;
