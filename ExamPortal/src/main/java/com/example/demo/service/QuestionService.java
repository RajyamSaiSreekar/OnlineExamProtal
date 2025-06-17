package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.QuestionDTO;
import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.exception.ResourceNotFoundException; // Import the custom exception


@Service
public class QuestionService {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	public Question addQuestion(Question question) {
		return questionRepository.save(question);
	}
	
	public List<Question> addMultipleQuestions(List<Question> q)
	{
		return questionRepository.saveAll(q);
	}
	
	public List<Question> getQuestionsByExam(Long examId){
		return questionRepository.findByExamExamId(examId);
	}
	
	public Question getQuestionById(Long id) {
		// Use orElseThrow to throw custom exception if not found
		return questionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
	}
	
	public void deleteQuestion(Long questionId) {
		// Check if question exists before deleting
		if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with id: " + questionId);
        }
		questionRepository.deleteById(questionId);
	}
	
	public QuestionDTO convertToDTO(Question question) {
		   return new QuestionDTO(
		       question.getQuestionId(),
		       question.getQuestionText(),
		       question.getOption1(),
		       question.getOption2(),
		       question.getCategory(),
		       question.getDifficulty(),
		       question.getMaxMarks()
		   );
	}	   
}
