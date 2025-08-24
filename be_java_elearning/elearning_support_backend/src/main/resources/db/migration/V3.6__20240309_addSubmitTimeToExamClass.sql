ALTER TABLE "elearning_support_dev"."exam_class"
    ADD COLUMN "submit_time" timestamp;
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."submit_time" IS 'Hạn thời gian nộp bài thi (Online)';

ALTER TABLE "elearning_support_dev"."exam_class"
    ADD COLUMN "test_type" int4 not null default 0;
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."test_type" IS 'Hình thức thi (0: Offline, 1: Online)';

ALTER TABLE "elearning_support_dev"."exam_class"
DROP COLUMN IF EXISTS "teacher_id";

-- update data in exam_class --
with exClassCTE as (
    select
        exClass.id as exam_class_id,
        test.test_type as test_type,
        exClass.examine_time as examine_time,
        test.duration as duration,
        (exClass.examine_time + test.duration * interval '1 minute') as submit_time
    from "elearning_support_dev"."exam_class" as exClass
       join "elearning_support_dev"."test" as test on exClass.test_id = test.id
)
update "elearning_support_dev"."exam_class"
set "test_type" = exClassCTE.test_type,
    "submit_time" = exClassCTE.submit_time
from exClassCTE where "exam_class"."id" = exClassCTE.exam_class_id;

-- add exam_class_id to student_test_set -> classify
ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "exam_class_id" int8;
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."test_type" IS 'Id lớp thi (exam_class) -> nhóm/thống kê kết quả thi';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "submission_note" text;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."submission_note" IS 'Ghi chú khi submit bài làm';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "status" int4 not null default 0;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."status" IS 'Trạng thái bài thi (0: Chưa làm bài(mở) - OPEN, 1: Đang làm bài - IN_PROGRESS, 2: Đã nộp bài - SUBMITTED, 3: Quá hạn - DUE)';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "final_submissions" jsonb default '[]';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."final_submissions" IS 'Dữ liệu làm bài cuối cùng (đã submit/lưu gần nhất)';

ALTER TABLE "elearning_support_dev"."student_test_set"
    ADD COLUMN "is_published" boolean not null default false;
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."is_published" IS 'Trạng thái publish bài thi (false: chưa publish, true: đã publish)';

ALTER TABLE "elearning_support_dev"."student_test_set"
    DROP COLUMN IF EXISTS "state";

-- create exam_class_id idx in student_test_set
DROP INDEX IF EXISTS "stdTestSetExamClassIdIdx";
CREATE INDEX "stdTestSetExamClassIdIdx" ON "elearning_support_dev"."student_test_set" USING hash("exam_class_id");