-- view user admin (SUPER_ADMIN is based role of admins) --
DROP VIEW IF EXISTS "elearning_support_dev"."view_user_admin_role";
CREATE OR REPLACE VIEW "elearning_support_dev"."view_user_admin_role" AS (
  SELECT
       userRole.user_id,
      '{' || string_agg(TEXT(role.id), ',') || '}' AS lst_role_id,
      '{' || string_agg(role.code, ',') || '}'     AS lst_role_code
    FROM elearning_support_dev.users_roles AS userRole
        JOIN elearning_support_dev.role ON userRole.role_id = role.id
    WHERE role.code LIKE '%ROLE_SUPER_ADMIN%'
    GROUP BY userRole.user_id
);