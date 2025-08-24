import { baseQueryApi } from "@/libs/redux/base-query";
import { APIPageResponse } from "@/types/http";
import { RCreateAutoTest, RCreateManualTest, RGetListQuestionAllowedInTest, RTests } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const testsApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getTests: build.query<APIPageResponse<any>, RTests>({
      query: (params) => ({
        url: API_PATH.GET_TESTS,
        method: "GET",
        params,
      }),
    }),
    getTestDetail: build.query<any, { testId: string }>({
      query: (params) => ({
        url: API_PATH.GET_TEST_DETAIL(params.testId),
        method: "GET",
      }),
    }),
    deleteTest: build.mutation<any, { testId: string }>({
      query: (params) => ({
        url: API_PATH.DELETE_TEST(params.testId),
        method: "DELETE",
      }),
    }),
    createAutoTest: build.mutation<any, RCreateAutoTest>({
      query: (params) => ({
        url: API_PATH.CREATE_AUTO_TEST,
        method: "POST",
        body: params,
      }),
    }),
    createManualTest: build.mutation<any, RCreateManualTest>({
      query: (params) => ({
        url: API_PATH.CREATE_MANUAL_TEST,
        method: "POST",
        body: params,
      }),
    }),
    updateTest: build.mutation<any, any>({
      query: (payload) => ({
        url: API_PATH.UPDATE_TEST(payload.testId),
        method: "PUT",
        body: payload,
      }),
    }),
    getListQuestionAllowedInTest: build.query<any, RGetListQuestionAllowedInTest>({
      query: (params) => {
        const { testId, ...rest } = params;
        return {
          url: API_PATH.GET_LIST_QUESTION_ALLOWED_IN_TEST(String(testId)),
          method: "GET",
          params: rest,
        };
      },
    }),
  }),
});

export const {
  useGetTestsQuery,
  useDeleteTestMutation,
  useCreateAutoTestMutation,
  useCreateManualTestMutation,
  useGetTestDetailQuery,
  useUpdateTestMutation,
  useGetListQuestionAllowedInTestQuery,
} = testsApi;
