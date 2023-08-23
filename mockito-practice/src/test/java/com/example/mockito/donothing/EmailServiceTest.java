package com.example.mockito.donothing;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock private EmailService emailService;
    @Mock private EmailServiceImpl emailServiceImpl;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("doNothing() example")
    public void testSendEmail() throws Exception {
        // 스텁을 설정합니다.
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // 이메일을 보냅니다.
        emailService.sendEmail("test@example.com", "Subject", "Body");

        // verify를 사용해서 sendEmail() 메서드가 한 번 호출되었는지 확인합니다.
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }
}
