ALTER TABLE "elearning_support_dev"."question"
ADD COLUMN IF NOT EXISTS "is_multiple_ans" BOOLEAN default true;
COMMENT ON COLUMN "elearning_support_dev"."question"."is_multiple_ans" IS 'Câu hỏi có nhiều đáp án trả lời đúng';