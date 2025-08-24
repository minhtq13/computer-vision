import { userStateReducer } from "@/stores/user";
import { combineReducers } from "redux";
import { baseQueryApi } from "./base-query";
import { appStateReducer } from "@/stores/app";
import { refreshStateReducer } from "@/stores/refresh";
import { studentsStateReducer } from "@/stores/students";
import { comboStateReducer } from "@/stores/combo";
import { teachersStateReducer } from "@/stores/teachers";
import { testsStateReducer } from "@/stores/tests";
import { subjectsStateReducer } from "@/stores/subjects";
import { examClassStateReducer } from "@/stores/exam-class";
import { questionsStateReducer } from "@/stores/questions";
import { studentTestSetStateReducer } from "@/stores/student-test-set";
import { importExportStateReducer } from "@/stores/import-export";
import { persistStateReducer } from "@/stores/persist";
import { notificationStateReducer } from "@/stores/notification";
import { permissionsStateReducer } from "@/stores/permissions";
import { rolesStateReducer } from "@/stores/roles";

const appReducer = combineReducers({
  user: userStateReducer,
  app: appStateReducer,
  refresh: refreshStateReducer,
  students: studentsStateReducer,
  teachers: teachersStateReducer,
  combo: comboStateReducer,
  subjects: subjectsStateReducer,
  tests: testsStateReducer,
  examClass: examClassStateReducer,
  questions: questionsStateReducer,
  studentTestSet: studentTestSetStateReducer,
  importExport: importExportStateReducer,
  persist: persistStateReducer,
  notification: notificationStateReducer,
  permissions: permissionsStateReducer,
  roles: rolesStateReducer,
  [baseQueryApi.reducerPath]: baseQueryApi.reducer,
});

export const rootReducer = (state: any, action: any) => appReducer(state, action);
