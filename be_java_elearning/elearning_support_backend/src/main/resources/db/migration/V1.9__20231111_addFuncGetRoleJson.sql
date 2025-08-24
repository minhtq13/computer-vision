-- Func get json: test_set_question_answer
DROP FUNCTION IF EXISTS elearning_support_dev.get_user_role_json(i_user_id int8);
CREATE OR REPLACE FUNCTION elearning_support_dev.get_user_role_json(i_user_id int8)
    RETURNS TEXT
AS
$$

DECLARE
    result TEXT;

BEGIN
    SELECT
     '[' || string_agg(format('{"roleId": %s, "roleCode": %s, "displayedName": %s}',
                             to_json(role.id), to_json(role.code), to_json(role.displayed_name)), ',') || ']' AS lst_role_json
    INTO result
    FROM elearning_support_dev.users_roles AS userRole
        JOIN elearning_support_dev.role ON userRole.role_id = role.id
    WHERE userRole.user_id = i_user_id
    GROUP BY userRole.user_id;
    RETURN result;
END
$$
    LANGUAGE plpgsql;