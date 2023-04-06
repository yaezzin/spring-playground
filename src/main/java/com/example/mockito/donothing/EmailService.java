package com.example.mockito.donothing;

public interface EmailService {
    void sendEmail(String to, String subject, String body) throws Exception;
}
