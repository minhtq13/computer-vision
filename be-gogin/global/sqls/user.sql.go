package sqls

const SqlGetUserByUsernameAndDeletedFlagAndStatus = "select * \n" +
	"from ${schema}users \n" +
	"where \n" +
	"	username = @username and \n" +
	"	deleted_flag = @deletedFlag and \n" +
	"	status = @status \n"

const SqlGetUserDetail = "SELECT \n" +
	"\n" +
	"    users.id AS id,\n" +
	"    users.code AS code,\n" +
	"    users.last_name AS lastName,\n" +
	"    users.first_name AS firstName,\n" +
	"    CONCAT_WS(' ', users.last_name, users.first_name) AS name,\n" +
	"    users.identification_number AS identificationNum,\n" +
	"    CASE\n" +
	"        WHEN users.identity_type = 0 THEN 'Chứng minh thư nhân dân'\n" +
	"        WHEN users.identity_type = 1 THEN 'Căn cước công dân'\n" +
	"        WHEN users.identity_type = 2 THEN 'Hộ chiếu'\n" +
	"        ELSE ''\n" +
	"        END AS identityType,\n" +
	"    users.username as username,\n" +
	"    users.birth_date AS birthDate,\n" +
	"    users.phone_number AS phoneNumber,\n" +
	"    users.address AS address,\n" +
	"    users.email AS email,\n" +
	"    CASE\n" +
	"        WHEN users.gender = 0 THEN 'FEMALE'\n" +
	"        WHEN users.gender = 1 THEN 'MALE'\n" +
	"        ELSE 'OTHER'\n" +
	"        END AS gender,\n" +
	"    CASE\n" +
	"        WHEN users.user_type = 1 THEN users.meta_data->>'courseNum'\n" +
	"        END AS courseNum,\n" +
	"    TEXT(COALESCE(users.meta_data, '{}')) AS metaData,\n" +
	"    users.created_at AS createdAt,\n" +
	"    users.modified_at AS modifiedAt,\n" +
	"    userDepartment.lst_department_name AS departmentName,\n" +
	"    userDepartment.lst_department_id AS lstDepartmentId,\n" +
	"    users.user_type AS userType,\n" +
	"    avatar.id AS avatarId,\n" +
	"    users.fcm_token AS fcmToken,\n" +
	"    COALESCE(avatar.file_path, avatar.external_link) AS avatarPath,\n" +
	"    avatar.stored_type AS avatarStoredType,\n" +
	"    ${schema}get_user_role_json(users.id) AS roleJson\n" +
	"FROM ${schema}users\n" +
	"         LEFT JOIN ${schema}view_user_department_details AS userDepartment ON users.id = userDepartment.user_id\n" +
	"         LEFT JOIN ${schema}file_attach AS avatar ON users.avatar_id = avatar.id AND avatar.type IN (0,1)\n" +
	"WHERE\n" +
	"    users.id = @userId AND users.deleted_flag = 1"
