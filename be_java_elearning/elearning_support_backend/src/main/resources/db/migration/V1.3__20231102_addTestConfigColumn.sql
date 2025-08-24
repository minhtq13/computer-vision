ALTER TABLE "elearning_support_dev"."test"
ADD COLUMN IF NOT EXISTS "gen_test_config" text default
    '{
    "numTotalQuestion": 60,
    "numEasyQuestion": 20,
    "numMediumQuestion": 20,
    "numHardQuestion": 20,
    }';

ALTER TABLE "elearning_support_dev"."test"
    ADD COLUMN IF NOT EXISTS "description" text default '';