-- add refresh_token column --
ALTER TABLE "elearning_support_dev"."auth_info"
ADD COLUMN "refresh_token" varchar(100) UNIQUE NOT NULL default '';

-- add rf_token_expired_at --
ALTER TABLE "elearning_support_dev"."auth_info"
ADD COLUMN "rf_token_expired_at" timestamp;
