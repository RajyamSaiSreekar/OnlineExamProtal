package com.onlineexam.analytics.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long examId;
    private Long userId;
    private Double totalMarks;
    private String performanceMetrics;

    public Report() {}

    public Report(Long examId, Long userId, Double totalMarks, String performanceMetrics) {
        this.examId = examId;
        this.userId = userId;
        this.totalMarks = totalMarks;
        this.performanceMetrics = performanceMetrics;
    }

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Double totalMarks) { this.totalMarks = totalMarks; }

    public String getPerformanceMetrics() { return performanceMetrics; }
    public void setPerformanceMetrics(String performanceMetrics) { this.performanceMetrics = performanceMetrics; }
}
