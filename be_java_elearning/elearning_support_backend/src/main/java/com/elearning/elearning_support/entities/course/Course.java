package com.elearning.elearning_support.entities.course;

import java.time.Instant;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import com.elearning.elearning_support.entities.BaseEntity;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course", schema = "elearning_support_dev")
@TypeDef(name = "int-array", typeClass = IntArrayType.class)
public class Course extends BaseEntity {

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

    @Column(name = "course_time")
    private Date courseTime;

    @Column(name = "course_weeks", columnDefinition = "Integer[]")
    @Type(type = "int-array")
    private Integer[] courseWeeks;

    @Column(name = "semester_id")
    private Long semesterId;

    @Column(name = "subject_id")
    private Long subjectId;

}