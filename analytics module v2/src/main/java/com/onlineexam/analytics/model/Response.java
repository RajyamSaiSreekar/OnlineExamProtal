package com.onlineexam.analytics.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "response")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;
    
    private Long examId;
    private Long questionId;
    private Long userId;
    private Double marks;

  

 // Parameterized constructor for easy object creation

	public Response(Long examId, Long questionId, Long userId, Double marks) {
		this.examId = examId;
		this.questionId = questionId;
		this.userId = userId;
		this.marks = marks;
	}
	
	//Default constructor
	public Response() {
		super();
	}



}
