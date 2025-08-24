package com.elearning.elearning_support.constants.sql;

public class SQLCronJob {

    public static final String CLEAN_UP_CRON_JOB_HISTORY =
        "delete from {h-schema}cron_job_history where date(now() - interval '3 month') >= date(ended_at)";

}
