import { baseQueryApi } from "@/libs/redux/base-query";
import { ITestSet, RManualTestSet, RScoring, RScoringResult, RTestSetDetail, RUpdateTestSet } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const testsApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getTestSetDetail: build.mutation<any, RTestSetDetail>({
      query: (payload) => ({
        url: API_PATH.GET_TEST_SET_DETAIL,
        method: "POST",
        body: payload,
      }),
    }),
    getListTestSet: build.query<ITestSet[], { testId: string }>({
      query: (payload) => ({
        url: API_PATH.GET_LIST_TEST_SET(payload.testId),
        method: "GET",
      }),
    }),
    deleteTestSet: build.mutation<any, { testSetId: string }>({
      query: (payload) => ({
        url: API_PATH.DELETE_TEST_SET(payload.testSetId),
        method: "DELETE",
      }),
    }),
    generateTestSet: build.mutation<any, { testId: string; numOfTestSet: number }>({
      query: (payload) => ({
        url: API_PATH.GENERATE_TEST_SET,
        method: "POST",
        body: payload,
      }),
    }),
    createManualTestSet: build.mutation<any, RManualTestSet>({
      query: (payload) => ({
        url: API_PATH.CREATE_MANUAL_TEST_SET,
        method: "POST",
        body: payload,
      }),
    }),
    updateTestSet: build.mutation<any, RUpdateTestSet>({
      query: (payload) => ({
        url: API_PATH.UPDATE_TEST_SET,
        method: "PUT",
        body: payload,
      }),
    }),
    scoring: build.mutation<any, RScoring>({
      query: (payload) => ({
        url: API_PATH.SCORING(payload.examClassCode),
        method: "POST",
        body: payload,
      }),
    }),
    handleScoringResult: build.mutation<any, RScoringResult>({
      query: (payload) => {
        const { examClassCode, tempFileCode, option } = payload;
        return {
          url: API_PATH.HANDLE_SCORING_RESULT(examClassCode, tempFileCode, option),
          method: "POST",
        };
      },
    }),
    getImagesInFolder: build.query<any, { examClassCode: string }>({
      query: (payload) => ({
        url: API_PATH.GET_IMAGES_IN_FOLDER(payload.examClassCode),
        method: "GET",
      }),
      providesTags: ["image"],
    }),
    deleteImageInFolder: build.mutation<any, { examClassCode: string; lstFileName: string[] }>({
      query: (payload) => ({
        url: API_PATH.DELETE_IMAGE_IN_FOLDER,
        method: "POST",
        body: payload,
      }),
      invalidatesTags: ["image"],
    }),
    loadLatestTempScoredData: build.query<any, { examClassCode: string; studentCodes: string[] }>({
      query: (payload) => ({
        url: API_PATH.LOAD_LATEST_TEMP_SCORED_DATA(payload.examClassCode, payload.studentCodes),
        method: "GET",
      }),
    }),
    uploadImage: build.mutation<any, { formData: FormData; examClassCode: string }>({
      query: (payload) => ({
        url: API_PATH.UPLOAD_IMAGE(payload.examClassCode),
        method: "POST",
        body: payload.formData,
      }),
      invalidatesTags: ["image"],
    }),
  }),
});

export const {
  useGetTestSetDetailMutation,
  useGetListTestSetQuery,
  useDeleteTestSetMutation,
  useGenerateTestSetMutation,
  useCreateManualTestSetMutation,
  useUpdateTestSetMutation,
  useScoringMutation,
  useHandleScoringResultMutation,
  useGetImagesInFolderQuery,
  useDeleteImageInFolderMutation,
  useLoadLatestTempScoredDataQuery,
  useUploadImageMutation,
} = testsApi;
