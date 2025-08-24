package com.elearning.elearning_support.constants.sql;

public class SQLComboBox {

        public static final String GET_LIST_SUBJECT_COMBO_BOX = "SELECT \n" +
                        "   id AS id, \n" +
                        "   title AS name, \n" +
                        "   code AS code \n" +
                        "FROM {h-schema}subject \n" +
                        "WHERE \n" +
                        "    (-1 IN (:viewableSubjectIds) OR id IN (:viewableSubjectIds)) AND \n" +
                        "    (:subjectTitle = '' OR title ILIKE ('%' || :subjectTitle || '%')) AND \n" +
                        "    (:subjectCode = '' OR code ILIKE ('%' || :subjectCode || '%')) AND \n" +
                        "    (-1 IN (:departmentIds) OR department_id IN (:departmentIds))";

        public static final String GET_LIST_CHAPTER_COMBO_BOX = "SELECT \n" +
                        "    id AS id, \n" +
                        "    CONCAT_WS('. ', TEXT(orders), title)AS name, \n" +
                        "    code AS code \n" +
                        "FROM {h-schema}chapter \n" +
                        "WHERE \n" +
                        "    subject_id = :subjectId AND \n" +
                        "    (:chapterTitle = '' OR title ILIKE ('%' || :chapterTitle || '%')) AND \n" +
                        "    (:chapterCode = '' OR code ILIKE ('%' || :chapterCode || '%'))" +
                        "ORDER BY orders";

        public static final String GET_LIST_USER_WITH_TYPE_COMBO_BOX = "SELECT \n" +
                        "    users.id AS id, \n" +
                        "    CONCAT_WS(' ', users.last_name, users.first_name) AS name, \n" +
                        "    users.code AS code \n" +
                        "FROM {h-schema}users \n" +
                        "   JOIN {h-schema}users_roles AS usrRole ON usrRole.user_id = users.id \n" +
                        "   JOIN {h-schema}role ON usrRole.role_id = role.id \n" +
                        "WHERE \n" +
                        "    users.status = 1 AND \n" +
                        "    users.deleted_flag = 1 AND \n" +
                        "    users.user_type = :userType AND \n" +
                        "    role.code = :roleBase AND \n" +
                        "    (:name = '' OR CONCAT_WS(' ', users.last_name, users.first_name) ILIKE ('%' || :name || '%')) AND \n"
                        +
                        "    (:code = '' OR users.code ILIKE ('%' || :code || '%'))";

        public static final String GET_LIST_ROLE_WITHOUT_ADMIN = "SELECT \n" +
                        "   id AS id, \n" +
                        "   code AS code, \n" +
                        "   description AS name \n" +
                        "FROM {h-schema}role \n" +
                        "WHERE \n" +
                        "   (:userType IN (-2, -1) OR (:userType = 0 AND code ILIKE '%TEACHER%') OR (:userType = 1 AND code ILIKE '%STUDENT%')) AND \n"
                        +
                        "   ('' = :search OR description ILIKE ('%' || :search || '%') OR code ILIKE ('%' || :search || '%')) AND \n"
                        +
                        "   code <> 'ROLE_ADMIN'";

        public static final String GET_LIST_SEMESTER = "SELECT * FROM {h-schema}semester WHERE ('' = :search OR code ILIKE ('%' || :search || '%')) order by cast(code as int4)";

        public static final String GET_LIST_TEST = "SELECT id, name FROM {h-schema}test WHERE ('' = :search OR name ILIKE ('%' || :search || '%')) ";

        public static final String GET_LIST_EXAM_CLASS = "WITH viewableExClassCTE  AS ( \n" +
                        "   SELECT DISTINCT exam_class_id \n" +
                        "       FROM {h-schema}user_exam_class \n" +
                        "   WHERE :isAdmin OR user_id = :currentUserId \n" +
                        ") \n" +
                        "SELECT \n" +
                        "   exClass.id AS id, \n" +
                        "   exClass.code AS code, \n" +
                        "   subject.title AS name \n" +
                        "FROM {h-schema}exam_class AS exClass \n" +
                        "   LEFT JOIN viewableExClassCTE ON exClass.id = viewableExClassCTE.exam_class_id \n" +
                        "   JOIN {h-schema}test ON exClass.test_id = test.id \n" +
                        "   LEFT JOIN {h-schema}subject ON subject.id = test.subject_id \n" +
                        "WHERE \n" +
                        "   (:isAdmin OR viewableExClassCTE.exam_class_id IS NOT NULL) AND \n" +
                        "   (-1 = :semesterId OR exClass.semester_id = :semesterId) AND \n" +
                        "   (-1 = :subjectId OR test.subject_id = :subjectId) AND \n" +
                        "   (-1 = :testType OR exClass.test_type = :testType) AND \n" +
                        "   ('' = :search OR exClass.code = :search)";

        public static final String GET_LIST_COURSE = "SELECT \n" +
                        "   course.id AS id, \n" +
                        "   course.code AS code \n" +
                        "FROM {h-schema}course \n" +
                        "WHERE \n" +
                        "   (-1 = :semesterId OR course.semester_id = :semesterId) AND \n" +
                        "   (-1 = :subjectId OR course.subject_id = :subjectId) AND \n" +
                        "   ('' = :search OR course.code = :search)";

        public static final String GET_LIST_DEPARTMENT = "SELECT \n" +
                        "   id, name, code" +
                        "   FROM {h-schema}department \n" +
                        "   WHERE \n" +
                        "       deleted_flag = 1 AND \n" +
                        "       ('' = :search OR \n" +
                        "           code ILIKE ('%' || :search || '%') OR \n" +
                        "           {h-schema}custom_unaccent(name) ILIKE ('%' || {h-schema}custom_unaccent(:search) || '%') \n"
                        +
                        "       ) AND \n" +
                        "       (-1 IN (:viewableDepartmentIds) OR id IN (:viewableDepartmentIds))";

}
