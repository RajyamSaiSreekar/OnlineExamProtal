package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Question {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long questionId;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="exam_id")
	private Exam exam;
	
	private String questionText;
	
	private String option1;
	private String option2;
	
	
	private String correctAnswer;
	
	private String category;
	private String difficulty;
	
	private int maxMarks;
	
}
