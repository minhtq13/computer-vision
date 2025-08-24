package com.elearning.elearning_support.constants.sql;

public class SQLStudentTestSet {

    public static final String GET_LIST_STUDENT_TEST_SET_RESULT =
        "WITH stdTestSetResultCTE AS ( \n" +
            "SELECT \n" +
            "       stdTestSetDetail.student_test_set_id, \n" +
            "       COUNT(1) FILTER ( WHERE stdTestSetDetail.is_corrected is TRUE) AS num_correct_answers, \n" +
            "       SUM(COALESCE(testSetQuest.question_mark, 0)) FILTER (WHERE stdTestSetDetail.is_corrected is TRUE) AS total_points, \n" +
            "       STRING_AGG(text(testSetQuest.question_no), ',' ORDER BY testSetQuest.question_no) FILTER (WHERE stdTestSetDetail.is_corrected is TRUE) as correct_answers \n" +
            "   FROM {h-schema}view_student_test_set_detail AS stdTestSetDetail \n" +
            "       JOIN {h-schema}test_set_question AS testSetQuest ON testSetQuest.id = stdTestSetDetail.test_set_question_id \n" +
            "   GROUP BY stdTestSetDetail.student_test_set_id \n" +
            "), \n" +
         "studentTestSetCTE AS ( \n" +
            "  SELECT \n" +
            "      stdTestSet.id AS id, \n" +
            "      stdTestSet.student_id AS student_id, \n" +
            "      stdTestSet.test_set_id AS test_set_id, \n" +
            "      testSet.code AS test_set_code, \n" +
            "      stdTestSet.marked AS num_marked_answers, \n" +
            "      stdTestSet.handled_test_file_id as handed_img_id, \n" +
            "      stdTestSet.status AS status, \n" +
            "      CASE \n" +
            "         WHEN stdTestSet.status = 0 THEN 'Chưa làm bài' \n" +
            "         WHEN stdTestSet.status = 1 THEN 'Đang làm bài' \n" +
            "         WHEN stdTestSet.status = 2 THEN 'Đã nộp bài' \n" +
            "         WHEN stdTestSet.status = 3 THEN 'Quá hạn' \n" +
            "         ELSE 'Không có dữ liệu' \n" +
            "      END AS status_label, \n" +
            "      stdTestSetResultCTE.num_correct_answers AS num_correct_answers, \n" +
            "      stdTestSetResultCTE.total_points AS total_points, \n" +
            "      stdTestSetResultCTE.correct_answers AS correct_answers \n" +
            "  FROM {h-schema}student_test_set AS stdTestSet \n" +
            "      LEFT JOIN stdTestSetResultCTE ON stdTestSetResultCTE.student_test_set_id = stdTestSet.id \n" +
            "      LEFT JOIN {h-schema}test_set AS testSet ON testSet.id = stdTestSet.test_set_id \n" +
            "  WHERE \n" +
            "      stdTestSet.exam_class_id = :examClassId \n" +
            "    ) \n" +
         "SELECT \n" +
            "    studentTestSetCTE.id AS id, \n" +
            "    studentTestSetCTE.student_id AS studentId, \n" +
            "    CONCAT_WS(' ', users.last_name, users.first_name) AS studentName, \n" +
            "    users.code AS studentCode, \n" +
            "    studentTestSetCTE.test_set_id AS testSetId, \n" +
            "    studentTestSetCTE.test_set_code AS testSetCode, \n" +
            "    studentTestSetCTE.status AS status, \n" +
            "    studentTestSetCTE.status_label AS statusLabel, \n" +
            "    (SELECT COUNT(1) FROM {h-schema}test_set_question WHERE test_set_id = studentTestSetCTE.test_set_id) numTestSetQuestions, \n" +
            "    studentTestSetCTE.num_marked_answers AS numMarkedAnswers, \n" +
            "    studentTestSetCTE.num_correct_answers AS numCorrectAnswers, \n" +
            "    studentTestSetCTE.correct_answers AS correctAnswersStr, \n" +
            "    COALESCE(studentTestSetCTE.total_points, 0) AS totalPoints, \n" +
            "    COALESCE(handledImg.file_path, handledImg.external_link) AS handledSheetImg, \n" +
            "    handledImg.stored_type AS storedType \n" +
            "FROM studentTestSetCTE \n" +
            "    LEFT JOIN {h-schema}users ON users.id = studentTestSetCTE.student_id \n" +
            "    LEFT JOIN {h-schema}file_attach AS handledImg ON handledImg.id = studentTestSetCTE.handed_img_id AND handledImg.type = 1 \n" +
            "ORDER BY users.first_name, users.last_name ";

