package com.elearning.elearning_support.constants.sql;

public class SQLUser {

        public static final String GET_USER_DETAIL = "SELECT \n" +
                        "    users.id AS id,\n" +
                        "    users.code AS code, \n" +
                        "    users.last_name AS lastName, \n" +
                        "    users.first_name AS firstName, \n" +
                        "    CONCAT_WS(' ', users.last_name, users.first_name) AS name, \n" +
                        "    users.identification_number AS identificationNum,\n" +
                        "    CASE \n" +
                        "           WHEN users.identity_type = 0 THEN 'Chứng minh thư nhân dân' \n" +
                        "           WHEN users.identity_type = 1 THEN 'Căn cước công dân' \n" +
                        "           WHEN users.identity_type = 2 THEN 'Hộ chiếu' \n" +
                        "           ELSE '' \n" +
                        "     END AS identityType, \n" +
                        "     users.username as username, \n" +
                        "     users.birth_date AS birthDate, \n" +
                        "     users.phone_number AS phoneNumber, \n" +
                        "     users.address AS address, \n" +
                        "     users.email AS email, \n" +
                        "     CASE \n" +
                        "          WHEN users.gender = 0 THEN 'FEMALE' \n" +
                        "          WHEN users.gender = 1 THEN 'MALE' \n" +
                        "          ELSE 'OTHER' \n" +
                        "     END AS gender, \n" +
                        "     CASE \n" +
                        "         WHEN users.user_type = 1 THEN users.meta_data->>'courseNum' \n" +
                        "     END AS courseNum, \n" +
                        "     TEXT(COALESCE(users.meta_data, '{}')) AS metaData, \n" +
                        "     users.created_at AS createdAt, \n" +
                        "     users.modified_at AS modifiedAt, \n" +
                        "     userDepartment.lst_department_name AS departmentName, \n" +
                        "     userDepartment.lst_department_id AS lstDepartmentId, \n" +
                        "     users.user_type AS userType, \n" +
                        "     avatar.id AS avatarId, \n" +
                        "     users.fcm_token AS fcmToken, \n" +
                        "     COALESCE(avatar.file_path, avatar.external_link) AS avatarPath, \n" +
                        "     avatar.stored_type AS avatarStoredType, \n" +
                        "     {h-schema}get_user_role_json_v2(users.id) AS roleJson \n" +
                        "FROM {h-schema}users \n" +
                        "     LEFT JOIN {h-schema}view_user_department_details AS userDepartment ON users.id = userDepartment.user_id \n"
                        +
                        "     LEFT JOIN {h-schema}file_attach AS avatar ON users.avatar_id = avatar.id AND avatar.type IN (0,1) \n"
                        +
                        "WHERE \n" +
                        "     users.id = :userId AND users.deleted_flag = 1 ";

        public static final String GET_LIST_STUDENT = "SELECT \n" +
                        "     student.id AS id, \n" +
                        "     student.code AS code, \n" +
                        "     avatar.file_path AS avatarPath, \n" +
                        "     student.first_name AS firstName, \n" +
                        "     student.last_name AS lastName, \n" +
                        "     student.birth_date AS birthDate, \n" +
                        "     '2' AS roleBaseId, \n" +
                        "     student.phone_number AS phoneNumber, \n" +
                        "     CASE \n" +
                        "          WHEN student.gender = 0 THEN 'FEMALE' \n" +
                        "          WHEN student.gender = 1 THEN 'MALE' \n" +
                        "          ELSE 'OTHER' \n" +
                        "     END AS gender, \n" +
                        "     CASE \n" +
                        "          WHEN student.gender = 0 THEN 'Nữ' \n" +
                        "          WHEN student.gender = 1 THEN 'Nam' \n" +
                        "          ELSE 'Khác' \n" +
                        "     END AS genderName, \n" +
                        "     student.email AS email, \n" +
                        "     student.meta_data->>'courseNum' AS courseNum, \n" +
                        "     'Học Sinh / Sinh Viên' AS userType, \n" +
                        "     COALESCE(student.modified_at, student.created_at) AS lastModifiedAt, \n" +
                        "     userDepartment.lst_department_name AS departmentName \n" +
                        "FROM {h-schema}users AS student \n" +
                        "     JOIN {h-schema}view_user_student_role AS roleStudent ON student.id = roleStudent.user_id \n"
                        +
                        "     LEFT JOIN {h-schema}file_attach AS avatar ON student.avatar_id = avatar.id AND avatar.type = 0 \n"
                        +
                        "     LEFT JOIN {h-schema}view_user_department_details AS userDepartment ON student.id = userDepartment.user_id \n"
                        +
                        "WHERE \n" +
                        "    student.status = 1 AND \n" +
                        "    student.deleted_flag = 1 AND \n" +
                        "    student.user_type = 1 AND \n" +
                        "    ('' = :search OR CONCAT_WS(' ', student.last_name, student.first_name) ILIKE ('%' || :search || '%') OR student.code ILIKE ('%' || :search || '%')) AND \n"
                        +
                        "    (-1 IN (:courseNums) OR COALESCE(student.meta_data->>'courseNum', '-1')::::int4 IN (:courseNums)) AND \n"
                        +
                        "    (-1 = ANY(CAST(:departmentArr AS int8[])) OR CAST(userDepartment.lst_department_id AS int8[]) && CAST(:departmentArr AS int8[])) ";

