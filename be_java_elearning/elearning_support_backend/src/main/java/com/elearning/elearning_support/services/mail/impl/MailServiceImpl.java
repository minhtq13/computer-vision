package com.elearning.elearning_support.services.mail.impl;

import java.io.File;
import java.util.Date;
import java.util.Random;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.entities.mail.Mail;
import com.elearning.elearning_support.enums.mail.MailSentStatusEnum;
import com.elearning.elearning_support.repositories.postgres.mail.MailRepository;
import com.elearning.elearning_support.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final MailRepository mailRepository;

    @Value("${spring.mail.username}")
    private String mailHost;

    @Value("${app.mail.mail-code-prefix}")
    private String mailCodePrefix;

    @Override
    public void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // multipart = true, encoding = utf-8
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE, "UTF-8");
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setText(mail.getContent(), true);
            mimeMessageHelper.setFrom("<" + mailHost + ">");
            mimeMessageHelper.setTo(mail.getLstToAddress().toArray(String[]::new));
            // mimeMessageHelper.setCc(mail.getLstToAddress().toArray(String[]::new));

            /* add attachments (if exists or has attachments) */
            if (ObjectUtils.isNotEmpty(mail.getLstAttachedFiles())) {
                for (File file : mail.getLstAttachedFiles()) {
                    mimeMessageHelper.addAttachment(file.getName(), new FileSystemResource(file));
                }
            }

            try {
                // send email
                mailSender.send(mimeMessageHelper.getMimeMessage());
                mail.setStatus(MailSentStatusEnum.SENT_SUCCESS.getStatus());
            } catch (MailException e) {
                log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
                mail.setStatus(MailSentStatusEnum.SENT_FAIL.getStatus());
            }

            // Lưu lịch sử gửi mail
            mail.setFromAddress(mailHost);
            mail.setSentTime(new Date());
            mail.setCode(generateMailCode());
            mailRepository.save(mail);
        } catch (Exception e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
        }
    }

    /**
     * Generate a unique mail code
     */
    private String generateMailCode() {
        Random random = new Random();
        String code = mailCodePrefix + (random.nextInt(9000000) + 1000000);
        while (mailRepository.existsByCode(code)) {
            code = mailCodePrefix + (random.nextInt(9000000) + 1000000);
        }
        return code;
    }
}
