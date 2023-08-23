package com.example.mockito.donothing;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private EmailService emailService;

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendEmail(String to, String subject, String body) throws Exception {
        emailService.sendEmail(to, subject, body);
    }
}
