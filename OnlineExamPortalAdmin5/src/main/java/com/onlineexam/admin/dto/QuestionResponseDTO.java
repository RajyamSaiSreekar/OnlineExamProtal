package com.onlineexam.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private Integer questionId;
    private String questionText;
    private String category;
    private String difficulty;
    private String correctAnswer;
    private Integer examId; // To link it back to the exam
    private String examTitle; // Optional: to provide more context, requires fetching exam
}		