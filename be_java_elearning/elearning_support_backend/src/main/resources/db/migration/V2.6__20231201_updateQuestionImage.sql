-- update image_ids col question --
ALTER TABLE "elearning_support_dev"."question"
DROP COLUMN IF EXISTS "image_id";

ALTER TABLE "elearning_support_dev"."question"
ADD COLUMN "image_ids" int8[] default '{}';