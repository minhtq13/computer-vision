ALTER TABLE "elearning_support_dev"."notification"
    ADD COLUMN "object_identifier" VARCHAR(255);
COMMENT ON COLUMN "elearning_support_dev"."notification"."object_identifier" IS 'Thông tin định danh đối tượng của thông báo (id, code,...)';

ALTER TABLE "elearning_support_dev"."notification"
    ADD COLUMN "object_type" INT4;
COMMENT ON COLUMN "elearning_support_dev"."notification"."object_type" IS 'Loại đối tượng của thông báo (định nghĩa trong NotificationObjectTypeEnum)';

