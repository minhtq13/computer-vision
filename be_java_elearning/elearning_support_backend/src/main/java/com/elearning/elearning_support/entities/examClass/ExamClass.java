package com.elearning.elearning_support.entities.examClass;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.entities.BaseEntity;
import com.elearning.elearning_support.entities.test.Test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exam_class", schema = "elearning_support_dev")
public class ExamClass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 10)
    @NotNull
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Size(max = 255)
    @Column(name = "room_name")
    private String roomName;

    @Column(name = "examine_time")
    private Date examineTime;

    @Column(name = "semester_id")
    private Long semesterId;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "test_id")
    private Long testId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "test_type", nullable = false)
    private Integer testType;

    @Column(name = "submit_time")
    private Date submitTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id", insertable = false, updatable = false)
    private Test test;

}