        public static final String GET_LIST_TEACHER = "SELECT  \n" +
                        "   teacher.id AS id, \n" +
                        "   teacher.code AS code, \n" +
                        "   avatar.file_path AS avatarPath, \n" +
                        "   teacher.first_name AS firstName, \n" +
                        "   teacher.last_name AS lastName, \n" +
                        "   teacher.birth_date AS birthDate, \n" +
                        "   '3' AS roleBaseId, \n" +
                        "   CASE \n" +
                        "       WHEN teacher.gender = 0 THEN 'FEMALE' \n" +
                        "       WHEN teacher.gender = 1 THEN 'MALE' \n" +
                        "       ELSE 'OTHER' \n" +
                        "   END AS gender, \n" +
                        "   CASE \n" +
                        "       WHEN teacher.gender = 0 THEN 'Nữ' \n" +
                        "       WHEN teacher.gender = 1 THEN 'Nam' \n" +
                        "       ELSE 'Khác' \n" +
                        "   END AS genderName, \n" +
                        "   teacher.phone_number AS phoneNumber, \n" +
                        "   teacher.email AS email, \n" +
                        "   'Giáo Viên / Giảng Viên' AS userType, \n" +
                        "   userDepartment.lst_department_name AS departmentName, \n" +
                        "   COALESCE(teacher.modified_at, teacher.created_at) AS lastModifiedAt \n" +
                        "FROM {h-schema}users AS teacher \n" +
                        "   JOIN {h-schema}view_user_teacher_role AS roleTeacher ON teacher.id = roleTeacher.user_id \n"
                        +
                        "   LEFT JOIN {h-schema}file_attach AS avatar ON teacher.avatar_id = avatar.id AND avatar.type = 0 \n"
                        +
                        "   LEFT JOIN {h-schema}view_user_department_details AS userDepartment ON teacher.id = userDepartment.user_id \n"
                        +
                        "WHERE \n" +
                        "  teacher.status = 1 AND \n" +
                        "  teacher.deleted_flag = 1 AND \n" +
                        "  teacher.user_type = 0 AND \n" +
                        "  ('' = :search OR CONCAT_WS(' ', teacher.last_name, teacher.first_name) ILIKE ('%' || :search || '%') OR teacher.code ILIKE ('%' || :search || '%')) AND \n"
                        +
                        "  (-1 = ANY(CAST(:departmentArr AS int8[])) OR CAST(userDepartment.lst_department_id AS int8[]) && CAST(:departmentArr AS int8[])) ";

        public static final String GET_ALL_USERS = "SELECT  \n" +
                        "   u.id AS id, \n" +
                        "   u.code AS code, \n" +
                        "   COALESCE(\n" +
                        "       (\n" +
                        "           SELECT CAST(json_agg(\n" +
                        "               json_build_object(\n" +
                        "                   'id', r.id,\n" +
                        "                   'code', r.code,\n" +
                        "                   'name', r.name,\n" +
                        "                   'description', r.description,\n" +
                        "                   'isDefault', r.is_default,\n" +
                        "                   'roleBaseId', r.role_base_id,\n" +
                        "                   'displayedName', r.displayed_name,\n" +
                        "                   'permissions', COALESCE((SELECT json_agg(json_build_object('id', p.id, 'code', p.code, 'name', p.name, 'description', p.description, 'parentId', p.parent_id))\n"
                        +
                        "                       FROM {h-schema}roles_permissions rp JOIN {h-schema}permission p ON rp.permission_id = p.id WHERE rp.role_id = r.id ), CAST('[]' AS json))\n"
                        +
                        "               )\n" +
                        "           ) AS text)\n" +
                        "           FROM {h-schema}users_roles ur\n" +
                        "           JOIN {h-schema}role r ON ur.role_id = r.id\n" +
                        "           WHERE ur.user_id = u.id \n" +
                        "           AND r.role_base_id <> -1\n" +
                        "       ), '[]'\n" +
                        "   ) AS roleInfoJson, \n" +
                        "   u.first_name AS firstName, \n" +
                        "   u.last_name AS lastName, \n" +
                        "   u.birth_date AS birthDate, \n" +
                        "   u.meta_data->>'courseNum' AS courseNum, \n" +
                        "   CASE \n" +
                        "       WHEN u.gender = 0 THEN 'FEMALE' \n" +
                        "       WHEN u.gender = 1 THEN 'MALE' \n" +
                        "       ELSE 'OTHER' \n" +
                        "   END AS gender, \n" +
                        "   u.phone_number AS phoneNumber, \n" +
                        "   u.email AS email, \n" +
                        "   CASE \n" +
                        "       WHEN u.user_type = -1 THEN 1 \n" +
                        "       WHEN u.user_type = 0 THEN 3 \n" +
                        "       WHEN u.user_type = 1 THEN 2 \n" +
                        "       ELSE 0 \n" +
                        "   END AS roleBaseId, \n" +
                        "   COALESCE(u.modified_at, u.created_at) AS lastModifiedAt \n" +
                        "FROM {h-schema}users AS u \n" +
                        "WHERE \n" +
                        "  u.status = 1 AND \n" +
                        "  u.deleted_flag = 1 AND \n" +
                        "  (:roleBaseId IS NULL OR u.user_type = CASE :roleBaseId WHEN 1 THEN -1 WHEN 2 THEN 1 WHEN 3 THEN 0 END) AND \n"
                        +
                        "  ('' = :search OR CONCAT_WS(' ', u.last_name, u.first_name) ILIKE ('%' || :search || '%') OR u.code ILIKE ('%' || :search || '%') OR u.email ILIKE ('%' || :search || '%'))";

