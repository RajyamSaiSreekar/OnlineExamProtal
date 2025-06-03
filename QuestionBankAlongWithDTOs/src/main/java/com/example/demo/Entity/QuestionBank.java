package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Question")
public class QuestionBank {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int questionId;
	@Column(unique=true,nullable=false)
	private String text;
	private String category;
	private String difficulty;
	private String option1;
	private String option2;
	private String correctAnswer;
	
	public QuestionBank() {
		
	}
}
