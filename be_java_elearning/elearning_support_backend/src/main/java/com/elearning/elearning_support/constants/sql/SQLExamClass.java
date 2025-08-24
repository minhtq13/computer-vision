package com.elearning.elearning_support.constants.sql;

public class SQLExamClass {

    public static final String GET_COUNTER_EXAM_CLASS_PARTICIPANT =
        "SELECT \n" +
            "    exam_class_id AS examClassId, \n" +
            "    string_agg(CAST(user_id AS TEXT), ',') FILTER (WHERE role_type = 0) AS lstStudentId, \n" +
            "    string_agg(CAST(user_id AS TEXT), ',') FILTER (WHERE role_type in (1,2)) AS lstSupervisorId, \n" +
            "    string_agg(CAST(user_id AS TEXT), ',') FILTER (WHERE role_type = 2) AS lstLecturerId, \n" +
            "    COUNT(user_id) FILTER (WHERE role_type in (1,2)) AS numSupervisors, \n" +
            "    COUNT(user_id) FILTER (WHERE role_type = 0) AS numStudents \n" +
            "FROM {h-schema}user_exam_class \n" +
            "GROUP BY exam_class_id \n";

    public static final String EXISTS_STUDENT_TEST_SET_IN_EXAM_CLASS =
        "SELECT exam_class_id, count(*) as number \n" +
            "FROM {h-schema}student_test_set \n" +
            "GROUP BY exam_class_id \n";

    public static final String GET_LIST_EXAM_CLASS =
        "WITH examClassParticipantCTE AS ( " + GET_COUNTER_EXAM_CLASS_PARTICIPANT + " ) \n" +
            "SELECT \n" +
            "    exClass.id AS id, \n" +
            "    exClass.code AS code, \n" +
            "    exClass.room_name AS roomName, \n" +
            "    DATE(exClass.examine_time) AS examineDate, \n" +
            "    exClass.examine_time AS examineTime, \n" +
            "    test.id AS testId, \n" +
            "    test.name AS testName, \n" +
            "    test.duration AS duration, \n" +
            "    semester.id AS semesterId, \n" +
            "    semester.code AS semester, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    examClassParticipantCTE.numStudents AS numberOfStudents, \n" +
            "    examClassParticipantCTE.numSupervisors AS numberOfSupervisors, \n" +
            "    CASE \n" +
            "       WHEN test.test_type = 0 THEN 'Offline' \n" +
            "       WHEN test.test_type = 1 THEN 'Online' \n" +
            "       ELSE '' \n" +
            "    END AS testType, \n" +
            "    COALESCE(exClass.modified_at, exClass.created_at) as modifiedAt \n" +
            "FROM {h-schema}exam_class AS exClass \n" +
            "    JOIN {h-schema}test ON exClass.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "    LEFT JOIN examClassParticipantCTE ON exClass.id = examClassParticipantCTE.examClassId \n" +
            "WHERE \n" +
            "    exClass.is_enabled = true AND \n" +
            "    exClass.deleted_flag = 1 AND \n" +
            "    ('' = :code OR exClass.code ILIKE ('%' || :code || '%')) AND \n" +
            "    (-1 = :semesterId OR test.semester_id = :semesterId) AND \n" +
            "    (-1 = :testId OR exClass.test_id = :testId) AND \n" +
            "    (-1 = :subjectId OR subject.id = :subjectId) AND \n" +
            "    (:currentUserId = -1 OR \n" +
            "       :currentUserId = exClass.created_by OR \n" +
            "       :currentUserId = ANY(CAST(('{' || examClassParticipantCTE.lstSupervisorId || '}') AS int8[])) OR \n" +
            "       :currentUserId = ANY(CAST(('{' || examClassParticipantCTE.lstStudentId || '}') AS int8[])) \n" +
            "    )";

