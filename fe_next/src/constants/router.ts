import pathsToArray from "@/helpers/string";

export const PATH_ROUTER = {
  PUBLIC: {
    EMPTY: "",
    DEFAULT: "/",
    LOGIN: "/login",
    ACCESS_DENIED: "/access-denied",
  },
  PROTECTED: {
    CREATE_USER: "/create-user",
    STUDENTS: "/students",
    TEACHERS: "/teachers",
    SUBJECTS: "/subjects",
    ADD_SUBJECT: "/subjects/add",
    PROFILE_USER: "/profile-user",
    EXAM_CLASS: "/exam-class",
    TEST_SET_CREATE: "/test-set-create",
    QUESTIONS: "/questions",
    TESTS: "/tests",
    TESTS_CREATE: "/tests/create",
    TESTS_EDIT: "/tests/edit",
    AUTOMATIC_SCORING: "/scoring",
    ADMINS: "/admins",
    SYSTEM_CONFIG: "/system-config",
    ONLINE_TEST: "/online-test",
    EXAM_CLASS_CREATE: "/exam-class/create",
    ADD_QUESTIONS: "/questions/add",
    STUDENT_TEST_LIST: "/student-tests",
    PERMISSIONS: "/permissions",
  },
  DETAIL: {
    STUDENT_DETAIL: (id: string) => `/students/${id}`,
    TEACHER_DETAIL: (id: string) => `/teachers/${id}`,
    SUBJECT_DETAIL: (id: string) => `/subjects/${id}`,
    TEST_SET_DETAIL: (id: string) => `/test-set-create/${id}`,
    EXAM_CLASS_DETAIL: (id: string) => `/exam-class/${id}`,
    EXAM_CLASS_EDIT: (id: string) => `/exam-class/${id}/edit`,
    QUESTION_EDIT: (id: string) => `/questions/${id}`,
    TESTS_EDIT: (testId: string, testNo: string) => `/tests/${testId}/${testNo}/edit`,
    TESTS_PREVIEW: (testId: string, testNo: string) => `/tests/${testId}/${testNo}/preview`,
    ADMIN_EDIT: (id: string) => `/admins/${id}`,
    ONLINE_TEST_DETAIL: (id: string) => `/online-test/${id}`,
  },
};

export const PublicRouters = pathsToArray(PATH_ROUTER.PUBLIC);
export const ProtectedRouters = pathsToArray(PATH_ROUTER.PROTECTED);
