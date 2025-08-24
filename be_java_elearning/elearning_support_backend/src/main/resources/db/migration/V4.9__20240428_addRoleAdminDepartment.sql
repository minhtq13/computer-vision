INSERT INTO "elearning_support_dev"."role" ("id", "code", "name", "displayed_name", is_default)
VALUES
    (4, 'ROLE_ADMIN_DEPARTMENT', 'ADMIN_DEPARTMENT', 'Admin Trường/Khoa/Viện', true),
    (5, 'ROLE_ADMIN_SYSTEM', 'ADMIN_SYSTEM', 'Admin Hệ thống', true);

COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."result" IS 'Kết quả chạy cron (0: thất bại, 1: thành công)';

DROP TABLE IF EXISTS "elearning_support_dev"."student_course";

DROP TABLE IF EXISTS "elearning_support_dev"."course_file_attach";