import { baseQueryApi } from "@/libs/redux/base-query";
import { API_PATH } from "@/constants/apiPath";

// Tạo helper function để xử lý download
const handleFileDownload = (data: Blob, fileName: string) => {
  const url = window.URL.createObjectURL(data);
  const link = document.createElement("a");
  link.href = url;
  link.setAttribute("download", fileName);
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
};

export const importExportApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    exportList: build.query<Blob, { params: any; object: string }>({
      query: ({ params, object }) => ({
        url: API_PATH.EXPORT_LIST(object),
        method: "GET",
        responseHandler: async (response) => response.blob(),
        params,
      }),
      async onQueryStarted({ object }, { queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          handleFileDownload(data, `${object}-${Date.now()}.xlsx`);
        } catch (error) {
          console.error("Export failed:", error);
        }
      },
    }),
    exportTestList: build.mutation<Blob, { params: any }>({
      query: ({ params }) => ({
        url: API_PATH.EXPORT_TEST_LIST,
        method: "POST",
        body: params,
        responseHandler: (response) => response.blob(),
      }),
      async onQueryStarted({ params }, { queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          handleFileDownload(data, `Test-${params.nameFile}-${Date.now()}.docx`);
        } catch (error) {
          console.error("Export failed:", error);
        }
      },
    }),
    exportExamClass: build.query<
      Blob,
      {
        params: {
          semesterId: string;
          subjectId: string;
        };
      }
    >({
      query: ({ params }) => ({
        url: API_PATH.EXPORT_EXAM_CLASS,
        method: "GET",
        responseHandler: (response) => response.blob(),
        params,
      }),
      async onQueryStarted({ params }, { queryFulfilled }) {
        try {
          console.log("params", params);
          const { data } = await queryFulfilled;
          handleFileDownload(data, `examClass-${Date.now()}.xlsx`);
        } catch (error) {
          console.error("Export failed:", error);
        }
      },
    }),
    exportExamClassStudent: build.query<Blob, { classCode: string }>({
      query: ({ classCode }) => ({
        url: API_PATH.EXPORT_EXAM_CLASS_STUDENT(classCode),
        method: "GET",
        responseHandler: (response) => response.blob(),
      }),
      async onQueryStarted({ classCode }, { queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          handleFileDownload(data, `student-exam-class-${classCode}-${Date.now()}.xlsx`);
        } catch (error) {
          console.error("Export failed:", error);
        }
      },
    }),
    importStudent: build.mutation<
      Blob,
      {
        classId: string;
        file: FormData;
      }
    >({
      query: ({ classId, file }) => ({
        url: API_PATH.IMPORT_STUDENT(classId),
        method: "POST",
        body: file,
      }),
    }),
    importQuestion: build.mutation<Blob, { file: FormData }>({
      query: ({ file }) => ({
        url: API_PATH.IMPORT_QUESTION,
        method: "POST",
        body: file,
      }),
    }),
    importList: build.mutation<any, { file: any; object: string }>({
      query: ({ file, object }) => {
        console.log("file", file);
        return {
          url: API_PATH.IMPORT_LIST(object),
          method: "POST",
          body: file,
        };
      },
    }),
  }),
});

export const {
  useLazyExportListQuery,
  useImportListMutation,
  useImportQuestionMutation,
  useImportStudentMutation,
  useExportTestListMutation,
  useLazyExportExamClassQuery,
  useLazyExportExamClassStudentQuery,
} = importExportApi;
