package com.example.mockito.donothing;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceStub implements EmailService {

    private boolean emailSent;

    @Override
    public void sendEmail(String to, String subject, String body) {
        this.emailSent = true;
    }

    public boolean isEmailSent() {
        return emailSent;
    }
}
