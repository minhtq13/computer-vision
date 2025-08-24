alter table "elearning_support_dev"."mail"
    add column if not exists "created_at" timestamp;
comment on column "elearning_support_dev"."mail"."created_at" is 'Thời gian tạo';

alter table "elearning_support_dev"."mail"
    add column if not exists "created_by" int8;
comment on column "elearning_support_dev"."mail"."created_by" is 'Id tác nhân tạo (-1 hoặc null = Mail hệ thống tạo)';

alter table "elearning_support_dev"."mail"
    add column if not exists "modified_at" timestamp;
comment on column "elearning_support_dev"."mail"."modified_at" is 'Thời gian cập nhật';

alter table "elearning_support_dev"."mail"
    add column if not exists "modified_by" int8;
comment on column "elearning_support_dev"."mail"."modified_by" is 'Id tác nhân cập nhật (-1 hoặc null = Hệ thống)';