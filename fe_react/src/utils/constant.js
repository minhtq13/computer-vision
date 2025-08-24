export const HUST_COLOR = "#8c1515";
export const COLOR_TAG_A = "#007bff";
export const ROLE_ADMIN = -1;
export const ROLE_TEACHER = 0;
export const ROLE_STUDENT = 1;
export const ROLE_ID_ADMIN = 1;
export const ROLE_ID_TEACHER = 3;
export const ROLE_ID_STUDENT = 2;

export const ROLE_ADMIN_DEPARTMENT_ID = 4;
export const ROLE_ADMIN_SYSTEM_ID = 5;

export const genderOption = [
	{
		value: "MALE",
		label: "Nam",
	},
	{
		value: "FEMALE",
		label: "Nữ",
	},
];

export const UserTypeEnum = {
    ADMIN: -1,
    TEACHER: 0,
    STUDENT: 1
}

export const ChangePasswordTypeEnum = {
    RESET: "RESET", UPDATE: "UPDATE"
}

export const roleOption = [
	{
		value: ROLE_ADMIN,
		label: "Admin",
	},
	{
		value: ROLE_TEACHER,
		label: "Giảng viên",
	},
	{
		value: ROLE_STUDENT,
		label: "Sinh viên",
	},
];

export const roleMap = new Map([
    ["ROLE_SUPER_ADMIN", "Super Admin"],
    ["ROLE_TEACHER", "Giảng viên"],
    ["ROLE_STUDENT", "Sinh viên"]
]);

// Base roles code
export const ROLE_SUPER_ADMIN_CODE = "ROLE_SUPER_ADMIN";

export const ROLE_ADMIN_DEPARTMENT_CODE = "ROLE_ADMIN_DEPARTMENT";

export const ROLE_ADMIN_SYSTEM_CODE = "ROLE_ADMIN_SYSTEM";

export const ROLE_TEACHER_CODE = "ROLE_TEACHER";

export const ROLE_STUDENT_CODE = "ROLE_STUDENT";


export const levelOptions = [
  {
    value: "ALL",
    label: "Tất cả",
  },
  {
    value: "EASY",
    label: "Dễ",
  },
  {
    value: "MEDIUM",
    label: "Trung bình",
  },
  {
    value: "HARD",
    label: "Khó",
  },
];

export const fetchSizeOptions = [
    {
        value: 10,
        label: "10",
    },
    {
        value: 20,
        label: "20",
    },
    {
        value: 50,
        label: "50",
    },
    {
        value: 100,
        label: "100",
    },
    {
        value: -1,
        label: "Tất cả"
    }
];

export const levelIntOptions = [
    {
        value: -1,
        label: "Tất cả",
    },
    {
        value: 0,
        label: "Dễ",
    },
    {
        value: 1,
        label: "Trung bình",
    },
    {
        value: 2,
        label: "Khó",
    },
];
export const courseNumOptions = [
    {
        value: 61,
        label: "61"
    },
    {
        value: 62,
        label: "62"
    },
	{
		value: 63,
		label: "63"
	},
	{
		value: 64,
		label: "64"
	},
	{
		value: 65,
		label: "65"
	},
	{
		value: 66,
		label: "66"
	},
	{
		value: 67,
		label: "67"
	},
    {
        value: 68,
        label: "68"
    },
    {
        value: 69,
        label: "69"
    }
];
export const numberAnswerOption = [
  {
    text: "15 câu",
    value: 15,
  },
  {
    text: "20 câu",
    value: 20,
  },
  {
    text: "25 câu",
    value: 25,
  },
  {
    text: "30 câu",
    value: 30,
  },
  {
    text: "40 câu",
    value: 40,
  },
  {
    text: "60 câu",
    value: 60,
  }
];

export const testTypeOptionsMap = new Map([
    [-1, {
        value: "ALL",
        key: -1,
        label: "Tất cả",
    }],
    [0, {
        value: "OFFLINE",
        key: 0,
        label: "Offline",
    }],
    [1, {
        value: "ONLINE",
        key: 1,
        label: "Online",
    }],
]);

export const testTypeEnum = {
    ALL: {
        type: -1,
        value: "ALL"
    },
    OFFLINE: {
        type: 0,
        value: "OFFLINE"
    },
    ONLINE: {
        type: 1,
        value: "ONLINE"
    }
};

export const studentTestStatusMap = new Map([
    [-1, {
        value: "ALL",
        key: -1,
        label: "Tất cả",
        color: "",
    }],
    [0, {
        value: "OPEN",
        key: 0,
        label: "Chưa làm bài (Đang mở)",
        color: "#0066ff"
    }],
    [1, {
        value: "IN_PROGRESS",
        key: 1,
        label: "Đang làm bài",
        color: "#ff9933"
    }],
    [2, {
        value: "SUBMITTED",
        key: 2,
        label: "Đã nộp bài",
        color: "#49be25"
    }],
    [3, {
        value: "DUE",
        key: 3,
        label: "Quá hạn",
        color: "#ff3300"
    }],
]);

export const studentTestStatusOptions = [
    {
        value: 0,
        label: "Chưa làm bài",
    },
    {
        value: 1,
        label: "Đang làm bài"
    },
    {
        value: 2,
        label: "Đã nộp bài"
    },
    {
        value: 3,
        label: "Quá hạn"
    }
];

// student test status enums
export const studentTestStatusEnum = {
    ALL: -1,
    OPEN: 0,
    IN_PROGRESS: 1,
    SUBMITTED: 2,
    DUE: 3
}
export const fileStoredTypeEnum = {
    INTERNAL_SERVER: {
        type: 0,
        value: "INTERNAL_SERVER",
        label: "Lưu trữ trong máy chủ",
    },
    EXTERNAL_SERVER: {
        type: 1,
        value: "EXTERNAL_SERVER",
        label: "Lưu trữ ở máy chủ bên thứ 3",
    }
}

export const dateTimePattern = {
    FORMAT_DATE_DD_MM_YYYY_HH_MM_SS: "DD/MM/YYYY HH:mm:ss",
    FORMAT_DATE_HH_MM_YYYY_HH_MM : "HH:mm DD/MM/YYYY",
    FORMAT_DD_MM_YYYY_SLASH: "DD/MM/YYYY"
}
export const searchTimeDebounce = 1500;