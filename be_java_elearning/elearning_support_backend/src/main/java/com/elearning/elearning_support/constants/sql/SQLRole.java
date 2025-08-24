package com.elearning.elearning_support.constants.sql;

public class SQLRole {
        public static final String GET_ALL_ROLES = "SELECT r.id, r.code, r.name, r.description, r.is_default as \"isDefault\", r.role_base_id as \"roleBaseId\",  r.displayed_name as \"displayedName\", "
                        + "CAST(COUNT(DISTINCT ur.user_id) AS BIGINT) as \"userCount\", "
                        + "COALESCE("
                        + " (SELECT CAST(json_agg(json_build_object('id', p.id, 'code', p.code, 'name', p.name, 'description', p.description)) AS TEXT) FROM roles_permissions rp JOIN permission p ON rp.permission_id = p.id WHERE rp.role_id = r.id), '[]'"
                        + ") as permissionInfo "
                        + "FROM role r LEFT JOIN users_roles ur ON r.id = ur.role_id "
                        + "WHERE (:type = 'ALL' OR (:type = 'BASE' AND r.role_base_id = -1) OR (:type = 'CUSTOM' AND r.role_base_id <> -1))"
                        + "GROUP BY r.id "
                        + "ORDER BY r.id ASC";

}
