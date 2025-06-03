package com.example.demo.service;

import java.util.List;
//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.demo.model.Question;
import com.example.demo.model.Response;
//import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.ResponseRepository;

@Service
public class ResponseService {
	
	@Autowired
	private ResponseRepository responseRepository;
	
	//@Autowired
	//private QuestionRepository questionRepository;
	
	
	public Response submitResponse(Response response) {
		/*Question question=questionRepository.findById(response.getQuestionId())
				.orElseThrow(() -> new RuntimeException("Question not Found"));
		
		if(question.getCorrectAnswer().equals(response.getSubmittedAnswer())) {
			response.setMarksObtained(question.getMaxMarks());		
		}
		else {
			response.setMarksObtained(0);
		}*/
		return responseRepository.save(response);
	}
	
	public List<Response> getResponsesByUser(Long userId){
		return responseRepository.findByUserId(userId);
	}
}
