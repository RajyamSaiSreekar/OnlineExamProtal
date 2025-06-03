package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;

import com.example.demo.dto.AnswerSubmissionDTO;
import com.example.demo.dto.ExamSubmissionDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.model.Exam;
import com.example.demo.model.Question;
import com.example.demo.model.Response;
import com.example.demo.service.ExamService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.ResponseService;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
	
	@Autowired
	private ExamService examService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private ResponseService responseService;
	
	@PostMapping
	public Exam createExam(@RequestBody Exam exam) {
		return examService.createExam(exam);
	}
	
	@GetMapping
	public List<Exam> getAllExams(){
		return examService.getAllExams();	
	}
	
	@PostMapping("/{examId}/questions")
	public Question addQuestion(@PathVariable Long examId, @RequestBody Question question) {
		Exam exam = examService.getExamById(examId);
		question.setExam(exam);
		return questionService.addQuestion(question);
	}
	@PostMapping("/{examId}/mulquestions")
	public List<Question> addMultipleQuestions(@PathVariable Long examId, @RequestBody List<Question> question) {
		Exam exam = examService.getExamById(examId);
		for(Question q:question) {
		q.setExam(exam);
		}
		return questionService.addMultipleQuestions(question);
		
	}
	
	/*@GetMapping("/{examId}/questions")
	public ResponseEntity<List<QuestionDTO>> getQuestionsByExam(@PathVariable Long examId) {
	   List<Question> questions = questionService.getQuestionsByExam(examId);
	   List<QuestionDTO> dtoList = questions.stream().map(q -> convertToDTO(q))
	    .collect(Collectors.toList());
	   return ResponseEntity.ok(dtoList);
	}
	*/
	@GetMapping("/{examId}/questions")
	public ResponseEntity<List<QuestionDTO>> getQuestionsByExam(@PathVariable Long examId) {
	    List<Question> questions = questionService.getQuestionsByExam(examId);
	    List<QuestionDTO> dtoList = new ArrayList<>();

	    // Iterate over each question and convert it into a QuestionDTO
	    for (Question question : questions) {
	        QuestionDTO dto = new QuestionDTO();
	        dto.setQuestionId(question.getQuestionId());
	        dto.setQuestionText(question.getQuestionText());
	        dto.setCategory(question.getCategory());
	        dto.setDifficulty(question.getDifficulty());
	        dto.setOption1(question.getOption1());
	        dto.setOption2(question.getOption2());
	        // Map additional fields as required. For example:
	         dto.setMaxMarks(question.getMaxMarks());
	        // dto.setCorrectAnswer(question.getCorrectAnswer());
	        
	        dtoList.add(dto);
	    }
	    return ResponseEntity.ok(dtoList);
	}

	
	@PostMapping("/{examId}/submit")
	public ResponseEntity<?> submitExam(@PathVariable Long examId, @RequestBody ExamSubmissionDTO submissionDTO) {
		List<Response> savedResponses=new ArrayList<>();
		Exam exam = examService.getExamById(examId);
		for(AnswerSubmissionDTO ans : submissionDTO.getAnswers()) {
			Question question = questionService.getQuestionById(ans.getQuestionId());
			
			Response response = new Response();
			response.setUserId(submissionDTO.getUserId());
			response.setExam(exam);
			response.setQuestion(question);
			response.setSubmittedAnswer(ans.getSubmittedAnswer());
			
			int marks=question.getCorrectAnswer().equals(ans.getSubmittedAnswer())?question.getMaxMarks():0;
			response.setMarksObtained(marks);
			savedResponses.add(responseService.submitResponse(response));
		}
		return ResponseEntity.ok(savedResponses);
	}
	
	//examiner AND ADMIN
	@GetMapping("/responses/{userId}")
	public List<Response> getUserResponse(@PathVariable Long userId){
		return responseService.getResponsesByUser(userId);
	}
	
	@DeleteMapping("/{examId}")
	public String deleteExam(@PathVariable Long examId) {
		examService.deleteExam(examId);
		return "Exam deleted successfully.";
	}
	
	@DeleteMapping("{examid}/questions/{questionId}")
	public String deleteQuestion(@PathVariable Long questionId) {
		questionService.deleteQuestion(questionId);
		return "Question deleted successfully.";
	}
}
