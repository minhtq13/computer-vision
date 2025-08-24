package com.elearning.elearning_support.entities.studentTest;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import com.elearning.elearning_support.dtos.test.studentTestSet.StdTestSetDetailItemDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionDataItem;
import com.elearning.elearning_support.entities.BaseEntity;
import com.elearning.elearning_support.entities.fileAttach.FileAttach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_test_set")
public class StudentTestSet extends BaseEntity {

    @Id
    @SequenceGenerator(name = "studentTestSetIdSeq", sequenceName = "student_test_set_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentTestSetIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "test_set_id", nullable = false)
    private Long testSetId;

    @Column(name = "is_enabled")
    private Boolean isEnabled = Boolean.TRUE;

    @Column(name = "marker_rate")
    private Double markerRate;

    @Column(name = "marked")
    private Integer marked;

    @Column(name = "test_date")
    private Date testDate;

    @Column(name = "test_type")
    private Integer testType;

    @Column(name = "allowed_start_time")
    private Date allowedStartTime;

    @Column(name = "allowed_submit_time")
    private Date allowedSubmitTime;

    @Column(name = "started_time")
    private Date startedTime;

    @Column(name = "submitted_time")
    private Date submittedTime;

    @Column(name = "is_submitted", nullable = false)
    private Boolean isSubmitted;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "temp_submissions", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<SubmissionDataItem> temporarySubmissions;

    @Column(name = "final_submissions", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<SubmissionDataItem> finalSubmissions;

    @Column(name = "submission_note")
    private String submissionNote;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "handled_test_file_id", referencedColumnName = "id")
    private FileAttach handedTestFile;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_test_set_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<StudentTestSetDetail> lstStudentTestSetDetail;

    @Column(name = "student_test_set_detail", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<StdTestSetDetailItemDTO> stdTestSetDetail;

    @Column(name = "synchronized_status")
    private Integer synchronizedStatus;

    @Column(name = "exam_class_id")
    private Long examClassId;

    @Transient
    private String handledFileAbsolutePath;

    public StudentTestSet(Long studentId, Long testSetId) {
        this.studentId = studentId;
        this.testSetId = testSetId;
    }
}