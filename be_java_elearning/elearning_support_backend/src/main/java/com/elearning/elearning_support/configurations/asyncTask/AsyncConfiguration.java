package com.elearning.elearning_support.configurations.asyncTask;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class AsyncConfiguration {

    @Value(value = "${batch.async.properties.common.core_thread:10}")
    private Integer commonCoreThread;

    @Value(value = "${batch.async.properties.common.max_thread:10}")
    private Integer commonMaxThread;

    @Value(value = "${batch.async.properties.common.queue_capacity:200}")
    private Integer commonQueueCapacity;

    @Value(value = "${batch.async.properties.clean-up.core_thread:10}")
    private Integer cleanUpCoreThread;

    @Value(value = "${batch.async.properties.clean-up.max_thread:10}")
    private Integer cleanUpMaxThread;

    @Value(value = "${batch.async.properties.clean-up.queue_capacity:100}")
    private Integer cleanUpQueueCapacity;

    @Value(value = "${batch.async.properties.db-save.core_thread:10}")
    private Integer dbSaveCoreThread;

    @Value(value = "${batch.async.properties.db-save.max_thread:10}")
    private Integer dbSaveMaxThread;

    @Value(value = "${batch.async.properties.db-save.queue_capacity:500}")
    private Integer dbSaveQueueCapacity;

    @Bean(name = "commonAsyncTaskExecutor")
    public Executor getCommonAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(commonCoreThread);
        executor.setMaxPoolSize(commonMaxThread);
        executor.setQueueCapacity(commonQueueCapacity);
        executor.setThreadNamePrefix("ELS-CommonAsyncTaskExecutor");
        executor.initialize();
        return executor;
    }

    @Bean(name = "cleanUpAsyncTaskExecutor")
    public Executor getCleanUpAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cleanUpCoreThread);
        executor.setMaxPoolSize(cleanUpMaxThread);
        executor.setQueueCapacity(cleanUpQueueCapacity);
        executor.setThreadNamePrefix("ELS-CleanUpAsyncTaskExecutor");
        executor.initialize();
        return executor;
    }

    @Bean(name = "dbSaveAsyncTaskExecutor")
    public Executor getDBSaveAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(dbSaveCoreThread);
        executor.setMaxPoolSize(dbSaveMaxThread);
        executor.setQueueCapacity(dbSaveQueueCapacity);
        executor.setThreadNamePrefix("ELS-DBSaveAsyncTaskExecutor");
        executor.initialize();
        return executor;
    }

}
