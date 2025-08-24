package com.elearning.elearning_support.constants.message.messageConst;

public class MessageConst {

    public static class Resources {
        public static final String QUESTION = "question";

        public static final String SUBJECT = "subject";

        public static final String CHAPTER = "chapter";

        public static final String TEST = "test";

        public static final String TEST_SET = "test_set";

        public static final String STUDENT_TEST_SET = "student_test_set";

        public static final String FILE_ATTACHED = "file_attached";

        public static final String USER = "user";

        public static final String EXAM_CLASS = "exam_class";

        public static final String PERMISSION = "permission";

        public static final String ROLE = "role";

        public static final String USER_EXAM_CLASS = "user_exam_class";

        public static final String AUTH_INFORMATION = "auth_information";

        public static final String COURSE = "course";

    }

    public static final String RESOURCE_NOT_FOUND = "not found";

    public static final String RESOURCE_EXISTED = "existed";

    public static final String RESOURCE_OVERLAPPED = "overlapped";
    public static final String RESOURCE_DUPLICATED = "duplicated";

    public static final String PERMISSIONS_DENIED = "permission denied";

    public static final String UPLOAD_FAILED = "upload failed";

    public static final String UNAUTHORIZED = "unauthorized";

    public static final String PATTERN_NOT_MATCH = "pattern not match";

    public static final String INVALID_FIELD_LENGTH = "invalid length";

    public static final String USER_NOT_MATCH_ROLE_BASE_ID = "not match role base id";

    public static class Validation {
        public static final String NOT_NULL = "Must be not null";

        public static final String NOT_BLANK = "Must be not blank";

        public static final String PATTERN_NOT_MATCH = "Pattern not match";

    }

    public static final String EXCEPTION_LOG_FORMAT = "======= EXCEPTION: {} CAUSE BY {} ========";

    public static class CommonError {
        public static final String PERMISSIONS_DENIED = "user.permission.denied";
    }

    public static class Question {
        public static final String NOT_FOUND = "error.question.not.found";

        public static final String USED_IN_TEST_SET = "error.question.used.in.test.set";
    }

    public static class Subject {
        public static final String NOT_FOUND = "error.subject.not.found";

        public static final String EXISTED_CODE = "error.subject.existed.code";
    }

    public static class Chapter {
        public static final String NOT_FOUND = "error.chapter.not.found";
        public static final String EXISTED_CODE = "error.chapter.existed.code";

        public static final String EXISTED_ORDERS = "error.chapter.existed.orders";
    }

    public static class Test {
        public static final String NOT_FOUND = "error.test.not.found";

        public static final String NOT_FOUND_BY_TYPE = "error.test.not.found.by.type";

        public static final String USED_IN_EXAM_CLASSES = "error.test.used.in.exam.classes";

        public static final String ASSIGNED_OR_HANDLED_BY_STUDENTS = "error.test.assigned.handled.by.students";

    }

    public static class TestSet {
        public static final String NOT_FOUND = "error.test.set.not.found";
        public static final String EXISTED_BY_CODE = "error.test.set.existed.code";

        public static final String USED = "error.test.set.used";
    }

    public static class StudentTestSet {

        public static final String NOT_FOUND = "error.student.test.set.not.found";

        public static final String EXISTED_NOT_OPEN_IN_EXAM_CLASS = "error.existed.not.open.in.exam.class";
    }

    public static class FileAttach {

        public static final String UPLOAD_ERROR_CODE = "file_attach.error.upload";
        public static final String NOT_FOUND_ERROR_CODE = "file_attach.error.not.found";

        public static final String EXISTED_ERROR_CODE = "file_attach.error.existed";

        public static final String DUPLICATED_ERROR_CODE = "file_attach.error.duplicated";

        public static final String PERMISSION_DENIED_ERROR_CODE = "file_attach.error.permission.denied";

        public static final String FILE_EXCESS_SIZE_ERROR_CODE = "file.error.upload.excess.size";

        public static final String FILE_INVALID_EXTENSION_ERROR_CODE = "file.error.upload.invalid.extension";

        public static final String FILE_EXCESS_FILENAME_LENGTH_ERROR_CODE = "file.upload.excess.filename.length.error";

        // excel file

        public static final String FILE_EXCEL_EXCESS_COLUMN_NUMBER_ERROR = "file.excel.excess.column.number";

        public static final String FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR = "file.excel.missing.column.number";

        public static final String FILE_EXCEL_EMPTY_SHEET_ERROR = "file.error.excel.empty.sheet";

        public static final String FILE_EXCEL_INVALID_COLUMN_NAME = "file.error.excel.invalid.column.name";

    }

    public static class User {
        public static final String USER_NOT_FOUND_ERROR_CODE = "user.error.not.found";

        public static final String USER_USERNAME_EXISTED_ERROR = "user.error.username.existed";

        public static final String USER_EMAIL_EXISTED_ERROR = "user.email.existed.error";

        public static final String USER_CODE_EXISTED_ERROR = "user.error.code.existed";

        public static final String USER_CODE_AND_USER_TYPE_EXISTED_ERROR = "user.error.code.user_type.existed";

        public static final String USER_WRONG_USERNAME_OR_PASSWORD_ERROR_CODE = "user.error.wrong.username.password";

        public static final String USER_OLD_PASSWORD_NOT_MATCH = "user.error.old.password.not.match";

        public static final String USER_NOT_MATCH_ROLE_BASE_ID = "user.error.not.match.role.base.id";
    }

    public static class ExamClass {
        public static final String NOT_FOUND = "error.exam.class.not.found";

        public static final String EXISTED_BY_CODE = "error.exam.class.existed.code";

        public static final String NOT_FOUND_BY_TYPE = "error.exam.class.not.found.by.type";
    }

    public static class Permission {
        public static final String NOT_FOUND = "error.permission.not.found";
        public static final String EXISTED_BY_CODE = "error.permission.existed.code";
    }

    public static class Role {
        public static final String NOT_FOUND = "error.role.not.found";
        public static final String EXISTED_BY_CODE = "error.role.existed.code";
        public static final String ROLE_NOT_MATCH_USER_TYPE = "error.role.not.match.user.type";
        public static final String ROLE_BASE_ID_EXISTED = "error.role.base.id.existed";
    }

    public static class Course {
        public static final String NOT_FOUND = "error.course.not.found";

        public static final String EXISTED_BY_CODE = "error.course.existed.code";
    }

    public static class UserExamClass {
        public static final String STUDENT_NOT_FOUND = "error.student.exam.class.not_found";

        public static final String SUPERVISOR_NOT_FOUND = "error.supervisor.exam.class.not_found";
    }

    public static class AuthInfo {

        public static final String NOT_FOUND = "error.auth.info_not_found";

        public static final String USER_NAME_NOT_FOUND = "error.auth.username.not.found";

        public static final String WRONG_USERNAME_PASSWORD = "error.auth.wrong.username.password";

        public static final String ACCESS_TOKEN_INVALID = "error.auth.access.token.invalid";

        public static final String REFRESH_TOKEN_EXPIRED = "error.auth.refresh.token.expired";
    }

}
