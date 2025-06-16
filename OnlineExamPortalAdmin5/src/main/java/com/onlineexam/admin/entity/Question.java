package com.onlineexam.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "category")
    private String category;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "correct_answer", nullable = false, columnDefinition = "TEXT")
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY) // Use LAZY to prevent eager fetching issues, handle in service
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;
}