DROP TABLE IF EXISTS "elearning_support_dev"."cron_job_history";
CREATE TABLE "elearning_support_dev"."cron_job_history" (
    "id" bigserial not null primary key,
    "job_name" text not null,
    "started_at" timestamp,
    "ended_at" timestamp,
    "execution_time" int8,
    "result" int4 not null default 0,
    "message" text
);
COMMENT ON TABLE "elearning_support_dev"."cron_job_history" IS 'Bảng lưu lịch sử quét cron job';
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."id" IS 'Id bản ghi lịch sử chạy job';
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."job_name" IS 'Tên của bean/method thực hiện job';
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."started_at" IS 'Thời gian bắt đầu chạy job';
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."ended_at" IS 'Thời gian dừng job';
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."execution_time" IS 'Tổng thời gian thực hiện job (ms)';
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."result" IS 'Kết quả chạy job';
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."message" IS 'Kết quả chạy job/ nội dung lỗi';