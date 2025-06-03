package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Exam;
import com.example.demo.repository.ExamRepository;

@Service
public class ExamService {

	@Autowired
	private ExamRepository examRepository;
	
	public Exam createExam(Exam exam) {
		return examRepository.save(exam);
	}
	
	public List<Exam> getAllExams(){
		return examRepository.findAll();
	}
	
	public Exam getExamById(Long id) {
		return examRepository.findById(id).orElse(null);
	}
	
	public void deleteExam(Long examId) {
		 examRepository.deleteById(examId);
	}
	
}
