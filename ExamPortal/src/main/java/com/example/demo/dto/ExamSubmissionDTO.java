package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class ExamSubmissionDTO {
	private Long userId;
	//private Long examId;
	private List<AnswerSubmissionDTO> answers;
}
