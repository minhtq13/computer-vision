package com.elearning.elearning_support.entities.test;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test", schema = "elearning_support_dev")
public class Test extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "question_quantity")
    private Integer questionQuantity;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "total_point")
    private Integer totalPoint;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "semester_id")
    private Long semesterId;

    @Column(name = "gen_test_config")
    private String genTestConfig;

    @Column(name = "test_type", nullable = false)
    private Integer testType;

    @Column(name = "is_allowed_using_doc")
    private Boolean isAllowedUsingDocuments;

}