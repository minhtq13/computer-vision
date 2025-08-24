package com.elearning.elearning_support.services.cronJob.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import com.elearning.elearning_support.constants.CronJobConstants;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.entities.cronJob.CronJobHistory;
import com.elearning.elearning_support.enums.crobJob.CronJobResultEnum;
import com.elearning.elearning_support.repositories.postgres.cronJob.CronJobHistoryRepository;
import com.elearning.elearning_support.services.cronJob.CronJobService;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.springCustom.SpringContextUtils;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronJobServiceImpl implements CronJobService {

    private final CronJobHistoryRepository cronJobHistoryRepository;

    @Override
    public void execute(String taskName, String jobName, String beanName, String methodName, Object... args) {
        long startInMillis = System.currentTimeMillis();
        CronJobHistory jobHistory = CronJobHistory.builder()
            .startedAt(DateUtils.getCurrentDateTime())
            .jobName(jobName)
            .beanName(taskName)
            .build();
        log.info("===== STARTED EXECUTING JOB {}() IN {} =====", jobName, taskName);
        try {
            // Invoke task method
            Object beanObject = SpringContextUtils.getBean(beanName);
            Method executedMethod;
            if (Objects.isEmpty(args)) {
                executedMethod = beanObject.getClass().getDeclaredMethod(methodName);
            } else {
                Class<?>[] parameterTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
                executedMethod = beanObject.getClass().getDeclaredMethod(methodName, parameterTypes);
            }
            ReflectionUtils.makeAccessible(executedMethod);
            if (Objects.isEmpty(args)) {
                executedMethod.invoke(beanObject);
            } else {
                executedMethod.invoke(beanObject, args);
            }
            // onSuccess
            jobHistory.setResult(CronJobResultEnum.SUCCESS.getType());
            jobHistory.setMessage(CronJobConstants.SUCCESS_MESSAGE);
        } catch (Exception e) {
            // onFailure
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
            jobHistory.setResult(CronJobResultEnum.FAIL.getType());
            jobHistory.setMessage(e.getMessage());
            jobHistory.setStackTrace(Arrays.toString(e.getStackTrace()));
        }
        jobHistory.setEndedAt(DateUtils.getCurrentDateTime());
        jobHistory.setExecutionTime(System.currentTimeMillis() - startInMillis);
        cronJobHistoryRepository.save(jobHistory);
        log.info("===== ENDED EXECUTING JOB {}() IN {} AFTER {} =====", jobName, taskName, System.currentTimeMillis() - startInMillis);
    }

    @Override
    public void cleanUpCronJobHistory() {
        cronJobHistoryRepository.cleanUpCronJobHistory();
    }
}
