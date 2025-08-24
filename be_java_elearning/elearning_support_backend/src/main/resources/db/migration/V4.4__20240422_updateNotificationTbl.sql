ALTER TABLE "elearning_support_dev"."notification"
    DROP COLUMN IF EXISTS "type";
-- new content_type column --
ALTER TABLE "elearning_support_dev"."notification"
ADD COLUMN "content_type" INT4;
COMMENT ON COLUMN "elearning_support_dev"."notification"."content_type" IS 'Loại nội dung thông báo';

ALTER TABLE "elearning_support_dev"."notification"
    ADD COLUMN "title" text;
COMMENT ON COLUMN "elearning_support_dev"."notification"."title" IS 'Tiêu đề thông báo';

ALTER TABLE "elearning_support_dev"."notification"
    ALTER COLUMN "created_by" DROP NOT NULL;
COMMENT ON COLUMN "elearning_support_dev"."notification"."created_by" IS 'Người thực hiện gửi thông báo (NULL = Hệ thống)';

DROP INDEX IF EXISTS "notificationUserIdIdx";
CREATE INDEX "notificationUserIdIdx" ON "elearning_support_dev"."notification" USING HASH("user_id");