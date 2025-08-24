DROP TABLE IF EXISTS "elearning_support_dev"."teacher_subject";
CREATE TABLE "elearning_support_dev"."teacher_subject"
(
    "id"         bigserial not null unique,
    "teacher_id"    int8   not null,
    "subject_id" int8      not null,
    "role_type" int2       not null,
    primary key ("teacher_id", "subject_id", "role_type")
);
COMMENT ON TABLE "elearning_support_dev"."teacher_subject" IS 'Bảng lưu quan hệ giữa GV và Môn học/Học phần';
COMMENT ON COLUMN "elearning_support_dev"."teacher_subject"."id" IS 'Id bản ghi';
COMMENT ON COLUMN "elearning_support_dev"."teacher_subject"."teacher_id" IS 'Id GV (user_id)';
COMMENT ON COLUMN "elearning_support_dev"."teacher_subject"."subject_id" IS 'Id môn học/học phần';
COMMENT ON COLUMN "elearning_support_dev"."teacher_subject"."role_type" IS 'Vai trò trong môn học/học phần 0: Giảng viên giảng dạy, 1: Trưởng nhóm môn học';

-- Update and columns student_test_set --
ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "allowed_start_time" timestamp;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."allowed_start_time" IS 'Thời gian bắt đầu làm bài cho phép';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "allowed_submit_time" timestamp;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."allowed_start_time" IS 'Hạn nộp bài';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "started_time" timestamp;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."started_time" IS 'Thời gian bắt đầu làm bài';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "submitted_time" timestamp;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."submitted_time" IS 'Thời gian nộp bài';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "is_submitted" boolean not null default false;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."is_submitted" IS 'Trạng thái nộp bài thi (true: đã nộp, false: chưa nộp) đối với các bài thi online';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "temp_submissions" jsonb default '[]';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."temp_submissions" IS 'Dữ liệu làm bài tạm thời (Thi Online) cập nhật sau một khoảng thời gian nhất định';

-- add column test_type to test --
ALTER TABLE "elearning_support_dev"."test"
    ADD COLUMN "test_type" int4 not null default 0;
COMMENT ON COLUMN "elearning_support_dev"."test"."test_type" IS 'Hình thức thi (0: Offline, 1: Online)';
