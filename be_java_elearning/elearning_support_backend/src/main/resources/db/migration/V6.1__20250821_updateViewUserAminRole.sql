-- Cập nhật view để sử dụng 'ROLE_ADMIN' thay vì 'ROLE_SUPER_ADMIN'
DROP VIEW IF EXISTS "elearning_support_dev"."view_user_admin_role";
CREATE OR REPLACE VIEW "elearning_support_dev"."view_user_admin_role"
AS
(
SELECT userRole.user_id,
       ('{'::text || string_agg(role.id::text, ','::text)) || '}'::text   AS lst_role_id,
       ('{'::text || string_agg(role.code::text, ','::text)) || '}'::text AS lst_role_code
FROM elearning_support_dev.users_roles userRole
         join (select adminRole.user_id
               from elearning_support_dev.users_roles as adminRole
                    join elearning_support_dev.role on adminRole.role_id = role.id
               where role.code = 'ROLE_ADMIN') as userAdmin on userRole.user_id = userAdmin.user_id
         JOIN elearning_support_dev.role ON userRole.role_id = role.id
GROUP BY userRole.user_id
);