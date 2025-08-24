import { RoleType } from "@/stores/roles/type";

export const API_PATH_BACKEND_SPRING = "/e-learning/sp/api"; // for using reverse proxy by nginx

export const API_PATH_BACKEND_GO = "/e-learning/go/api"; // for using reverse proxy by nginx

export const DEFAULT_PATH_BACKEND_SPRING = "/e-learning/sp";

export const DEFAULT_PATH_BACKEND_GO = "/e-learning/go";

// check BASE_URL in different envs
export const BASE_URL_SPRING = process.env.NEXT_PUBLIC_BACKEND_SPRING_HOST;

export const BASE_RESOURCE_URL_SPRING = process.env.NEXT_PUBLIC_BE_SPRING_RESOURCES_HOST;

export const BASE_URL_GO = process.env.NEXT_PUBLIC_BACKEND_GO_HOST;

export const BASE_RESOURCE_URL_GO = process.env.NEXT_PUBLIC_BE_GO_RESOURCES_HOST;

export const API_PATH = {
  // static files
  QUESTION_IMPORT_TEMPLATE: BASE_RESOURCE_URL_SPRING + "/resources/excelTemplate/Questions.xlsx",
  STUDENT_IMPORT_TEMPLATE: BASE_RESOURCE_URL_SPRING + "/resources/excelTemplate/Dump_UserStudent.xlsx",
  TEACHER_IMPORT_TEMPLATE: BASE_RESOURCE_URL_SPRING + "/resources/excelTemplate/Dump_UserTeacher.xlsx",
  ANSWER_SHEET_TEMPLATE: BASE_RESOURCE_URL_SPRING + "/resources/wordTemplate/AnswerSheetTemplate.pdf", // wait for get template

  // GraphQL
  GRAPHQL_ENDPOINT: BASE_URL_SPRING + "/graphql",

  // Student APIs
  GET_STUDENTS: BASE_URL_SPRING + "/user/student/page",
  DELETE_STUDENT: (userId: string, userType: string) => `${BASE_URL_SPRING}/user/hard/${userId}?userType=${userType}`,
  EXPORT_STUDENT: BASE_URL_SPRING + "/user/student/export",

  // Auth APIs
  LOGIN: BASE_URL_GO + "/auth/login",
  REFRESH_TOKEN: BASE_URL_GO + "/auth/token/refresh",
  LOGOUT: BASE_URL_GO + "/auth/logout",
  // User APIs
  GET_USER_INFO: (userId: string) => `${BASE_URL_SPRING}/user/${userId}`,
  POST_IMAGE: BASE_URL_SPRING + "/file-attach/upload-img",
  GET_PROFILE: BASE_URL_GO + "/user/profile",
  UPDATE_PROFILE: BASE_URL_SPRING + "/user/profile",
  UPDATE_USER: (userId: string) => `${BASE_URL_SPRING}/user/${userId}`,
  CREATE_USER: BASE_URL_SPRING + "/user",
  DELETE_USER: (id: string, userType: string) => `${BASE_URL_SPRING}/user/${id}?userType=${userType}`,
  GET_ALL_ADMIN: BASE_URL_SPRING + "/user/admin/page",
  UPDATE_PASSWORD: BASE_URL_SPRING + "/user/password/update",

  // Combo APIs
  GET_COMBO_TEACHER: BASE_URL_SPRING + "/combobox/user/teacher",
  GET_COMBO_STUDENT: BASE_URL_SPRING + "/combobox/user/student",
  GET_COMBO_TEST: BASE_URL_SPRING + "/combobox/test",
  GET_COMBO_SUBJECT: BASE_URL_SPRING + "/combobox/subject",
  GET_COMBO_VIEWABLE_SUBJECT: BASE_URL_SPRING + "/combobox/subject/viewable",
  GET_COMBO_CHAPTER: BASE_URL_SPRING + "/combobox/subject/chapter",
  GET_COMBO_SEMESTER: BASE_URL_SPRING + "/combobox/semester",
  GET_COMBO_ROLE: BASE_URL_SPRING + "/combobox/role",
  GET_COMBO_EXAM_CLASS: BASE_URL_SPRING + "/combobox/exam-class",
  GET_COMBO_DEPARTMENT: BASE_URL_SPRING + "/combobox/department",
  GET_COMBO_COURSE: BASE_URL_SPRING + "/combobox/course",
  GET_COMBO_TAG: BASE_URL_SPRING + "/combobox/tag",

  // Test APIs
  GET_TESTS: BASE_URL_SPRING + "/test",
  GET_TEST_DETAIL: (testId: string) => `${BASE_URL_SPRING}/test/details/${testId}`,
  DELETE_TEST: (testId: string) => `${BASE_URL_SPRING}/test/${testId}`,
  CREATE_AUTO_TEST: BASE_URL_SPRING + "/test/create/random",
  CREATE_MANUAL_TEST: BASE_URL_SPRING + "/test/create",
  UPDATE_TEST: (testId: string) => `${BASE_URL_SPRING}/test/${testId}`,
  GET_LIST_QUESTION_ALLOWED_IN_TEST: (testId: string) => `${BASE_URL_SPRING}/test/questions/allowed/page/${testId}`,

  // Subject APIs
  GET_SUBJECTS: BASE_URL_SPRING + "/subject/list",
  GET_SUBJECT_DETAIL: (id: string) => `${BASE_URL_SPRING}/subject/detail/${id}`,
  ADD_SUBJECT: BASE_URL_SPRING + "/subject",
  DELETE_CHAPTER: (id: string) => `${BASE_URL_SPRING}/chapter/disable/${id}`,
  UPDATE_SUBJECT: (subjectId: string) => `${BASE_URL_SPRING}/subject/${subjectId}`,
  UPDATE_CHAPTER: (chapterId: string) => `${BASE_URL_SPRING}/chapter/${chapterId}`,
  ADD_CHAPTER: BASE_URL_SPRING + "/chapter",

  // Teacher APIs
  GET_TEACHERS: BASE_URL_SPRING + "/user/teacher/page",
  DELETE_TEACHER: (userId: string, userType: string) => `${BASE_URL_SPRING}/user/hard/${userId}?userType=${userType}`,
  EXPORT_TEACHER: BASE_URL_SPRING + "/user/teacher/export",

  // Question APIs
  GET_QUESTIONS: BASE_URL_SPRING + "/question",
  GET_PAGINATION_QUESTIONS: BASE_URL_SPRING + "/question/page",
  DELETE_QUESTION: (questionsId: number) => `${BASE_URL_SPRING}/question/${questionsId}`,
  ADD_QUESTION: BASE_URL_SPRING + "/question",
  UPDATE_QUESTION: (questionsId: number) => `${BASE_URL_SPRING}/question/${questionsId}`,
  GET_QUESTION_DETAIL: (questionId: number) => `${BASE_URL_SPRING}/question/detail/${questionId}`,
  ADD_TAG: BASE_URL_SPRING + "/tag",

  // Exam Class APIs
  GET_EXAM_CLASSES: BASE_URL_SPRING + "/exam-class/page",
  GET_PARTICIPANTS: (examClassId: string) => `${BASE_URL_SPRING}/exam-class/participant/list/${examClassId}`,
  GET_EXAM_CLASS_DETAIL: (examClassId: string) => `${BASE_URL_SPRING}/exam-class/detail/${examClassId}`,
  CREATE_EXAM_CLASS: BASE_URL_SPRING + "/exam-class",
  UPDATE_EXAM_CLASS: (id: number) => `${BASE_URL_SPRING}/exam-class/${id}`,
  SEND_EMAIL_RESULT_EXAM_CLASS: (examClassId: string) => `${BASE_URL_SPRING}/exam-class/result/send-email/${examClassId}`,

  // Import Export APIs
  EXPORT_LIST: (object: string) => `${BASE_URL_SPRING}/user/${object}/export`,
  EXPORT_TEST_LIST: BASE_URL_SPRING + "/test-set/export",
  EXPORT_EXAM_CLASS: BASE_URL_SPRING + "/exam-class/export",
  EXPORT_EXAM_CLASS_STUDENT: (classCode: string) => `${BASE_URL_SPRING}/std-test-set/result/export/${classCode}`,
  IMPORT_STUDENT: (classId: string) => `${BASE_URL_SPRING}/exam-class/participant/student/import/${classId}`,
  IMPORT_QUESTION: BASE_URL_SPRING + "/question/import",
  IMPORT_LIST: (object: string) => `${BASE_URL_SPRING}/user/${object}/import`,

  // Notification APIs
  GET_USER_NOTIFICATION: BASE_URL_SPRING + "/notifications",
  GET_PAGE_NOTIFICATION: BASE_URL_SPRING + "/notifications/page",
  REGISTER_FCM_TOKEN: BASE_URL_SPRING + "/notifications/fcm/register-token",
  UPDATE_NEW_NOTIFICATION: BASE_URL_SPRING + "/notifications/new-status/update",
  GET_COUNT_NEW_NOTIFICATIONS: BASE_URL_SPRING + "/notifications/new-status/count",

  // Student Test Set APIs
  GET_RESULTS: (examClassCode: string) => `${BASE_URL_SPRING}/std-test-set/result/${examClassCode}`,
  GET_OPENING_STUDENT_TEST_LIST: BASE_URL_SPRING + "/std-test-set/online-test",
  GET_STUDENT_TEST_SET_DETAILS: (id: string) => `${BASE_URL_SPRING}/std-test-set/details/${id}`,
  LOAD_STUDENT_TEST_SET: (studentTestSetId: string, testSetId: string) =>
    `${BASE_URL_SPRING}/std-test-set/details/test-set/${studentTestSetId}/${testSetId}`,
  START_ATTEMPT_TEST: BASE_URL_SPRING + "/std-test-set/start-attempt",
  SUBMIT_TEST: BASE_URL_SPRING + "/std-test-set/submit",
  SAVE_TEMP_SUBMISSION: BASE_URL_SPRING + "/std-test-set/temp-submission",

  // Test Set APIs
  GET_TEST_SET_DETAIL: BASE_URL_SPRING + "/test-set/detail",
  GET_LIST_TEST_SET: (testId: string) => `${BASE_URL_SPRING}/test-set/list?testId=${testId}`,
  DELETE_TEST_SET: (testSetId: string) => `${BASE_URL_SPRING}/test-set/${testSetId}`,
  GENERATE_TEST_SET: BASE_URL_SPRING + "/test-set/generate",
  CREATE_MANUAL_TEST_SET: BASE_URL_SPRING + "/test-set/create",
  UPDATE_TEST_SET: BASE_URL_SPRING + "/test-set",
  SCORING: (examClassCode: string) => `${BASE_URL_SPRING}/test-set/scoring/exam-class/${examClassCode}`,
  HANDLE_SCORING_RESULT: (examClassCode: string, tempFileCode: string, option: string) =>
    `${BASE_URL_SPRING}/test-set/scoring/result/save/${examClassCode}?tempFileCode=${tempFileCode}&option=${option}`,
  GET_IMAGES_IN_FOLDER: (examClassCode: string) => `${BASE_URL_SPRING}/test-set/handled-answers/uploaded/${examClassCode}`,
  DELETE_IMAGE_IN_FOLDER: BASE_URL_SPRING + "/test-set/handled-answers/delete",
  LOAD_LATEST_TEMP_SCORED_DATA: (examClassCode: string, studentCodes: string[]) =>
    `${BASE_URL_SPRING}/test-set/scored-data/exam-class/${examClassCode}?studentCodes=${studentCodes.join(",")}`,
  UPLOAD_IMAGE: (examClassCode: string) => `${BASE_URL_SPRING}/test-set/handled-answers/upload/${examClassCode}`,

  // Role APIs
  GET_ROLES: (roleType: RoleType) => `${BASE_URL_SPRING}/roles?type=${roleType}`,

  GET_ROLE_DETAIL: (id: number) => `${BASE_URL_SPRING}/roles/details/${id}`,
  DELETE_ROLE: (id: number) => `${BASE_URL_SPRING}/roles/${id}`,
  CREATE_ROLE: BASE_URL_SPRING + "/roles",
  UPDATE_ROLE: (id: number) => `${BASE_URL_SPRING}/roles/${id}`,
  GET_BASE_ROLES: BASE_URL_SPRING + "/roles/bases",
  ASSIGN_CUSTOM_ROLE: BASE_URL_SPRING + "/roles/assign-custom-role",
  ASSIGN_BASE_ROLE: BASE_URL_SPRING + "/roles/assign-base-role",

  // Permission APIs
  GET_PERMISSIONS: BASE_URL_SPRING + "/permissions",
  GET_PERMISSION_DETAIL: (permissionId: number) => `${BASE_URL_SPRING}/permissions/${permissionId}`,
  DELETE_PERMISSION: (permissionId: number) => `${BASE_URL_SPRING}/permissions/${permissionId}`,
  CREATE_PERMISSION: BASE_URL_SPRING + "/permissions",
  UPDATE_PERMISSION: (permissionId: number) => `${BASE_URL_SPRING}/permissions/${permissionId}`,

  // User APIs
  GET_ALL_USER: BASE_URL_SPRING + "/user/page",
};
