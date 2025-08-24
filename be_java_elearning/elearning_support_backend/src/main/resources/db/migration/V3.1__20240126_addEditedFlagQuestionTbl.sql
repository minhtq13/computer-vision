-- add flag to mark question is edited --
alter table "elearning_support_dev"."question"
add column "is_edited" boolean default false;