package com.elearning.elearning_support.entities.mail;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.utils.converter.ListStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mail_templates", schema = "elearning_support_dev")
public class MailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 30)
    @NotNull
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @NotNull
    @Column(name = "raw_content", nullable = false)
    private String rawContent;

    @NotNull
    @Column(name = "lst_mail_params", nullable = false)
    @Convert(converter = ListStringConverter.class)
    private List<String> lstMailParams;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

}