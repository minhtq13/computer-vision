-- Thêm cột 'description'
ALTER TABLE "elearning_support_dev"."permission"
    ADD COLUMN "description" varchar(255);

-- Cập nhật các bản ghi hiện có để cột description không bị NULL
UPDATE "elearning_support_dev"."permission" SET "description" = '' WHERE description IS NULL;

-- Thêm ràng buộc NOT NULL cho cột 'description'
ALTER TABLE "elearning_support_dev"."permission"
    ALTER COLUMN "description" SET NOT NULL;

-- Xóa cột parent_code
ALTER TABLE "elearning_support_dev"."permission"
DROP COLUMN IF EXISTS "parent_code";