package com.elearning.elearning_support.services.cronJob;

import org.springframework.stereotype.Service;

@Service
public interface CronJobService {

    void execute(String taskName, String jobName, String beanName, String methodName, Object ...args);

    void cleanUpCronJobHistory();

}
