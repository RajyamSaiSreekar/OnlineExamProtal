package com.example.demo.service;

import java.util.List;
//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
//import com.example.demo.model.Question;
import com.example.demo.model.Response;
import com.example.demo.repository.ExamRepository;
//import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.ResponseRepository;

@Service
public class ResponseService {
	
	@Autowired
	private ExamRepository examRepository;
	
	@Autowired
	private ResponseRepository responseRepository;
	
	//@Autowired
	//private QuestionRepository questionRepository;
	
	
	public Response submitResponse(Response response) {
		return responseRepository.save(response);
	}
	
	public List<Response> getResponsesByUser(Long userId){
		return responseRepository.findByUserId(userId);
	}
	
	 /**
     * Fetches all responses for a given exam.
     * Throws ResourceNotFoundException if the examId does not exist.
     *
     * @param examId The ID of the exam.
     * @return A list of responses for the specified exam.
     */
	public List<Response> getResponsesByExam(Long examId) {
        // --- VALIDATION: Invalid Exam ID on Fetching Responses ---
        // Check if the exam exists before attempting to fetch responses for it.
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        // Assuming ResponseRepository has findByExamExamId method
        return responseRepository.findByExamExamId(examId);
	}


	public List<Response> getResponsesByUserAndExam(Long userId, Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        // No validation for userId as per your requirement.

		return responseRepository.findByUserIdAndExamExamId(userId, examId);
	}
}
