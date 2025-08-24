package com.elearning.elearning_support.repositories.postgres.cronJob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.sql.SQLCronJob;
import com.elearning.elearning_support.entities.cronJob.CronJobHistory;

@Repository
public interface CronJobHistoryRepository extends JpaRepository<CronJobHistory, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLCronJob.CLEAN_UP_CRON_JOB_HISTORY)
    void cleanUpCronJobHistory();

}
