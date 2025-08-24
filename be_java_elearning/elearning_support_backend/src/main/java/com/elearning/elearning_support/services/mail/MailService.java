package com.elearning.elearning_support.services.mail;

import org.springframework.stereotype.Service;
import com.elearning.elearning_support.entities.mail.Mail;

@Service
public interface MailService {

    void sendMail(Mail mail);

}
