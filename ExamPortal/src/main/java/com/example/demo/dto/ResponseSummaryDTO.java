package com.example.demo.dto;
 
import lombok.Data;
 
@Data
public class ResponseSummaryDTO {
    private Long responseId;
    private Long questionId;
    private String submittedAnswer;
    private int marksObtained;
 
    // Constructors
    public ResponseSummaryDTO(Long responseId, Long questionId, String submittedAnswer, int marksObtained) {
        this.responseId = responseId;
        this.questionId = questionId;
        this.submittedAnswer = submittedAnswer;
        this.marksObtained = marksObtained;
    }
 
    // Getters and Setters
    // (or use Lombok @Data for brevity)
}