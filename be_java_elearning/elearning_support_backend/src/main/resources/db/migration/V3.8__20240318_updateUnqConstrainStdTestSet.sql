ALTER TABLE "elearning_support_dev"."student_test_set"
DROP CONSTRAINT IF EXISTS "student_test_set_pkey";

ALTER TABLE "elearning_support_dev"."student_test_set"
ADD CONSTRAINT "studentExamClassUnqConstraint" UNIQUE ("student_id", "exam_class_id");

ALTER TABLE "elearning_support_dev"."test_question"
DROP COLUMN "question_mark";