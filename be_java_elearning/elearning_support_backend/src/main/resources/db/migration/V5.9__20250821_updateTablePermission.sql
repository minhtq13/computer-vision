ALTER TABLE "elearning_support_dev"."role"
    ADD COLUMN description text;

UPDATE "elearning_support_dev"."role" SET description = '' WHERE description IS NULL;

ALTER TABLE "elearning_support_dev"."role"
    ALTER COLUMN description SET NOT NULL;

ALTER TABLE "elearning_support_dev"."role"
    ADD COLUMN role_base_id int4;

UPDATE "elearning_support_dev"."role" SET role_base_id = -1 WHERE role_base_id IS NULL;

ALTER TABLE "elearning_support_dev"."role"
    ALTER COLUMN role_base_id SET NOT NULL;


ALTER TABLE "elearning_support_dev"."role"
ALTER COLUMN displayed_name TYPE varchar(255);

ALTER TABLE "elearning_support_dev"."role"
    ALTER COLUMN displayed_name DROP NOT NULL;


-- Set role_base_id cho các role admin custom
UPDATE "elearning_support_dev"."role"
SET role_base_id = (select id from "elearning_support_dev"."role" where code = 'ROLE_ADMIN' order by id desc limit 1)
WHERE code = 'ROLE_ADMIN_DEPARTMENT';

UPDATE "elearning_support_dev"."role"
SET role_base_id = (select id from "elearning_support_dev"."role" where code = 'ROLE_ADMIN' order by id desc limit 1)
WHERE code = 'ROLE_ADMIN_SYSTEM';

