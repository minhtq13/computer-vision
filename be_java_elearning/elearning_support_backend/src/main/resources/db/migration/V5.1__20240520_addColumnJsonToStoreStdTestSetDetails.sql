alter table "elearning_support_dev"."student_test_set"
    add column "student_test_set_detail" jsonb default '[]';
comment on column "elearning_support_dev"."student_test_set"."student_test_set_detail" is 'Chi tiết bài làm dạng json';

alter table "elearning_support_dev"."student_test_set"
    add column "synchronized_status" int4 default 0;
comment on column "elearning_support_dev"."student_test_set"."synchronized_status"
    is 'Trạng thái đồng bộ từ cột "student_test_set_detail" -> bảng "student_test_set_detail" (0: Chưa đồng bộ, 1: Đã đồng bộ)';

drop view if exists "elearning_support_dev"."view_student_test_set_detail";
create or replace view "elearning_support_dev"."view_student_test_set_detail" as
(
select stdTestSetDetail.student_test_set_id,
       cast((stdTestSetDetail.detail_item -> 'isCorrected') as boolean)              as is_corrected,
       cast((stdTestSetDetail.detail_item -> 'testSetQuestionId') as int8)           as test_set_question_id,
       translate(text(stdTestSetDetail.detail_item -> 'selectedAnswer'), '[]', '{}') as selected_answers
from (select id as student_test_set_id, jsonb_array_elements(student_test_set_detail) as detail_item from elearning_support_dev.student_test_set) as stdTestSetDetail
);