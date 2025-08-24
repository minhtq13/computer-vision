package com.elearning.elearning_support.entities.mail;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.core.io.InputStreamResource;
import com.elearning.elearning_support.entities.BaseEntity;
import com.elearning.elearning_support.utils.converter.SetStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mail")
public class Mail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @NotNull
    @Column(name = "lst_to_address")
    @Convert(converter = SetStringConverter.class)
    private Set<String> lstToAddress;

    @NotNull
    @Column(name = "from_address", nullable = false)
    private String fromAddress;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @NotNull
    @Column(name = "is_has_attachments", nullable = false)
    private Boolean isHasAttachments = false;

    @Transient
    private List<File> lstAttachedFiles;

    @NotNull
    @Column(name = "sent_time", nullable = false)
    private Date sentTime;

}