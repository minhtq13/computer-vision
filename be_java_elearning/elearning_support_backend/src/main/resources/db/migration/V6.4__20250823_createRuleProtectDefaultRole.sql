-- Tạo rule để ngăn chặn việc xóa / cập nhật các role mặc định --
-- Trong trường hợp cần thiết thì phải alter table + disable và enable lại rule --
drop rule if exists "ruleLockDeleteDefaultRole" on "elearning_support_dev"."role";
create or replace rule "ruleLockDeleteDefaultRole" AS on delete to "elearning_support_dev"."role" where "old"."is_default" is true
    do instead nothing;
drop rule if exists "ruleLockUpdateDefaultRole" on "elearning_support_dev"."role";
create or replace rule "ruleLockUpdateDefaultRole" AS on update to "elearning_support_dev"."role" where "old"."is_default" is true
    do instead nothing;