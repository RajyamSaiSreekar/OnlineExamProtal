package com.onlineexam.analytics.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor   //Default constructor

@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long examId;
    private Long userId;
    private Double totalMarks;
    private String performanceMetrics;
    
     
    

 // Parameterized constructor for easy object creation
    public Report(Long examId, Long userId, Double totalMarks, String performanceMetrics) {
        this.examId = examId;
        this.userId = userId;
        this.totalMarks = totalMarks;
        this.performanceMetrics = performanceMetrics;
    }
    
    
}
