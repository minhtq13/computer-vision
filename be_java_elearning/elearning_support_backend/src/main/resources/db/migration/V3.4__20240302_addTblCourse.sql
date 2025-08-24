-- thêm field course_id vào tbl exam_class --
ALTER TABLE "elearning_support_dev"."exam_class"
    DROP COLUMN IF EXISTS "course_id";
ALTER TABLE "elearning_support_dev"."exam_class"
ADD COLUMN "course_id" int8;

-- Bảng course --
DROP TABLE IF EXISTS "elearning_support_dev"."course";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."course"
(
    "id"           bigserial   not null unique,
    "code"         varchar(10) not null unique,
    "created_at"   timestamp   not null,
    "created_by"   int8        not null,
    "is_enabled"   boolean default true,
    "modified_at"  timestamp,
    "modified_by"  int8,
    "room_name"    varchar(255),
    "course_time" timestamp,
    "course_weeks" int4[] default '{}',
    "semester_id"  int8,
    "subject_id"   int8,
    "deleted_flag" int2 default 1,
    PRIMARY KEY ("id")
);
COMMENT ON TABLE "elearning_support_dev"."course" IS 'Bảng lưu các lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course"."id" IS 'Id lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course"."code" IS 'Mã mã lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course"."is_enabled" IS 'Trạng thái hiển thị lớp học (Ẩn/Hiện)';
COMMENT ON COLUMN "elearning_support_dev"."course"."course_time" IS 'Thời gian học (thời gian diễn ra buổi học đầu tiên)';
COMMENT ON COLUMN "elearning_support_dev"."course"."course_weeks" IS 'Tuần học';
COMMENT ON COLUMN "elearning_support_dev"."course"."room_name" IS 'Tên phòng học';
COMMENT ON COLUMN "elearning_support_dev"."course"."subject_id" IS 'Id môn học';
COMMENT ON COLUMN "elearning_support_dev"."course"."semester_id" IS 'Id kỳ học';
COMMENT ON COLUMN "elearning_support_dev"."course"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."course"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."course"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."course"."modified_by" IS 'Id Người thực hiện cập nhật';
COMMENT ON COLUMN "elearning_support_dev"."course"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

-- add table user_course --
DROP TABLE IF EXISTS "elearning_support_dev"."user_course";
CREATE TABLE "elearning_support_dev"."user_course"
(
    "id"         bigserial not null unique,
    "user_id"    int8      not null,
    "course_id" int8      not null,
    "role_type" int2,
    primary key ("id")
);
COMMENT ON TABLE "elearning_support_dev"."user_course" IS 'Bảng lưu thông tin liên kết giữa Người dung (GV/SV) và Lớp học';
COMMENT ON COLUMN "elearning_support_dev"."user_course"."user_id" IS 'Id người dùng (users)';
COMMENT ON COLUMN "elearning_support_dev"."user_course"."role_type" IS 'Id lớp học (course)';
COMMENT ON COLUMN "elearning_support_dev"."user_course"."role_type" IS 'Vai trò của người dùng trong lớp học (0: Sinh viên/Học viên, 1: Giảng viên)';

-- create constraint --
ALTER TABLE "elearning_support_dev"."user_course"
    DROP CONSTRAINT IF EXISTS userCourseRoleUnqConstraint;
ALTER TABLE "elearning_support_dev"."user_course"
    ADD CONSTRAINT userCourseUnqConstraint UNIQUE ("user_id", "course_id", "role_type");