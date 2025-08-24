package com.elearning.elearning_support.constants.sql;

public class SQLTestSet {

    public static final String GET_LIST_TEST_QUESTION_CORRECT_ANSWER =
        "SELECT \n" +
            "    testSetQuest.id AS id, \n" +
            "    testSetQuest.test_set_id AS testSetId, \n" +
            "    testSetQuest.question_id AS questionId, \n" +
            "    testSetQuest.question_no AS questionNo, \n" +
            "    COALESCE(testSetQuest.question_mark, 0) AS questionMark, \n" +
            "    {h-schema}get_correct_in_lst_answer_json(testSetQuest.lst_answer_json) AS correctAnswerNo \n" +
            "FROM {h-schema}test_set_question AS testSetQuest \n" +
            "WHERE testSetQuest.test_set_id = :testSetId ";

    public static final String GET_LIST_TEST_SET_GENERAL_SCORING_DATA =
        "SELECT  \n" +
            "    testSet.id AS testSetId, \n" +
            "    testSet.code AS testSetCode, \n" +
            "    exClass.id AS examClassId, \n" +
            "    exClass.code AS examClassCode, \n" +
            "    testSet.question_mark AS questionMark \n" +
            "FROM {h-schema}test_set AS testSet \n" +
            "    JOIN {h-schema}test ON testSet.test_id = test.id \n" +
            "    JOIN {h-schema}exam_class AS exClass ON exClass.test_id = test.id \n" +
            "WHERE exClass.code IN (:examClassCodes) AND testSet.code IN (:testCodes)";

    public static final String GET_MAX_CURRENT_TEST_NO_BY_TEST_ID =
        "SELECT MAX(CAST(test_no AS int4)) AS maxTestNo FROM {h-schema}test_set WHERE test_id = :testId";

    public static final String DELETE_ALL_BY_TEST_ID =
        "DELETE FROM {h-schema}test_set WHERE test_id = :testId";

    public static final String DELETE_TEST_SET_QUESTION_BY_TEST_SET_IDS_IN =
        "DELETE FROM {h-schema}test_set_question WHERE test_set_id IN (:testSetIds)";

    public static final String GET_LIST_TEST_SET_PREVIEW_BY_TEST_ID =
        "SELECT \n" +
            "   id AS testSetId," +
            "   code AS testSetCode, \n" +
            "   test_no AS testSetNo, \n" +
            "   test_id AS testId, \n" +
            "   (SELECT COUNT(1) FROM {h-schema}student_test_set WHERE test_set_id = test_set.id) > 0 AS isUsed, \n" +
            "   (SELECT COUNT(1) FROM {h-schema}student_test_set WHERE test_set_id = test_set.id AND status > 0) > 0 AS isHandled \n" +
            "FROM {h-schema}test_set \n" +
            "WHERE \n" +
            "   test_id = :testId AND \n" +
            "   deleted_flag = 1 AND \n" +
            "   is_enabled IS true";

}
