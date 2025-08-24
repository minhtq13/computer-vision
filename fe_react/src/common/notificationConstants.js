import {appPath} from "../config/appPath";


export const notificationObjectTypeEnum = {
    USER: 1,
    EXAM_CLASS: 2,
    ONLINE_TEST_DETAIL: 3
}

export const mapNotificationDirectPath = new Map([
    // exam class
    [1000, {
        code: "SAVED_EXAM_CLASS_SCORING_RESULT_SUCCESSFULLY",
        directPath: `${appPath.examClassDetail}/$OBJECT_IDENTIFIER`,
        objectType: notificationObjectTypeEnum.EXAM_CLASS
    }],
    // online test result
    [1001, {
        code: "SAVED_ONLINE_SCORING_RESULT_SUCCESSFULLY",
        directPath: `${appPath.onlineStudentTestDetails}/$OBJECT_IDENTIFIER`,
        objectType: notificationObjectTypeEnum.ONLINE_TEST_DETAIL
    }]
]);