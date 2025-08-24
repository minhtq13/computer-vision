ALTER TABLE "elearning_support_dev"."question"
ADD COLUMN "previous_id" int8;
COMMENT ON COLUMN "elearning_support_dev"."question"."previous_id" IS 'Id phiên bản câu hỏi trước đó (null -> phiên bản gốc hay base)';

ALTER TABLE "elearning_support_dev"."question"
    ADD COLUMN "base_id" int8;
COMMENT ON COLUMN "elearning_support_dev"."question"."base_id" IS 'Id phiên bản câu hỏi gốc (câu hỏi gốc => base_id = null)';

ALTER TABLE "elearning_support_dev"."question"
    ADD COLUMN "is_newest" boolean default true;
COMMENT ON COLUMN "elearning_support_dev"."question"."is_newest" IS 'Cờ ghi lại phiên bản mới nhất';

