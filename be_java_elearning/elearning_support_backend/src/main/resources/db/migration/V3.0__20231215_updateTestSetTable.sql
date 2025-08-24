ALTER TABLE "elearning_support_dev"."test_set"
ADD COLUMN "total_point" int4 default 10;

ALTER TABLE "elearning_support_dev"."test_question"
ALTER COLUMN "question_mark" DROP NOT NULL;