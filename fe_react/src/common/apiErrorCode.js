export const User = {
    USER_OLD_PASSWORD_NOT_MATCH: {code: "user.error.old.password.not.match", message: "Mật khẩu hiện tại không hợp lệ"},
    USER_NAME_NOT_FOUND: {code: "error.auth.username.not.found", message: "Tài khoản không tồn tại"},
    EXISTED_USER_NAME: {code: "user.error.username.existed", message: "Tên đăng nhập đã tồn tại"}
};

export const Common = {
    PERMISSION_DENIED: {
        code: "user.permission.denied",
        message: "Không có quyền!"
    }
}

export const Test = {
    NOT_FOUND: {
        code: "error.test.not.found",
        message: "Bộ đề thi không tồn tại"
    },
    NOT_FOUND_BY_TYPE: {
        code: "error.test.not.found.by.type",
        message: "Bộ đề thi không tồn tại hoặc không áp dụng cho hình thức thi online"
    },
    USED_IN_EXAM_CLASSES: {
        code: "error.test.used.in.exam.classes",
        message: "Đề thi đã được sử dụng trong các lớp thi"
    },
    ASSIGNED_OR_HANDLED_BY_STUDENTS: {
        code: "error.test.assigned.handled.by.students",
        message: "Đề thi đã được giao hoặc làm bởi các thí sinh"
    },
};

export const ExamClass = {
    NOT_FOUND_BY_TYPE: {
        "code": "error.exam.class.not.found.by.type",
        "message": "Không tìm thấy lớp thi có hình thức thi"
    }
}

export const StudentTestSet = {
    EXISTED_NOT_OPEN_IN_EXAM_CLASS: {
        "code": "error.existed.not.open.in.exam.class",
        "message": "Không thể assign do các bài thi đang được thực hiện!"
    }
}

export const TestSet = {
    NOT_FOUND: {"code": "error.test.set.not.found", "message": "Không tìm thấy đề thi"},
    USED: {"code": "error.test.set.used", "message": "Đề thi đã được sử dụng"}
}

export const System = {
    FORBIDDEN: {code: "error.forbidden", message: "Không có quyền truy cập"},
    UNAUTHORIZED: {code: "error.unauthorized", message: "Token hết hạn hoặc không hợp lệ"},
    SERVER_ERROR: {code: "error.server.error", message: "Lỗi máy chủ"},
    BAD_GATEWAY: {code: "error.bad.gateway", message: "Bad Gateway"},
    BAD_CREDENTIALS: {code: "user.error.wrong.username.password", message: "Sai thông tin tài khoản / mật khẩu !"},
    USERNAME_NOT_FOUND: {code: "error.auth.username.not.found", message: "Tài khoản chưa tồn tại trong hệ thống !"}
}

export const Question = {
    USED_IN_TEST_SET: {code: "error.question.used.in.test.set", message: "Câu hỏi đã được sử dụng trong đề thi"}
}

export const UNKNOWN_ERROR_MSG = "Lỗi không xác định";

export const errorCodeMap = new Map([
    // User
    [User.USER_OLD_PASSWORD_NOT_MATCH.code, User.USER_OLD_PASSWORD_NOT_MATCH],
    [User.EXISTED_USER_NAME.code, User.EXISTED_USER_NAME],

    // Test Set
    ["error.test.set.not.found", TestSet.NOT_FOUND],
    ["error.test.set.used", TestSet.USED],

    // Test
    ["error.test.not.found", Test.NOT_FOUND],
    ["error.test.not.found.by.type", Test.NOT_FOUND_BY_TYPE],
    ["error.test.used.in.exam.classes", Test.USED_IN_EXAM_CLASSES],
    ["error.test.assigned.handled.by.students", Test.ASSIGNED_OR_HANDLED_BY_STUDENTS],

    // Question
    [Question.USED_IN_TEST_SET.code, Question.USED_IN_TEST_SET],

    // common
    [Common.PERMISSION_DENIED.code, Common.PERMISSION_DENIED],
    // System
    [System.BAD_CREDENTIALS.code, System.BAD_CREDENTIALS],
    [System.USERNAME_NOT_FOUND.code, System.USERNAME_NOT_FOUND]
]);