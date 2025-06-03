package com.onlineexam.analytics.model;

import jakarta.persistence.*;

@Entity
@Table(name = "response")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;
    
    private Long examId;
    private Long questionId;
    private Long userId;
    private Double marks;

    public Long getResponseId() {
		return responseId;
	}

	public Response(Long examId, Long questionId, Long userId, Double marks) {
		this.examId = examId;
		this.questionId = questionId;
		this.userId = userId;
		this.marks = marks;
	}

	public Response() {
		super();
	}

	public Long getExamId() {
		return examId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public Long getUserId() {
		return userId;
	}

	public Double getMarks() {
		return marks;
	}



}
