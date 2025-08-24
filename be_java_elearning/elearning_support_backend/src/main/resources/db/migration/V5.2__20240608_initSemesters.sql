-- init first year --
insert into "elearning_support_dev"."semester" (name, code, school_year)
values ('20231', '20231', '2023-2024'),
       ('20232', '20232', '2023-2024'),
       ('20233', '20233', '2023-2024');

-- add column stacktrace for cron_job_history tbl --
alter table "elearning_support_dev"."cron_job_history" add column "stack_trace" text;