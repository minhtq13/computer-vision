package com.elearning.elearning_support.services.tasks.studentTestSet;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.constants.TimeConstants;
import com.elearning.elearning_support.services.cronJob.CronJobService;
import lombok.extern.slf4j.Slf4j;

@Component("cron-task-student-test-set")
@Slf4j
public class StudentTestSetTask {

    private final String taskName = "studentTestSetTask";
    private final String studentTestSetServiceBeanName = "studentTestSetServiceImpl";

    @Autowired
    @Qualifier(value = "commonAsyncTaskExecutor")
    private Executor commonExecutor;

    @Autowired
    private CronJobService cronJobService;

    @Scheduled(fixedRate = 30 * TimeConstants.MINUTE) // scan every 30 minutes
    public void scanDueStudentTestSet() {
        final String jobName = "scanDueStudentTestSet";
        final String methodName = "scanDueStudentTestSet";
        commonExecutor.execute(
            () -> cronJobService.execute(taskName, jobName, studentTestSetServiceBeanName, methodName));
    }

}
