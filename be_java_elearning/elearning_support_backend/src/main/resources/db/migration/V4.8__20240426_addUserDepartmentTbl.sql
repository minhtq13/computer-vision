DROP TABLE IF EXISTS "elearning_support_dev"."users_departments";
CREATE TABLE "elearning_support_dev"."users_departments" (
  id bigserial not null unique,
  user_id int8 not null,
  department_id int8 not null,
 primary key (user_id, department_id)
);
COMMENT ON TABLE "elearning_support_dev"."users_departments" IS 'Bảng mapping người dùng và Khoa/Trường thành viên/Bộ phận';
COMMENT ON COLUMN "elearning_support_dev"."users_departments"."id" IS 'Id bản ghi';
COMMENT ON COLUMN "elearning_support_dev"."users_departments"."user_id" IS 'Id người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users_departments"."department_id" IS 'Id Khoa/Trường thành viên/Bộ phận';

-- init root department --
ALTER TABLE "elearning_support_dev"."department"
ALTER COLUMN "parent_code" DROP NOT NULL;

ALTER TABLE "elearning_support_dev"."department"
DROP CONSTRAINT IF EXISTS "department_parent_id_key";


-- view users_departments --
DROP VIEW IF EXISTS "elearning_support_dev"."view_user_department_details";
CREATE OR REPLACE VIEW "elearning_support_dev"."view_user_department_details" AS
(
        select
                userDep.user_id,
                '{' || string_agg(TEXT(userDep.department_id), ',') || '}' as lst_department_id,
                string_agg(department.name, ', ') as lst_department_name
            from elearning_support_dev.users_departments as userDep
                join elearning_support_dev.department on userDep.department_id = department.id
            where department.deleted_flag = 1
            group by userDep.user_id
);

-- init department data --
INSERT INTO "elearning_support_dev"."department" ("parent_id", "code", "name", "address", "phone_number", "email", "created_by", "created_at")
VALUES
    (-1, 'ADMIN_CENTER', 'TT Quản trị hệ thống', '', '', '', -1, now()),
    (-1, 'SEEE', 'Trường Điện - Điện tử', 'Tòa C7, Số 1 Đại Cồ Việt, Bách Khoa, Hai Bà Trưng, Hà Nội', '', 'seee-office@hust.edu.vn', -1, now()),
    (-1, 'SOICT', 'Trường CNTT & Truyền Thông', 'Nhà B1, Đại học Bách khoa Hà Nội', '', 'vp@soict.hust.edu.vn', -1, now());



-- init root department --
ALTER TABLE "elearning_support_dev"."users"
    ALTER COLUMN "department_id" DROP NOT NULL;