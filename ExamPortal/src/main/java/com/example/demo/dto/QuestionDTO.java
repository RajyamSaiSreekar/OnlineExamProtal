package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

	private Long questionId;
	private String questionText;
	
	private String option1;
	private String option2;
	private String category;
	private String difficulty;
	
	private Integer maxMarks;
}
