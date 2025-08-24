
ALTER TABLE "elearning_support_dev"."test_set_question"
ADD COLUMN "lst_answer_json" jsonb default '[]';

-- create constraint (test_id, code) in test_set table
ALTER TABLE "elearning_support_dev"."test_set"
    DROP CONSTRAINT IF EXISTS testSetCodeUnqConstraint;
ALTER TABLE "elearning_support_dev"."test_set"
    ADD CONSTRAINT testSetCodeUnqConstraint UNIQUE ("test_id", "code");

DROP TABLE IF EXISTS "elearning_support_dev"."test_set_question_answer";
