package com.onlineexam.report.entity;

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
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name="exam_id",nullable=false)
    private Exam exam; // Referencing a minimal Exam entity in this service

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user; // Referencing a minimal User entity in this service

    private Integer totalMarks;
    private String performanceMetrics;
}
