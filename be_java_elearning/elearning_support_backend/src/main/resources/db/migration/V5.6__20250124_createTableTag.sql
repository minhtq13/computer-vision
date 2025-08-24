-- create table tag --
create table if not exists "elearning_support_dev"."tags" (
      id bigserial not null primary key,
      name varchar(255) not null,
      object_applied_types text not null default '[]',
      is_enabled boolean default true,
      created_at timestamp not null,
      created_by int8 not null,
      deleted_flag int4 default 1
);
comment on table "elearning_support_dev"."tags" is 'Bảng lưu các thẻ (tag) của hệ thống';
comment on column "elearning_support_dev"."tags"."id" is 'Id của tag';
comment on column "elearning_support_dev"."tags"."name" is 'Tên của tag';
comment on column "elearning_support_dev"."tags"."object_applied_types" is 'Danh sách các loại đối tượng được phép gắn tag';
comment on column "elearning_support_dev"."tags"."is_enabled" is 'Trạng thái true = hiện, false = ẩn';
comment on column "elearning_support_dev"."tags"."created_at" is 'Thời gian tạo';
comment on column "elearning_support_dev"."tags"."created_by" is 'Id người tạo';
comment on column "elearning_support_dev"."tags"."deleted_flag" is 'Cờ kiểm tra xoá (0: Đã xoá, 1: Chưa xoá)';
-- attach tag to question --
alter table "elearning_support_dev"."question"
add column if not exists "tag_ids" int8[] default '{}';
comment on column "elearning_support_dev"."question"."tag_ids" is 'Danh sách id của tag gắn với câu hỏi';