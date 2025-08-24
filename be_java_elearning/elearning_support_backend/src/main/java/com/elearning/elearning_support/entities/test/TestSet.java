package com.elearning.elearning_support.entities.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_set", schema = "elearning_support_dev")
public class TestSet extends BaseEntity {

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
    @Column(name = "test_no")
    private String testNo;

    @NotNull
    @Column(name = "test_id", nullable = false)
    private Long testId;

    @Column(name = "total_point")
    private Integer totalPoint;

    @Column(name = "question_mark")
    private Double questionMark;

}