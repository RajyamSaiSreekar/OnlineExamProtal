package com.example.demo.dto;
	 
	//import lombok.AllArgsConstructor;
import lombok.Data;
	//import lombok.NoArgsConstructor;
	 
	@Data
	//@NoArgsConstructor
	//@AllArgsConstructor
	public class UserResponseDTO {
	    private Long responseId;
	    private Long questionId;
	    private String submittedAnswer;
	    private int marksObtained;
	    private Long examId;
	    private Long userId;
	 
	    public UserResponseDTO(Long userId, Long questionId, String submittedAnswer, int marksObtained, Long examId,Long responseId) {
	        this.responseId = responseId;
	        this.questionId = questionId;
	        this.submittedAnswer = submittedAnswer;
	        this.marksObtained = marksObtained;
	        this.examId = examId;
	        this.userId=userId;
	        }
	   /* public UserResponseDTO( Long questionId, String submittedAnswer, int marksObtained) {
	       // this.responseId = responseId;
	        this.questionId = questionId;
	        this.submittedAnswer = submittedAnswer;
	        this.marksObtained = marksObtained;
	       // this.examId = examId;
	       // this.userId=userId;
	        }*/
	 
	    // Getters and Setters
	}

