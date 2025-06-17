package com.onlineexam.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.onlineexam")
public class AnalyticsReportingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsReportingApplication.class, args);
    }
}
