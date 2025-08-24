package com.elearning.elearning_support.constants.sql;

public class SQLSubject {

    public static final String GET_LIST_SUBJECT = "SELECT \n" +
            "    subject.id AS id, \n" +
            "    subject.code AS code, \n" +
            "    subject.title AS title, \n" +
            "    subject.credit AS credit, \n" +
            "    department.name AS departmentName, \n" +
            "    coalesce(subject.modified_at, subject.created_at) as modifiedAt \n" +
            "FROM {h-schema}subject \n" +
            "    LEFT JOIN {h-schema}department ON subject.department_id = department.id \n" +
            "WHERE \n" +
            "    (-1 IN (:viewableSubjectIds) OR subject.id IN (:viewableSubjectIds)) AND \n" +
            "    subject.is_enabled = true AND \n" +
            "    subject.deleted_flag = 1 AND \n" +
            "    ('' = :search OR subject.title ILIKE ('%' || :search || '%') OR subject.code ILIKE ('%' || :search || '%')) AND \n"
            +
            "    ('' = :departmentName OR department.name ILIKE ('%' || :departmentName || '%')) AND \n" +
            "    (-1 = :departmentId OR department.id = :departmentId)";

    public static final String GET_DETAIL_SUBJECT = "SELECT \n" +
            "    subject.id AS id, \n" +
            "    subject.code AS code, \n" +
            "    subject.title AS title, \n" +
            "    subject.description AS description, \n" +
            "    subject.credit AS credit, \n" +
            "    department.id AS departmentId, \n" +
            "    department.name AS departmentName, \n" +
            "    {h-schema}get_list_chapter_subject_json(subject.id) AS lstChapterJson \n" +
            "FROM {h-schema}subject \n" +
            "    LEFT JOIN {h-schema}department ON subject.department_id = department.id \n" +
            "WHERE subject.is_enabled = true AND subject.deleted_flag = 1 AND subject.id = :subjectId";

    public static final String GET_ALL_SUBJECT_ID_CODE = "SELECT id, code FROM {h-schema}subject WHERE deleted_flag = 1";

    public static final String GET_LIST_VIEWABLE_SUBJECT_ID = "SELECT subject.id AS subject_id \n" +
            "FROM {h-schema}subject \n" +
            "   LEFT JOIN {h-schema}teacher_subject AS tchSubject ON tchSubject.subject_id = subject.id \n" +
            "WHERE \n" +
            "   (:isAdmin OR tchSubject.teacher_id = :currentUserId) AND \n" +
            "   subject.is_enabled = true AND subject.deleted_flag = 1 \n";

    public static final String GET_LIST_VIEWABLE_SUBJECT_ID_BY_EXAM_CLASS = "SELECT \n" +
            "   DISTINCT test.subject_id AS subjectId \n" +
            "FROM {h-schema}user_exam_class AS userExClass \n" +
            "   JOIN {h-schema}exam_class AS exClass ON userExClass.exam_class_id = exClass.id \n" +
            "   JOIN {h-schema}test ON test.id = exClass.test_id \n" +
            "WHERE \n" +
            "   (:isAdmin OR \n" +
            "       exClass.created_by = :currentUserId OR \n" +
            "       userExClass.user_id = :currentUserId \n" +
            "   )";

    public static final String GET_LIST_SUBJECT_ID_BY_DEPARTMENT_ID_IN = "select id from {h-schema}subject where deleted_flag = 1 and is_enabled and department_id = (:departmentIds)";

}
