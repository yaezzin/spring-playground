package com.example.mockito.donothing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EmailServiceImplTest {

    @Mock private EmailServiceStub emailServiceStub;
    @InjectMocks private EmailServiceImpl emailServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailServiceImpl.setEmailService(emailServiceStub);
    }

    @Test
    public void sendEmailTest() throws Exception {
        // EmailServiceImpl.sendEmail()을 호출합니다.
        emailServiceImpl.sendEmail("test@example.com", "Test Subject", "Test Body");

        // EmailServiceStub.sendEmail()이 한 번 호출되었는지 검증합니다.
        verify(emailServiceStub, times(1)).sendEmail(anyString(), anyString(), anyString());
    }
}



