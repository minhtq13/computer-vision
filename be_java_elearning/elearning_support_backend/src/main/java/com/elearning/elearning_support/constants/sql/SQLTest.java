package com.elearning.elearning_support.constants.sql;

public class SQLTest {

    public static final String GET_LIST_TEST =
        "WITH testSetCTE AS (" +
            "   SELECT " +
            "           test_id, \n" +
            "           string_agg(code, ',') AS lst_test_set_code \n" +
            "       FROM {h-schema}test_set \n" +
            "       GROUP BY test_id " +
            ") \n" +
        "SELECT \n" +
            "    test.id AS id, \n" +
            "    test.name AS name, \n" +
            "    test.duration AS duration, \n" +
            "    test.question_quantity AS questionQuantity, \n" +
            "    test.created_at AS createdAt, \n" +
            "    test.created_by AS createdBy, \n" +
            "    COALESCE(test.modified_at, test.created_at) AS modifiedAt, \n" +
            "    test.start_time AS startTime, \n" +
            "    test.end_time AS endTime, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectName, \n" +
            "    subject.code AS subjectCode, \n" +
            "    testSetCTE.lst_test_set_code AS lstTestSetCode, \n" +
            "    semester.id AS semesterId, \n" +
            "    semester.code AS semester, \n" +
            "    test.gen_test_config AS genTestConfig, \n" +
            "    CASE \n" +
            "       WHEN test.test_type = 0 THEN 'Offline' \n" +
            "       WHEN test.test_type = 1 THEN 'Online' \n" +
            "       ELSE '' \n" +
            "    END AS testType \n" +
            "FROM {h-schema}test \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "    LEFT JOIN testSetCTE ON testSetCTE.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "WHERE \n" +
            "    test.is_enabled = true AND \n" +
            "    test.deleted_flag = 1 AND \n" +
            "    (-1 IN (:viewableSubjectIds) OR subject.id IN (:viewableSubjectIds)) AND \n" +
            "    (-1 = :subjectId OR subject.id = :subjectId) AND \n" +
            "    ('ALL' = :subjectCode OR subject.code = :subjectCode) AND \n" +
            "    (:startTime = DATE('1970-01-01') OR test.start_time >= :startTime) AND \n" +
            "    (:endTime = DATE('1970-01-01') OR test.end_time <= :endTime) AND \n" +
            "    (:semesterId = -1 OR test.semester_id = :semesterId) AND \n" +
            "    (:semesterCode = '' OR semester.code = :semesterCode) AND \n" +
            "    (:testType = -1 OR test.test_type = :testType) \n";

    public static final String GET_TEST_DETAILS =
        "WITH testSetCTE AS (" +
            "   SELECT " +
            "           test_id, \n" +
            "           string_agg(code, ',') AS lst_test_set_code \n" +
            "       FROM {h-schema}test_set \n" +
            "       GROUP BY test_id " +
            ") \n" +
            "SELECT \n" +
            "    test.id AS id, \n" +
            "    test.name AS name, \n" +
            "    test.duration AS duration, \n" +
            "    test.question_quantity AS questionQuantity, \n" +
            "    test.total_point AS totalPoint, \n" +
            "    test.created_at AS createdAt, \n" +
            "    COALESCE(test.modified_at, test.created_at) AS modifiedAt, \n" +
            "    test.start_time AS startTime, \n" +
            "    test.end_time AS endTime, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectName, \n" +
            "    subject.code AS subjectCode, \n" +
            "    testSetCTE.lst_test_set_code AS lstTestSetCode, \n" +
            "    semester.id AS semesterId, \n" +
            "    semester.code AS semester, \n" +
            "    test.gen_test_config AS genTestConfig, \n" +
            "    CASE \n" +
            "       WHEN test.test_type = 0 THEN 'Offline' \n" +
            "       WHEN test.test_type = 1 THEN 'Online' \n" +
            "       ELSE '' \n" +
            "    END AS testType \n" +
            "FROM {h-schema}test \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "    LEFT JOIN testSetCTE ON testSetCTE.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "WHERE \n" +
            "    test.id = :testId AND \n" +
            "    test.is_enabled = true AND \n" +
            "    test.deleted_flag = 1 \n";

