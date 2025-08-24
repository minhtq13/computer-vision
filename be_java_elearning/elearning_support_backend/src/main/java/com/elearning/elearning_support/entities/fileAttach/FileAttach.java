package com.elearning.elearning_support.entities.fileAttach;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file_attach", schema = "elearning_support_dev")
public class FileAttach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "type", nullable = false)
    private Integer type;

    @Size(max = 10)
    @NotNull
    @Column(name = "file_ext", nullable = false, length = 10)
    private String extension;

    @Column(name = "size")
    private Long size;

    @NotNull
    @Column(name = "stored_type", nullable = false)
    private Integer storedType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "external_link")
    private String externalLink;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}