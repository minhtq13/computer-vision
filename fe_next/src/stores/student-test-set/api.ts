import { baseQueryApi } from "@/libs/redux/base-query";
import { StudentTestSetResults } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const studentTestSetApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getResults: build.query<StudentTestSetResults, { examClassCode: string }>({
      query: (params) => ({
        url: API_PATH.GET_RESULTS(params.examClassCode),
        method: "GET",
      }),
    }),
    getOpeningStudentTestList: build.query<any, any>({
      query: (params) => ({
        url: API_PATH.GET_OPENING_STUDENT_TEST_LIST,
        method: "GET",
        params,
      }),
    }),
    getStudentTestSetDetails: build.query<any, { id: string }>({
      query: (params) => ({
        url: API_PATH.GET_STUDENT_TEST_SET_DETAILS(params.id),
        method: "GET",
      }),
    }),
    loadStudentTestSet: build.query<
      any,
      {
        studentTestSetId: string;
        testSetId: string;
      }
    >({
      query: (params) => ({
        url: API_PATH.LOAD_STUDENT_TEST_SET(params.studentTestSetId, params.testSetId),
        method: "GET",
      }),
    }),
    startAttemptTest: build.mutation<any, any>({
      query: (params) => ({
        url: API_PATH.START_ATTEMPT_TEST,
        method: "PUT",
        body: params,
      }),
    }),
    submitTest: build.mutation<any, any>({
      query: (params) => ({
        url: API_PATH.SUBMIT_TEST,
        method: "PUT",
        body: params,
      }),
    }),
    saveTempSubmission: build.mutation<any, any>({
      query: (params) => ({
        url: API_PATH.SAVE_TEMP_SUBMISSION,
        method: "PUT",
        body: params,
      }),
    }),
  }),
});

export const {
  useGetResultsQuery,
  useGetOpeningStudentTestListQuery,
  useGetStudentTestSetDetailsQuery,
  useLoadStudentTestSetQuery,
  useStartAttemptTestMutation,
  useSubmitTestMutation,
  useSaveTempSubmissionMutation,
} = studentTestSetApi;
