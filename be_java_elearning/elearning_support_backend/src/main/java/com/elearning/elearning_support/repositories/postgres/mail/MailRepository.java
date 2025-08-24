package com.elearning.elearning_support.repositories.postgres.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.entities.mail.Mail;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {

    Boolean existsByCode(String code);

}
