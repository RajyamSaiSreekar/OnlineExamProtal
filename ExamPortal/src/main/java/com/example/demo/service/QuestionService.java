package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.QuestionDTO;
import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;

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
		return questionRepository.findById(id).orElse(null);
	}
	
	public void deleteQuestion(Long questionId) {
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