    public static final String SWITCH_TEST_STATUS =
        "UPDATE {h-schema}test SET is_enabled = :isEnabled WHERE id = :testId";


    public static final String GET_TEST_SET_DETAILS_BY_TEST_ID_AND_CODE =
        "SELECT \n" +
            "    testSet.id AS testSetId, \n" +
            "    testSet.test_id AS testId, \n" +
            "    test.name AS testName, \n" +
            "    test.question_quantity AS questionQuantity, \n" +
            "    testSet.test_no AS testNo, \n" +
            "    testSet.code AS testSetCode, \n" +
            "    test.duration AS duration, \n" +
            "    test.is_allowed_using_doc AS isAllowedUsingDocuments, \n" +
            "    semester.code AS semester, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    subject.code AS subjectCode, \n" +
            "    coalesce(department.name, '') AS departmentName, \n" +
            "    testSet.created_at AS createdAt, \n" +
            "    testSet.modified_at AS modifiedAt \n" +
            "FROM {h-schema}test_set AS testSet \n" +
            "    JOIN {h-schema}test ON testSet.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "    LEFT JOIN {h-schema}department ON subject.department_id = department.id \n" +
            "WHERE \n" +
            "    testSet.is_enabled = true AND \n" +
            "    testSet.test_id = :testId AND \n" +
            "    testSet.code = :code";

    public static final String GET_TEST_SET_DETAILS_BY_ID =
        "SELECT \n" +
            "    testSet.id AS testSetId, \n" +
            "    testSet.test_id AS testId, \n" +
            "    test.name AS testName, \n" +
            "    (SELECT COUNT (1) FROM {h-schema}test_set_question WHERE test_set_id = testSet.id) AS questionQuantity, \n" +
            "    testSet.test_no AS testNo, \n" +
            "    testSet.code AS testSetCode, \n" +
            "    test.duration AS duration, \n" +
            "    test.is_allowed_using_doc AS isAllowedUsingDocuments, \n" +
            "    semester.code AS semester, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    subject.code AS subjectCode, \n" +
            "    testSet.created_at AS createdAt, \n" +
            "    testSet.modified_at AS modifiedAt \n" +
            "FROM {h-schema}test_set AS testSet \n" +
            "    JOIN {h-schema}test ON testSet.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "WHERE \n" +
            "    testSet.is_enabled = true AND \n" +
            "    testSet.deleted_flag = 1 AND " +
            "    testSet.id = :testSetId ";

    public static final String GET_LIST_TEST_SET_QUESTION =
        "SELECT \n" +
            "    testSetQuest.id AS testSetQuestionId, \n" +
            "    testSetQuest.question_id AS id, \n" +
            "    question.content AS content, \n" +
            "    question.level AS level, \n" +
            "    testSetQuest.question_no AS questionNo, \n" +
            "    question.is_multiple_ans AS isMultipleAnswers, \n" +
            "    COALESCE({h-schema}get_list_file_json_by_ids_id(question.image_ids), '[]') AS lstImageJson, \n" +
            "    COALESCE({h-schema}get_list_test_question_answer_json(testSetQuest.lst_answer_json), '[]') AS lstAnswerJson \n" +
            "FROM {h-schema}test_set_question testSetQuest \n" +
            "     JOIN {h-schema}question ON testSetQuest.question_id = question.id \n" +
            "WHERE testSetQuest.test_set_id = :testSetId " +
            "ORDER BY testSetQuest.question_no ";

    public static final String DELETE_ALL_TEST_QUESTION_BY_TEST_ID =
        "DELETE FROM {h-schema}test_question WHERE test_id = :testId";

}
