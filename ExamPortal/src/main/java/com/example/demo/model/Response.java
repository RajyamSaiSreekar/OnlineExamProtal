package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Response {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long responseId;
	//private Long questionId;
	private Long userId;
	
	@ManyToOne
	@JoinColumn(name="exam_id")
	private Exam exam;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="question_id")
	private Question question;
	
	private String submittedAnswer;
	private Integer marksObtained;
}
