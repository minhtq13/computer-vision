ALTER TABLE "elearning_support_dev"."notification"
ADD COLUMN "is_new" boolean not null default true;

-- comment on result of cron jobs --
COMMENT ON COLUMN "elearning_support_dev"."cron_job_history"."result" IS 'Kết quả chạy job (1: success, 0: failed)';