package com.elearning.elearning_support.constants.sql;

public class SQLQuestion {

    public static final String GET_LIST_QUESTION_TAG =
        "SELECT question.id as question_id, json_agg(json_build_object('id', tags.id, 'name', tags.name)) as tag_json_str \n" +
            "FROM {h-schema}question \n" +
            "   JOIN {h-schema}tags ON tags.id = any(question.tag_ids) \n" +
            "GROUP BY question.id \n";

    public static final String GET_LIST_QUESTION =
        "WITH questionTagsCTE AS ( \n" +
            GET_LIST_QUESTION_TAG + " )\n" +
        "SELECT \n" +
            "    question.id AS id, \n" +
            "    question.code AS code, \n" +
            "    question.content AS content, \n" +
            "    question.level AS level, \n" +
            "    {h-schema}get_list_answer_json_by_question_id(question.id) AS lstAnswerJson, \n" +
            "    text(questionTagsCTE.tag_json_str) as tagsJsonStr \n" +
            "FROM {h-schema}question \n" +
            "    LEFT JOIN {h-schema}chapter ON question.chapter_id = chapter.id \n" +
            "    LEFT JOIN {h-schema}subject ON chapter.subject_id = subject.id \n" +
            "    LEFT JOIN questionTagsCTE on question.id = questionTagsCTE.question_id \n" +
            "WHERE \n" +
            "    question.is_enabled = true AND \n" +
            "    question.deleted_flag = 1 AND \n" +
            "    (-1 IN (:viewableSubjectIds) OR subject.id IN (:viewableSubjectIds)) AND \n" +
            "    ('' = :search OR {h-schema}custom_unaccent(question.content) ILIKE ('%' || {h-schema}custom_unaccent(:search) || '%')) AND \n" +
            "    (-1 = :questionLevel OR question.level = :questionLevel) AND \n" +
            "    (-1 = :subjectId OR subject.id = :subjectId) AND \n" +
            "    ('' = :subjectCode OR subject.code = :subjectCode) AND \n" +
            "    (-1 IN (:chapterIds) OR chapter.id IN (:chapterIds)) AND \n" +
            "    ('' = :chapterCode OR chapter.code = :chapterCode) AND \n"+
            "    (-1 = :tagId OR :tagId = any(question.tag_ids)) \n";

    public static final String GET_LIST_QUESTION_BY_IDS_IN =
        "WITH questionTagsCTE AS ( \n" +
            GET_LIST_QUESTION_TAG + " )\n" +
        "SELECT \n" +
            "    question.id AS id, \n" +
            "    question.code AS code, \n" +
            "    question.content AS content, \n" +
            "    question.level AS level, \n" +
            "    question.is_multiple_ans AS isMultipleAnswers, \n" +
            "    {h-schema}get_list_answer_json_by_question_id(question.id) AS lstAnswerJson, \n" +
            "    text(questionTagsCTE.tag_json_str) as tagsJsonStr \n" +
            "FROM {h-schema}question \n" +
            "    LEFT JOIN questionTagsCTE on question.id = questionTagsCTE.question_id \n" +
            "WHERE \n" +
            "    question.is_enabled = true AND \n" +
            "    question.deleted_flag = 1 AND \n" +
            "    question.id IN (:questionIds) \n" +
            "ORDER BY COALESCE(question.modified_at, question.created_at) DESC \n";

    public static final String GET_LIST_QUESTION_ALLOWED_USING_IN_TEST =
        "WITH viewableQuestionCTE AS ( \n" +
            "    SELECT question_id \n" +
            "       FROM {h-schema}test_question \n" +
            "       WHERE test_id = :testId \n" +
            "    UNION \n" +
            "    SELECT max(question.id) AS question_id \n" +
            "       FROM {h-schema}question \n" +
            "           JOIN {h-schema}chapter ON question.chapter_id = chapter.id \n" +
            "           JOIN {h-schema}subject ON chapter.subject_id = subject.id AND subject.id IN (SELECT subject_id FROM {h-schema}test WHERE test.id = :testId) \n" +
            "       GROUP BY COALESCE(question.base_id, question.id) \n" +
            "),  \n" +
            "questionTagsCTE as ( \n" + GET_LIST_QUESTION_TAG + " )\n" +
        "SELECT \n" +
            "    question.id AS id, \n" +
            "    question.code AS code, \n" +
            "    question.content AS content, \n" +
            "    question.level AS level, \n" +
            "    {h-schema}get_list_answer_json_by_question_id(question.id) AS lstAnswerJson, \n" +
            "    (testQuest.id IS NOT NULL) AS isInTest, \n" +
            "    question.is_newest AS isNewest, \n" +
            "    text(questionTagsCTE.tag_json_str) as tagsJsonStr, \n" +
            "    coalesce(question.modified_at, question.created_at) as modifiedAt \n" +
            "FROM {h-schema}question \n" +
            "    JOIN viewableQuestionCTE ON question.id = viewableQuestionCTE.question_id \n" +
            "    LEFT JOIN {h-schema}test_question AS testQuest ON testQuest.test_id = :testId AND testQuest.question_id = question.id \n" +
            "    LEFT JOIN questionTagsCTE on question.id = questionTagsCTE.question_id \n" +
            "WHERE \n" +
            "    question.is_enabled = true AND \n" +
            "    question.deleted_flag = 1 AND \n" +
            "    ('' = :search OR question.content ilike ('%' || :search || '%')) AND \n" +
            "    (-1 = :level OR question.level = :level) ";

