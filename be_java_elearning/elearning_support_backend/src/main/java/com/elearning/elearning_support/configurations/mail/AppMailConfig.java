package com.elearning.elearning_support.configurations.mail;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AppMailConfig {

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String authProperty;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String startTLSPropertyEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String startTLSPropertyRequired;



    @Bean
    public JavaMailSender mailSenderConfig(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setProtocol(protocol);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", authProperty);
        properties.put("mail.smtp.starttls.enable", startTLSPropertyEnable);
        properties.put("mail.smtp.starttls.required", startTLSPropertyRequired);
        return mailSender;
    }

}
