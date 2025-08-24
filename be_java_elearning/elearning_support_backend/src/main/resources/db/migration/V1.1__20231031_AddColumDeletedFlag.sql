ALTER TABLE "elearning_support_dev"."users"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."users"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

ALTER TABLE "elearning_support_dev"."exam_class"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

ALTER TABLE "elearning_support_dev"."department"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."department"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

ALTER TABLE "elearning_support_dev"."question"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."question"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

ALTER TABLE "elearning_support_dev"."chapter"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."chapter"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

ALTER TABLE "elearning_support_dev"."subject"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."subject"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

ALTER TABLE "elearning_support_dev"."test"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."test"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

ALTER TABLE "elearning_support_dev"."test_set"
    ADD COLUMN IF NOT EXISTS "deleted_flag" int2 default 1;
COMMENT ON COLUMN "elearning_support_dev"."test_set"."deleted_flag" IS 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';

-- Add column department_id to subject
ALTER TABLE "elearning_support_dev"."subject"
ADD COLUMN IF NOT EXISTS "department_id" int8;

