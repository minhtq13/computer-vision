package com.elearning.elearning_support.services.tasks.semester;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.constants.TimeConstants;
import com.elearning.elearning_support.services.cronJob.CronJobService;
import lombok.extern.slf4j.Slf4j;

@Component("cron-task-semester")
@Slf4j
public class SemesterTask {

    private final String taskName = "semesterTask";
    private final String semesterServiceBeanName = "semesterServiceImpl";

    @Autowired
    private CronJobService cronJobService;

    @Autowired
    @Qualifier(value = "commonAsyncTaskExecutor")
    private Executor commonExecutor;

    @Scheduled(fixedRate = TimeConstants.MONTH) // scan every month
    @CacheEvict(value = "combo-box@semester", allEntries = true)
    public void autoGenerateSemester() {
        final String jobName = "autoGenerateSemester";
        final String methodName = "autoGenerateSemester";
        commonExecutor.execute(() -> cronJobService.execute(taskName, jobName, semesterServiceBeanName, methodName));
    }

}
