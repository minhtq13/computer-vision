export const Notification_Duration = 3;
export const PAGINATION_OPTIONS = ["10", "20", "50", "100"];
export const COOKIES_NAME = ["access_token", "refresh_token", "roles", "permissions"];
export const SCROLL_TABLE_HEIGHT = 404;
export const SCROLL_TABLE_WIDTH = 1000;
export enum ThemeMode {
  Dark = "dark",
  Light = "light",
}

export const CookieKey = "theme";
export const HUST_COLOR = "#8c1515";
export const COLOR_TAG_A = "#007bff";

export const ROLE_ADMIN_DEPARTMENT_ID = 4;
export const ROLE_ADMIN_SYSTEM_ID = 5;

export const USER_TYPE = {
  ADMIN: -1,
  TEACHER: 0,
  STUDENT: 1,
};

export const ChangePasswordTypeEnum = {
  RESET: "RESET",
  UPDATE: "UPDATE",
};

export const ROLE_ADMIN_DEPARTMENT_CODE = "ROLE_ADMIN_DEPARTMENT";

export const ROLE_ADMIN_SYSTEM_CODE = "ROLE_ADMIN_SYSTEM";

export const PAGE_SIZE_OPTIONS = [
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
];

export const courseNumOptions = [
  {
    value: 61,
    label: "61",
  },
  {
    value: 62,
    label: "62",
  },
  {
    value: 63,
    label: "63",
  },
  {
    value: 64,
    label: "64",
  },
  {
    value: 65,
    label: "65",
  },
  {
    value: 66,
    label: "66",
  },
  {
    value: 67,
    label: "67",
  },
  {
    value: 68,
    label: "68",
  },
  {
    value: 69,
    label: "69",
  },
];
export const NUMBER_ANSWER = [
  {
    text: "30",
    value: 30,
  },
  {
    text: "60",
    value: 60,
  },
];

export const SCORING_NUMBER_ANSWER = [
  {
    text: "20",
    value: 20,
  },
  {
    text: "40",
    value: 40,
  },
  {
    text: "60",
    value: 60,
  },
];

export const testTypeEnum = {
  ALL: {
    type: -1,
    value: "ALL",
  },
  OFFLINE: {
    type: 0,
    value: "OFFLINE",
  },
  ONLINE: {
    type: 1,
    value: "ONLINE",
  },
};

// student test status enums
export const studentTestStatusEnum = {
  ALL: -1,
  OPEN: 0,
  IN_PROGRESS: 1,
  SUBMITTED: 2,
  DUE: 3,
};

export const dateTimePattern = {
  FORMAT_DATE_DD_MM_YYYY_HH_MM_SS: "DD/MM/YYYY HH:mm:ss",
  FORMAT_DATE_HH_MM_YYYY_HH_MM: "HH:mm DD/MM/YYYY",
  FORMAT_DD_MM_YYYY_SLASH: "DD/MM/YYYY",
};
export const searchTimeDebounce = 1000;
