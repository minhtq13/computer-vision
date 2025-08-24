package com.elearning.elearning_support.entities.cronJob;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cron_job_history")
public class CronJobHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "started_at")
    private Date startedAt;

    @Column(name = "ended_at")
    private Date endedAt;

    @Column(name = "execution_time")
    private Long executionTime;

    @Column(name = "result")
    private Integer result;

    @Column(name = "message")
    private String message;

    @Column(name = "stack_trace")
    private String stackTrace;

}