    public static final String GET_LIST_QUESTION_IDS =
        "WITH newestQuestionCTE AS ( \n" +
            "   SELECT MAX(id) AS newest_question_id FROM {h-schema}question WHERE is_enabled = true AND deleted_flag = 1 GROUP BY COALESCE(base_id, id) \n" +
            ") \n"+
        "SELECT \n" +
            "    question.id AS id \n" +
            "FROM {h-schema}question \n" +
            "    JOIN newestQuestionCTE ON newestQuestionCTE.newest_question_id = question.id \n" +
            "    LEFT JOIN {h-schema}chapter ON question.chapter_id = chapter.id \n" +
            "    LEFT JOIN {h-schema}subject ON chapter.subject_id = subject.id \n" +
            "WHERE \n" +
            "    question.is_enabled = true AND \n" +
            "    question.deleted_flag = 1 AND \n" +
            "    (-1 IN (:viewableSubjectIds) OR subject.id IN (:viewableSubjectIds)) AND \n" +
            "    ('' = :search OR {h-schema}custom_unaccent(question.content) ILIKE ('%' || {h-schema}custom_unaccent(:search) || '%')) AND \n" +
            "    (-1 = :questionLevel OR question.level = :questionLevel) AND \n" +
            "    (-1 = :subjectId OR subject.id = :subjectId) AND \n" +
            "    ('' = :subjectCode OR subject.code = :subjectCode) AND \n" +
            "    (-1 IN (:chapterIds) OR chapter.id IN (:chapterIds)) AND \n" +
            "    ('' = :chapterCode OR chapter.code = :chapterCode) AND \n" +
            "    (-1 = :tagId OR :tagId = any(question.tag_ids)) \n" +
            "ORDER BY COALESCE(question.modified_at, question.created_at) DESC \n";

    public static final String COUNT_LIST_QUESTION_IDS =
        "SELECT COUNT(1) FROM ( " + GET_LIST_QUESTION_IDS + ") AS questions";

    public static final String GET_LIST_QUESTION_IDS_WITH_LIMIT =
        GET_LIST_QUESTION_IDS + "LIMIT :limitSize";

    public static final String GET_LIST_QUESTION_ID_BY_CHAPTER_ID_IN =
        "SELECT id FROM {h-schema}question WHERE chapter_id IN (:lstChapterId) AND deleted_flag = 1 AND is_newest";

    public static final String GET_LIST_QUESTION_ID_IN_TEST =
        "SELECT \n" +
            "   testQuest.question_id AS id, \n" +
            "   question.level AS level, \n" +
            "   '{' || string_agg(CAST(answer.id AS TEXT), ',') || '}' AS lstAnswerId \n" +
            "FROM {h-schema}test_question AS testQuest \n" +
            "   JOIN {h-schema}question ON testQuest.question_id = question.id \n" +
            "   LEFT JOIN {h-schema} answer ON testQuest.question_id = answer.question_id \n" +
            "WHERE testQuest.test_id = :testId AND question.is_enabled = true \n" +
            "GROUP BY testQuest.question_id, question.level";

    public static final String GET_LIST_QUESTION_IN_TEST =
        "WITH questionTagsCTE AS ( \n" +
            GET_LIST_QUESTION_TAG + " )\n" +
        "SELECT \n" +
            "    testQuest.question_id AS id, \n" +
            "    question.code AS code, \n" +
            "    question.content AS content, \n" +
            "    question.level AS level, \n" +
            "    question.is_multiple_ans AS isMultipleAnswers, \n" +
            "    {h-schema}get_list_file_json_by_ids_id(question.image_ids) AS lstImageJson, \n" +
            "    {h-schema}get_list_answer_json_by_question_id(question.id) AS lstAnswerJson, \n" +
            "    (select exists (select * from {h-schema}test_set_question where question.id = question_id)) as isUsed, \n" +
            "    text(questionTagsCTE.tag_json_str) as tagsJsonStr \n" +
            "FROM {h-schema}test_question AS testQuest \n" +
            "   JOIN {h-schema}question ON testQuest.question_id = question.id \n" +
            "   LEFT JOIN questionTagsCTE on question.id = questionTagsCTE.question_id \n" +
            "WHERE \n" +
            "     testQuest.test_id = :testId AND \n" +
            "     question.is_enabled = true AND \n" +
            "     ('' = :search OR question.content ILIKE ('%' || :search || '%')) AND \n" +
            "     (-1 = :questionLevel OR question.level = :questionLevel) AND \n" +
            "     (-1 = :tagId OR :tagId = any(question.tag_ids)) ";

    public static final String GET_QUESTION_DETAILS =
        "SELECT \n" +
            "    question.id AS id, \n" +
            "    question.code AS code, \n" +
            "    question.content AS content, \n" +
            "    question.level AS level, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    chapter.id AS chapterId, \n" +
            "    chapter.title AS chapterTitle, \n" +
            "    {h-schema}get_list_answer_json_by_question_id(question.id) AS lstAnswerJson, \n" +
            "    question.is_multiple_ans AS isMultipleAnswers, \n" +
            "    text(question.tag_ids) as tagIdsStr \n" +
            "FROM {h-schema}question \n" +
            "    LEFT JOIN {h-schema}chapter ON question.chapter_id = chapter.id \n" +
            "    LEFT JOIN {h-schema}subject ON chapter.subject_id = subject.id \n" +
            "WHERE \n" +
            "    question.is_enabled = true AND \n" +
            "    question.deleted_flag = 1 AND \n" +
            "    question.id = :questionId ";

    public static final String EXISTED_IN_USED_TEST_SET = 
        "SELECT ( \n" +
            "    SELECT count(1) \n" +
            "        FROM {h-schema}test_set_question AS testSetQues \n" +
            "           JOIN {h-schema}student_test_set AS stdTestSet ON stdTestSet.test_set_id = testSetQues.test_set_id \n" +
            "        WHERE testSetQues.question_id = :questionId \n" +
            ") > 0 ";

}
