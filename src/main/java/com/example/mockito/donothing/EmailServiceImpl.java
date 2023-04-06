package com.example.mockito.donothing;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) throws Exception {
        throw new Exception("실제 외부 통신 하는 코드들을 작성함");
    }
}