    public static final String UPDATE_IS_PUBLISHED_STATUS =
        "UPDATE {h-schema}student_test_set SET is_published = :isPublished WHERE exam_class_id = :examClassId AND test_type = 1";

    public static final String DELETE_BY_EXAM_CLASS_ID_AND_STATUS =
        "DELETE FROM {h-schema}student_test_set WHERE exam_class_id = :examClassId AND status = :status";

    public static final String GET_LIST_ONLINE_STUDENT_TEST_SET =
        "SELECT \n" +
            "    stdTestSet.id AS studentTestSetId,   \n" +
            "    stdTestSet.status AS status, \n" +
            "    CASE \n" +
            "       WHEN stdTestSet.status = 0 THEN 'OPEN' \n" +
            "       WHEN stdTestSet.status = 1 THEN 'IN_PROGRESS' \n" +
            "       WHEN stdTestSet.status = 2 THEN 'SUBMITTED' \n" +
            "       WHEN stdTestSet.status = 3 THEN 'DUE' \n" +
            "    END AS statusTag, \n" +
            "    stdTestSet.started_time AS startedTime, \n" +
            "    stdTestSet.allowed_start_time AS allowedStartTime, \n" +
            "    stdTestSet.allowed_submit_time AS allowedSubmitTime, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    subject.code AS subjectCode, \n" +
            "    exClass.id AS examClassId, \n" +
            "    exClass.code AS examClassCode, \n" +
            "    semester.code AS semester, \n" +
            "    test.duration AS duration, \n" +
            "    (SELECT COUNT(1) FROM {h-schema}test_set_question WHERE test_set_id = stdTestSet.test_set_id) numTestSetQuestions \n" +
            "FROM {h-schema}student_test_set AS stdTestSet \n" +
            "    LEFT JOIN {h-schema}exam_class AS exClass ON stdTestSet.exam_class_id = exClass.id \n" +
            "    LEFT JOIN {h-schema}test ON exClass.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "WHERE \n" +
            "    stdTestSet.student_id = :currentUserId AND \n" +
            "    stdTestSet.test_type = 1 AND \n" +
            "    stdTestSet.is_published IS TRUE AND \n" +
            "    ('' = :keyword OR \n" +
            "        (exClass.code ILIKE ('%' || :keyword || '%')) OR \n" +
            "        (subject.code ILIKE ('%' || :keyword || '%')) OR \n" +
            "        (subject.title ILIKE ('%' || :keyword || '%')) OR \n" +
            "        (semester.code ILIKE ('%' || :keyword || '%')) OR \n" +
            "        ({h-schema}custom_unaccent(subject.title) ILIKE ('%' || :keyword || '%'))\n" +
            "    ) AND \n" +
            "    (-1 = :subjectId OR test.subject_id = :subjectId) AND \n" +
            "    (-1 = :semesterId OR exClass.semester_id = :semesterId) AND \n" +
            "    (-1 = :testType OR stdTestSet.test_type = :testType) AND \n" +
            "    (-1 = :status OR stdTestSet.status = :status) ";

