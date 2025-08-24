-- Thêm hoặc cập nhật hàm (function) get_user_role_json_v2
drop function if exists "elearning_support_dev"."get_user_role_json_v2"(i_user_id bigint);
CREATE OR REPLACE FUNCTION "elearning_support_dev"."get_user_role_json_v2"(i_user_id bigint)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
DECLARE
result JSON;
BEGIN
SELECT
    json_agg(
            json_build_object(
                    'id', r.id,
                    'code', r.code,
                    'displayedName', r.displayed_name,
                    'name', r.name,
                    'isDefault', r.is_default,
                    'roleBaseId', r.role_base_id,
                    'permissions', r.permissions
            )
    )
INTO result
FROM elearning_support_dev.users_roles AS userRole
         JOIN (
    SELECT
        role.id,
        role.code,
        role.displayed_name,
        role.name,
        role.is_default,
        role.role_base_id,
        COALESCE(
                (
                    SELECT json_agg(
                                   json_build_object(
                                           'id', p.id,
                                           'code', p.code,
                                           'parentId', p.parent_id,
                                           'name', p.name,
                                           'description', p.description
                                   )
                           )
                    FROM elearning_support_dev.roles_permissions rp
                             JOIN elearning_support_dev.permission p ON rp.permission_id = p.id
                    WHERE rp.role_id = role.id
                ),
                '[]'::json
        ) AS permissions
    FROM elearning_support_dev.role
) AS r ON userRole.role_id = r.id
WHERE userRole.user_id = i_user_id;

RETURN COALESCE(result, '[]'::json)::text;
END
$function$;