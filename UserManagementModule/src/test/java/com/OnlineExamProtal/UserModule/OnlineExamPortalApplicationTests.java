package com.OnlineExamProtal.UserModule;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.OnlineExamProtal.UserModule.OnlineExamPortalApplication;


@SpringBootTest(classes = OnlineExamPortalApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OnlineExamPortalApplicationTests {

    @Test
    void contextLoads() {
    }
}
