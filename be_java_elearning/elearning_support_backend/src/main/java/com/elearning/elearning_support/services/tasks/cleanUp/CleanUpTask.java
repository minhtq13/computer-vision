package com.elearning.elearning_support.services.tasks.cleanUp;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.constants.TimeConstants;
import com.elearning.elearning_support.services.cronJob.CronJobService;
import lombok.extern.slf4j.Slf4j;

@Component("cron-task-cleanup")
@Slf4j
public class CleanUpTask {

    private final String taskName = "cleanUpTask";

    private final String cronJobServiceBeanName = "cronJobServiceImpl";

    @Autowired
    private CronJobService cronJobService;

    @Autowired
    @Qualifier("cleanUpAsyncTaskExecutor")
    private Executor cleanUpExecutor;

    @Scheduled(fixedRate = TimeConstants.MONTH) // clean up every month
    public void cleanUpCronJobHistory() {
        final String jobName = "cleanUpCronJobHistory";
        final String methodName = "cleanUpCronJobHistory";
        cleanUpExecutor.execute(() -> cronJobService.execute(taskName, jobName, cronJobServiceBeanName, methodName));
    }

}
