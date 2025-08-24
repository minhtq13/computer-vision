ALTER TABLE "elearning_support_dev"."users"
    ALTER COLUMN "identification_number" DROP NOT NULL;

ALTER TABLE "elearning_support_dev"."users"
    ALTER COLUMN "identity_type" DROP NOT NULL;

ALTER TABLE "elearning_support_dev"."users"
    ALTER COLUMN "created_source" DROP NOT NULL;
