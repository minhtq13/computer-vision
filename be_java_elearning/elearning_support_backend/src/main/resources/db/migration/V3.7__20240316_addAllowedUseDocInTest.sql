ALTER TABLE "elearning_support_dev"."test"
    ADD COLUMN "is_allowed_using_doc" boolean default false;
COMMENT ON COLUMN "elearning_support_dev"."test"."is_allowed_using_doc" IS 'Cho phép dùng tài liệu hay không? (true:có, false: không)';