export enum HttpErrorCode {
  // BE ERROR CODE
  UNKNOWN_ERROR_MSG = "error.unknown",
  // USER
  USER_OLD_PASSWORD_NOT_MATCH = "user.error.old.password.not.match",
  USER_NAME_NOT_FOUND = "error.auth.username.not.found",
  EXISTED_USER_NAME = "user.error.username.existed",
  // COMMON
  PERMISSION_DENIED = "user.permission.denied",
  // TEST
  TEST_NOT_FOUND = "error.test.not.found",
  TEST_NOT_FOUND_BY_TYPE = "error.test.not.found.by.type",
  TEST_USED_IN_EXAM_CLASSES = "error.test.used.in.exam.classes",
  TEST_ASSIGNED_OR_HANDLED_BY_STUDENTS = "error.test.assigned.handled.by.students",
  // EXAM CLASS
  EXAM_CLASS_NOT_FOUND_BY_TYPE = "error.exam.class.not.found.by.type",
  // STUDENT TEST SET
  STUDENT_TEST_SET_EXISTED_NOT_OPEN_IN_EXAM_CLASS = "error.existed.not.open.in.exam.class",
  // TEST SET
  TEST_SET_NOT_FOUND = "error.test.set.not.found",
  TEST_SET_USED = "error.test.set.used",
  // SYSTEM
  ERROR_FORBIDDEN = "error.forbidden",
  ERROR_UNAUTHORIZED = "error.unauthorized",
  ERROR_SERVER_ERROR = "error.server.error",
  ERROR_BAD_GATEWAY = "error.bad.gateway",
  ERROR_BAD_CREDENTIALS = "user.error.wrong.username.password",
  USED_IN_TEST_SET = "error.question.used.in.test.set",
}

export const HttpErrorMessage: Record<HttpErrorCode | number | string, string> = {
  [HttpErrorCode.UNKNOWN_ERROR_MSG]: "message.UNKNOWN_ERROR_MSG",
  // USER
  [HttpErrorCode.USER_OLD_PASSWORD_NOT_MATCH]: "message.USER_OLD_PASSWORD_NOT_MATCH",
  [HttpErrorCode.USER_NAME_NOT_FOUND]: "message.USER_NAME_NOT_FOUND",
  [HttpErrorCode.EXISTED_USER_NAME]: "message.EXISTED_USER_NAME",
  // COMMON
  [HttpErrorCode.PERMISSION_DENIED]: "message.PERMISSION_DENIED",
  // TEST
  [HttpErrorCode.TEST_NOT_FOUND]: "message.TEST_NOT_FOUND",
  [HttpErrorCode.TEST_NOT_FOUND_BY_TYPE]: "message.TEST_NOT_FOUND_BY_TYPE",
  [HttpErrorCode.TEST_USED_IN_EXAM_CLASSES]: "message.TEST_USED_IN_EXAM_CLASSES",
  [HttpErrorCode.TEST_ASSIGNED_OR_HANDLED_BY_STUDENTS]: "message.TEST_ASSIGNED_OR_HANDLED_BY_STUDENTS",
  // EXAM CLASS
  [HttpErrorCode.EXAM_CLASS_NOT_FOUND_BY_TYPE]: "message.EXAM_CLASS_NOT_FOUND_BY_TYPE",
  // STUDENT TEST SET
  [HttpErrorCode.STUDENT_TEST_SET_EXISTED_NOT_OPEN_IN_EXAM_CLASS]: "message.STUDENT_TEST_SET_EXISTED_NOT_OPEN_IN_EXAM_CLASS",
  // TEST SET
  [HttpErrorCode.TEST_SET_NOT_FOUND]: "message.ERROR_TEST_SET_NOT_FOUND",
  [HttpErrorCode.TEST_SET_USED]: "message.ERROR_TEST_SET_USED",
  // SYSTEM
  [HttpErrorCode.ERROR_FORBIDDEN]: "message.ERROR_FORBIDDEN",
  [HttpErrorCode.ERROR_UNAUTHORIZED]: "message.ERROR_UNAUTHORIZED",
  [HttpErrorCode.ERROR_SERVER_ERROR]: "message.ERROR_SERVER_ERROR",
  [HttpErrorCode.ERROR_BAD_GATEWAY]: "message.ERROR_BAD_GATEWAY",
  [HttpErrorCode.ERROR_BAD_CREDENTIALS]: "message.ERROR_BAD_CREDENTIALS",
  [HttpErrorCode.USED_IN_TEST_SET]: "message.USED_IN_TEST_SET",
};
