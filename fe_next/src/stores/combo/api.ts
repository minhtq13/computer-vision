import { baseQueryApi } from "@/libs/redux/base-query";
import {
  RComboChapter,
  RComboCourse,
  RComboExamClass,
  RComboRole,
  RComboStudent,
  RComboSubject,
  RComboTeacher,
  RComboViewableSubject,
  TCombo,
} from "./type";
import { API_PATH } from "@/constants/apiPath";

export const ComboApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getComboTeacher: build.query<TCombo[], RComboTeacher>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_TEACHER,
        method: "GET",
        params,
      }),
    }),
    getComboStudent: build.query<TCombo[], RComboStudent>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_STUDENT,
        method: "GET",
        params,
      }),
    }),
    getComboTest: build.query<TCombo[], { search?: string }>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_TEST,
        method: "GET",
        params,
      }),
    }),
    getComboSubject: build.query<TCombo[], RComboSubject>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_SUBJECT,
        method: "GET",
        params,
      }),
    }),
    getComboViewableSubject: build.query<TCombo[], RComboViewableSubject>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_VIEWABLE_SUBJECT,
        method: "GET",
        params,
      }),
    }),
    getComboChapter: build.query<TCombo[], RComboChapter>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_CHAPTER,
        method: "GET",
        params,
      }),
    }),
    getComboSemester: build.query<TCombo[], { search?: string }>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_SEMESTER,
        method: "GET",
        params,
      }),
    }),
    getComboRole: build.query<TCombo[], RComboRole>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_ROLE,
        method: "GET",
        params,
      }),
    }),
    getComboExamClass: build.query<TCombo[], RComboExamClass>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_EXAM_CLASS,
        method: "GET",
        params,
      }),
    }),
    getComboDepartment: build.query<TCombo[], { search?: string }>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_DEPARTMENT,
        method: "GET",
        params,
      }),
    }),
    getComboCourse: build.query<TCombo[], RComboCourse>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_COURSE,
        method: "GET",
        params,
      }),
    }),
    getComboTag: build.query<TCombo[], { filter?: string; objectAppliedType?: string }>({
      query: (params) => ({
        url: API_PATH.GET_COMBO_TAG,
        method: "GET",
        params,
      }),
    }),
  }),
});

export const {
  useGetComboTeacherQuery,
  useGetComboStudentQuery,
  useGetComboTestQuery,
  useGetComboSubjectQuery,
  useGetComboViewableSubjectQuery,
  useGetComboChapterQuery,
  useGetComboSemesterQuery,
  useGetComboRoleQuery,
  useGetComboExamClassQuery,
  useGetComboDepartmentQuery,
  useGetComboCourseQuery,
  useGetComboTagQuery,
} = ComboApi;
