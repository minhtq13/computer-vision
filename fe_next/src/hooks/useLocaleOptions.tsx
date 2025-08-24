import { RoleBaseCode, RoleBaseId } from "@/types/enum";
import { useTranslations } from "next-intl";

const useLocaleOptions = () => {
  const tCommon = useTranslations("common");
  const tStudentTest = useTranslations("studentTest");
  const genderOption = [
    {
      value: "MALE",
      label: tCommon("male"),
    },
    {
      value: "FEMALE",
      label: tCommon("female"),
    },
  ];
  const roleOption = [
    {
      value: RoleBaseCode.ADMIN,
      label: tCommon("admin"),
    },
    {
      value: RoleBaseCode.TEACHER,
      label: tCommon("teacher"),
    },
    {
      value: RoleBaseCode.STUDENT,
      label: tCommon("student"),
    },
  ];

  const levelOptions = [
    {
      value: "ALL",
      label: tCommon("all"),
    },
    {
      value: "EASY",
      label: tCommon("easy"),
    },
    {
      value: "MEDIUM",
      label: tCommon("medium"),
    },
    {
      value: "HARD",
      label: tCommon("hard"),
    },
  ];
  const levelOptionsWithoutAll = [
    {
      value: "EASY",
      label: tCommon("easy"),
    },
    {
      value: "MEDIUM",
      label: tCommon("medium"),
    },
    {
      value: "HARD",
      label: tCommon("hard"),
    },
  ];
  const levelIntOptions = [
    {
      value: -1,
      label: tCommon("all"),
    },
    {
      value: 0,
      label: tCommon("easy"),
    },
    {
      value: 1,
      label: tCommon("medium"),
    },
    {
      value: 2,
      label: tCommon("hard"),
    },
  ];
  const levelIntWithoutAll = [
    {
      value: 0,
      label: tCommon("easy"),
    },
    {
      value: 1,
      label: tCommon("medium"),
    },
    {
      value: 2,
      label: tCommon("hard"),
    },
  ];
  const testTypeOptionsMap = new Map([
    [
      -1,
      {
        value: "ALL",
        key: -1,
        label: tCommon("all"),
      },
    ],
    [
      0,
      {
        value: "OFFLINE",
        key: 0,
        label: tCommon("offline"),
      },
    ],
    [
      1,
      {
        value: "ONLINE",
        key: 1,
        label: tCommon("online"),
      },
    ],
  ]);

  const studentTestStatusMap = new Map([
    [
      -1,
      {
        value: "ALL",
        key: -1,
        label: tCommon("all"),
        color: "",
      },
    ],
    [
      0,
      {
        value: "OPEN",
        key: 0,
        label: tStudentTest("open"),
        color: "#0066ff",
      },
    ],
    [
      1,
      {
        value: "IN_PROGRESS",
        key: 1,
        label: tStudentTest("inProgress"),
        color: "#ff9933",
      },
    ],
    [
      2,
      {
        value: "SUBMITTED",
        key: 2,
        label: tStudentTest("submitted"),
        color: "#49be25",
      },
    ],
    [
      3,
      {
        value: "DUE",
        key: 3,
        label: tStudentTest("due"),
        color: "#ff3300",
      },
    ],
  ]);
  const studentTestStatusOptions = [
    {
      value: 0,
      label: tStudentTest("open"),
    },
    {
      value: 1,
      label: tStudentTest("inProgress"),
    },
    {
      value: 2,
      label: tStudentTest("submitted"),
    },
    {
      value: 3,
      label: tStudentTest("due"),
    },
  ];
  const fileStoredTypeEnum = {
    INTERNAL_SERVER: {
      type: 0,
      value: "INTERNAL_SERVER",
      label: tStudentTest("internalServer"),
    },
    EXTERNAL_SERVER: {
      type: 1,
      value: "EXTERNAL_SERVER",
      label: tStudentTest("externalServer"),
    },
  };
  const testTypeWithoutAll = [
    {
      value: "OFFLINE",
      label: tCommon("offline"),
    },
    {
      value: "ONLINE",
      label: tCommon("online"),
    },
  ];
  const testTypeOptions = [
    {
      value: "ALL",
      key: -1,
      label: tCommon("all"),
    },
    {
      value: "OFFLINE",
      key: 0,
      label: tCommon("offline"),
    },
    {
      value: "ONLINE",
      key: 1,
      label: tCommon("online"),
    },
  ];
  const roleBaseOptions = [
    {
      value: RoleBaseId.STUDENT,
      label: tCommon("student"), // Student
    },
    {
      value: RoleBaseId.TEACHER,
      label: tCommon("teacher"), // Teacher
    },
    {
      value: RoleBaseId.ADMIN,
      label: tCommon("admin"), // Super Admin
    },
  ];

  const renderTag = (item: any) => {
    if (item.level === 0) {
      return tCommon("easy");
    } else if (item.level === 1) {
      return tCommon("medium");
    } else {
      return tCommon("hard");
    }
  };

  return {
    testTypeOptions,
    genderOption,
    roleOption,
    levelOptions,
    levelOptionsWithoutAll,
    levelIntOptions,
    studentTestStatusMap,
    testTypeOptionsMap,
    studentTestStatusOptions,
    fileStoredTypeEnum,
    testTypeWithoutAll,
    levelIntWithoutAll,
    renderTag,
    roleBaseOptions,
  };
};

export default useLocaleOptions;