        public static final String GET_LIST_ADMIN = "SELECT \n" +
                        "   admin.id AS id, \n" +
                        "   admin.code AS code, \n" +
                        "   avatar.file_path AS avatarPath, \n" +
                        "   admin.first_name AS firstName, \n" +
                        "   admin.last_name AS lastName, \n" +
                        "   admin.birth_date AS birthDate, \n" +
                        "   '1' AS roleBaseId, \n" +
                        "   CASE \n" +
                        "       WHEN admin.gender = 0 THEN 'FEMALE' \n" +
                        "       WHEN admin.gender = 1 THEN 'MALE' \n" +
                        "       ELSE 'OTHER' \n" +
                        "   END AS gender, \n" +
                        "   admin.phone_number AS phoneNumber, \n" +
                        "   admin.email AS email, \n" +
                        "   'Admin' AS userType, \n" +
                        "   userDepartment.lst_department_name AS departmentName, \n" +
                        "   COALESCE(admin.modified_at, admin.created_at) AS lastModifiedAt \n" +
                        "FROM {h-schema}users AS admin \n" +
                        "   JOIN {h-schema}view_user_admin_role AS roleAdmin ON admin.id = roleAdmin.user_id \n" +
                        "   LEFT JOIN {h-schema}file_attach AS avatar ON admin.avatar_id = avatar.id AND avatar.type = 0 \n"
                        +
                        "   LEFT JOIN {h-schema}view_user_department_details AS userDepartment ON admin.id = userDepartment.user_id \n"
                        +
                        "WHERE \n" +
                        "  admin.status = 1 AND \n" +
                        "  admin.deleted_flag = 1 AND \n" +
                        "  admin.user_type = -1 AND \n" +
                        "  ('' = :search \n" +
                        "       OR {h-schema}custom_unaccent(CONCAT_WS(' ', admin.last_name, admin.first_name)) ILIKE ('%' || {h-schema}custom_unaccent(:search) || '%') \n"
                        +
                        "       OR admin.code ILIKE ('%' || :search || '%') \n" +
                        "       OR admin.email ILIKE ('%' || :search || '%') \n" +
                        "  ) AND \n" +
                        "  (-1 = ANY(CAST(:departmentArr AS int8[])) OR CAST(userDepartment.lst_department_id AS int8[]) && CAST(:departmentArr AS int8[])) ";

        public static final String GET_LIST_CURRENT_USERNAME = "SELECT username FROM {h-schema}users";

        public static final String GET_LIST_CURRENT_USER_EMAIL = "SELECT email FROM {h-schema}users";

        public static final String GET_LIST_CURRENT_USER_CODE_BY_USER_TYPE = "SELECT code FROM {h-schema}users WHERE user_type = :userType";

        public static final String DELETE_USER_ROLE_BY_USER_ID = "DELETE FROM {h-schema}users_roles WHERE user_id = :userId";

        public static final String GET_LIST_USER_ID_CODE_BY_CODE_AND_USER_TYPE = "SELECT id, code FROM {h-schema}users WHERE code IN (:lstCode) AND user_type = :userType AND deleted_flag = 1";

        public static final String FIND_STUDENT_ID_BY_UNIQUE_INFO = "SELECT users.id \n" +
                        "FROM {h-schema}users \n" +
                        "   JOIN {h-schema}view_user_student_role as vStudent ON vStudent.user_id = users.id \n" +
                        "WHERE \n" +
                        "   (users.email = :email) OR (users.username = :username) OR (users.code = :code AND users.user_type = 1)";

        public static final String DELETE_ALL_TEACHER_SUBJECT_BY_TEACHER_ID = "DELETE FROM {h-schema}teacher_subject WHERE teacher_id = :teacherId AND role_type = 0";

        public static final String UPDATE_USER_FCM_TOKEN = "UPDATE {h-schema}users SET fcm_token = :fcmToken, modified_at = CURRENT_TIMESTAMP WHERE id = :userId";

        public static final String GET_FCM_TOKEN_BY_USER_ID = "select fcm_token from {h-schema}users where id = :userId and deleted_flag = 1";

}