    public static final String GET_LIST_CLOSED_STUDENT_TEST_SET =
        "WITH scoringDetailsCTE AS ( \n" +
            "SELECT \n" +
            "        stdTestSetDetail.student_test_set_id AS studentTestSetId, \n" +
            "        COUNT(testSetQuest.id) AS numTestSetQuestions, \n" +
            "        COUNT(1) FILTER (WHERE stdTestSetDetail.is_corrected is TRUE) AS numCorrectAnswers, \n" +
            "        COALESCE(SUM(testSetQuest.question_mark) FILTER ( WHERE stdTestSetDetail.is_corrected is TRUE), 0) AS totalPoints \n" +
            "    FROM {h-schema}view_student_test_set_detail stdTestSetDetail \n" +
            "        LEFT JOIN {h-schema}test_set_question AS testSetQuest ON testSetQuest.id = stdTestSetDetail.test_set_question_id \n" +
            "    GROUP BY stdTestSetDetail.student_test_set_id \n" +
            ")\n"+
        "SELECT \n" +
            "    stdTestSet.id AS studentTestSetId,   \n" +
            "    stdTestSet.status AS status, \n" +
            "    CASE \n" +
            "       WHEN stdTestSet.status = 0 THEN 'OPEN' \n" +
            "       WHEN stdTestSet.status = 1 THEN 'IN_PROGRESS' \n" +
            "       WHEN stdTestSet.status = 2 THEN 'SUBMITTED' \n" +
            "       WHEN stdTestSet.status = 3 THEN 'DUE' \n" +
            "    END AS statusTag, \n" +
            "    stdTestSet.started_time AS startedTime, \n" +
            "    stdTestSet.allowed_start_time AS allowedStartTime, \n" +
            "    stdTestSet.allowed_submit_time AS allowedSubmitTime, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    subject.code AS subjectCode, \n" +
            "    exClass.id AS examClassId, \n" +
            "    exClass.code AS examClassCode, \n" +
            "    semester.code AS semester, \n" +
            "    test.duration AS duration, \n" +
            "    scoringDetailsCTE.totalPoints AS totalPoints \n" +
            "FROM {h-schema}student_test_set AS stdTestSet \n" +
            "    LEFT JOIN scoringDetailsCTE ON scoringDetailsCTE.studentTestSetId = stdTestSet.id \n" +
            "    LEFT JOIN {h-schema}exam_class AS exClass ON stdTestSet.exam_class_id = exClass.id \n" +
            "    LEFT JOIN {h-schema}test ON exClass.test_id = test.id \n" +
            "    LEFT JOIN {h-schema}subject ON test.subject_id = subject.id \n" +
            "    LEFT JOIN {h-schema}semester ON test.semester_id = semester.id \n" +
            "WHERE \n" +
            "    stdTestSet.student_id = :currentUserId AND \n" +
            "    stdTestSet.test_type = 1 AND \n" +
            "    stdTestSet.is_published IS TRUE AND \n" +
            "    stdTestSet.status IN (2,3) AND \n" +
            "    ('' = :keyword OR \n" +
            "        (exClass.code ILIKE ('%' || :keyword || '%')) OR \n" +
            "        (subject.code ILIKE ('%' || :keyword || '%')) OR \n" +
            "        (subject.title ILIKE ('%' || :keyword || '%')) OR \n" +
            "        (semester.code ILIKE ('%' || :keyword || '%')) OR \n" +
            "        ({h-schema}custom_unaccent(subject.title) ILIKE ('%' || :keyword || '%'))\n" +
            "    )";

    public static final String EXISTS_BY_TEST_ID =
        "SELECT EXISTS( \n" +
            "  SELECT \n" +
            "        stdTestSet.id \n" +
            "    FROM {h-schema}student_test_set as stdTestSet \n" +
            "        JOIN {h-schema}test_set AS testSet On stdTestSet.test_set_id = testSet.id \n" +
            "        JOIN {h-schema}test ON test.id = testSet.test_id \n" +
            "    WHERE test.id = :testId \n" +
            ")";

    public static final String GET_TEMP_SUBMISSION_BY_ID =
        "SELECT TEXT(COALESCE(temp_submissions, '[]')) FROM {h-schema}student_test_set WHERE id = :id";

    public static final String GET_STUDENT_TEST_SET_RESULT_BY_ID =
        "SELECT \n" +
            "    SUM(COALESCE(testSetQuest.question_mark, 0)) FILTER (WHERE stdTestSetDetail.is_corrected is TRUE) AS total_points \n" +
            "FROM {h-schema}view_student_test_set_detail AS stdTestSetDetail \n" +
            "    JOIN {h-schema}test_set_question AS testSetQuest ON testSetQuest.id = stdTestSetDetail.test_set_question_id \n" +
            "WHERE stdTestSetDetail.student_test_set_id = :id \n" +
            "GROUP BY stdTestSetDetail.student_test_set_id ";

    public static final String UPDATE_DUE_ONLINE_TEST_BY_STUDENT_ID =
        "UPDATE {h-schema}student_test_set \n" +
            "SET status = 3, modified_at = :updatedTime \n" +
            "WHERE " +
            "   student_id = :studentId AND \n" +
            "   is_published = true AND \n" +
            "   test_type = 1 AND \n" +
            "   allowed_submit_time < :updatedTime AND \n" +
            "   status IN (0,1) ";

    public static final String UPDATE_DUE_ALL_ONLINE_TEST =
        "UPDATE {h-schema}student_test_set \n" +
            "SET status = 3, modified_at = :updatedTime \n" +
            "WHERE \n" +
            "   is_published = true AND \n" +
            "   test_type = 1 AND \n" +
            "   allowed_submit_time < :updatedTime AND \n" +
            "   status IN (0,1) ";

    public static final String EXISTS_NOT_PUBLISHED_IN_EXAM_CLASS =
        "SELECT EXISTS (SELECT * FROM {h-schema}student_test_set WHERE exam_class_id = :examClassId AND test_type = 1 AND is_published is not true)";
}
