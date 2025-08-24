-- test_set_question --
ALTER TABLE "elearning_support_dev"."test_set_question"
    DROP CONSTRAINT IF EXISTS "test_set_question_pkey";

ALTER TABLE "elearning_support_dev"."test_set_question"
    ADD PRIMARY KEY ("id");

-- student_exam_class --
ALTER TABLE "elearning_support_dev"."user_exam_class"
    DROP CONSTRAINT IF EXISTS "user_exam_class_pkey";

ALTER TABLE "elearning_support_dev"."user_exam_class"
    ADD PRIMARY KEY ("id");

ALTER TABLE "elearning_support_dev"."user_exam_class"
    DROP CONSTRAINT IF EXISTS userExamClassRoleUnqConstraint;
ALTER TABLE "elearning_support_dev"."user_exam_class"
    ADD CONSTRAINT userExamClassRoleUnqConstraint UNIQUE ("user_id", "exam_class_id", "role_type");