    public static final String GET_DETAILS_EXAM_CLASS =
        "WITH examClassParticipantCTE AS ( " + GET_COUNTER_EXAM_CLASS_PARTICIPANT + " ), \n" +
            "testSetCTE AS ( \n" +
            "   SELECT \n" +
            "           test_id, \n" +
            "           STRING_AGG(TEXT(id), ',') AS lst_test_set_id, \n" +
            "           STRING_AGG(code, ',') AS lst_test_set_code \n" +
            "      FROM {h-schema}test_set \n" +
            "      GROUP BY test_id \n" +
            ") \n"    +
            "SELECT \n" +
            "    exClass.id AS id, \n" +
            "    exClass.code AS code, \n" +
            "    exClass.room_name AS roomName, \n" +
            "    exClass.examine_time AS examineTime, \n" +
            "    exClass.examine_time AS examineDate, \n" +
            "    CASE \n" +
            "       WHEN exClass.test_type = 0 THEN 'Offline' \n" +
            "       WHEN exClass.test_type = 1 THEN 'Online' \n" +
            "       ELSE '' \n" +
            "    END AS testType, \n" +
            "    test.id AS testId, \n" +
            "    test.name AS testName, \n" +
            "    test.duration as duration, \n" +
            "    semester.id AS semesterId, \n" +
            "    semester.code AS semester, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    coalesce(examClassParticipantCTE.lstStudentId, '') AS lstStudentId, \n" +
            "    coalesce(examClassParticipantCTE.lstSupervisorId, '') AS lstSupervisorId, \n" +
            "    coalesce(examClassParticipantCTE.lstLecturerId, '') AS lstLecturerIdStr, \n" +
            "    examClassParticipantCTE.numStudents AS numberOfStudents, \n" +
            "    examClassParticipantCTE.numSupervisors AS numberOfSupervisors, \n" +
            "    COALESCE(exClass.modified_at, exClass.created_at) AS lastModifiedAt, \n" +
            "    testSetCTE.lst_test_set_id AS lstTestSetId, \n" +
            "    testSetCTE.lst_test_set_code AS lstTestSetCode, \n" +
            "    (exClassResult.number > 0) AS existedResult \n" +
            "FROM {h-schema}exam_class AS exClass \n" +
            "    JOIN {h-schema}test ON exClass.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "    LEFT JOIN examClassParticipantCTE ON exClass.id = examClassParticipantCTE.examClassId \n" +
            "    LEFT JOIN testSetCTE ON testSetCTE.test_id = exClass.test_id \n" +
            "    LEFT JOIN (" + EXISTS_STUDENT_TEST_SET_IN_EXAM_CLASS + ") AS exClassResult ON exClassResult.exam_class_id = exClass.id \n"+
            "WHERE \n" +
            "    exClass.is_enabled = true AND \n" +
            "    exClass.deleted_flag = 1 AND \n" +
            "    exClass.id = :id ";

    public static final String GET_LIST_EXAM_CLASS_PARTICIPANT =
        "SELECT \n" +
            "   participant.id AS id, \n" +
            "   CONCAT_WS(' ', participant.last_name, participant.first_name) AS name, \n" +
            "   participant.email as email, \n" +
            "   participant.code AS code, \n" +
            "   userExClass.role_type AS roleType, \n" +
            "   CASE \n" +
            "       WHEN userExClass.role_type = 0 THEN 'Thí sinh' \n" +
            "       WHEN userExClass.role_type = 1 THEN 'Giám thị' \n" +
            "       WHEN userExClass.role_type = 2 THEN 'GV giảng dạy'" +
            "       ELSE '' \n" +
            "   END AS roleName, \n" +
            "   exClass.id AS examClassId, \n" +
            "   exClass.code AS examClassCode \n" +
            "FROM {h-schema}user_exam_class AS userExClass \n" +
            "    JOIN {h-schema}users AS participant ON userExClass.user_id = participant.id \n" +
            "    JOIN {h-schema}exam_class AS exClass ON exClass.id = userExClass.exam_class_id \n" +
            "WHERE userExClass.exam_class_id = :examClassId AND (userExClass.role_type in (:roleTypes)) \n" +
            "ORDER BY split_part(participant.first_name, ' ', -1) ASC";

    public static final String DELETE_EXAM_CLASS_PARTICIPANT_BY_ID =
        "DELETE FROM {h-schema}user_exam_class WHERE exam_class_id = :examClassId";

    public static final String GET_LIST_EXAM_CLASS_PARTICIPANT_ID =
        "SELECT \n" +
            "   userExClass.user_id AS id \n" +
            "FROM {h-schema}user_exam_class AS userExClass \n" +
            "WHERE userExClass.exam_class_id = :examClassId AND (:roleType = -1 OR userExClass.role_type = :roleType)";

    public static final String GET_LIST_EXAM_CLASS_NAME_BY_IDS_IN =
        "SELECT  \n" +
            "    concat_ws(' - ', subject.title, subject.code, exClass.code)  \n" +
            "FROM {h-schema}exam_class AS exClass \n" +
            "    LEFT JOIN {h-schema}test ON test.id = exClass.test_id \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "WHERE exClass.id IN (:examClassIds) ";



}
