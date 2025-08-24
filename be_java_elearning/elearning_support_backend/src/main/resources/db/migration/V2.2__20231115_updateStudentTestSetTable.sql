-- Add a is_used to test_set --
ALTER TABLE "elearning_support_dev"."test_set"
ADD COLUMN "is_used" boolean NOT NULL DEFAULT false;
COMMENT ON COLUMN "elearning_support_dev"."test_set"."is_used" IS 'Đề thi đã được sử dụng (0: Chưa sử dụng, 1: Đã sử dụng)';

-- move question_mark to test --
ALTER TABLE "elearning_support_dev"."test_set"
ADD COLUMN "question_mark" double precision;
COMMENT ON COLUMN "elearning_support_dev"."test_set"."question_mark" IS 'Điểm các câu hỏi trong đề';