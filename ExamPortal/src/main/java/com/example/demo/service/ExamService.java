package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Exam;
import com.example.demo.repository.ExamRepository;
import com.example.demo.exception.ResourceNotFoundException; // Import the custom exception

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
		// Use orElseThrow to throw custom exception if not found
		return examRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));
	}
	
	public void deleteExam(Long examId) {
		// Check if exam exists before deleting to avoid EmptyResultDataAccessException
		if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with id: " + examId);
        }
		examRepository.deleteById(examId);
	}
	
}
