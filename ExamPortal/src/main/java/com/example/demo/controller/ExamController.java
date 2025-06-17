package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.dto.AnswerSubmissionDTO;
import com.example.demo.dto.ExamSubmissionDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.dto.ResponseSummaryDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.Exam;
import com.example.demo.model.Question;
import com.example.demo.model.Response;
import com.example.demo.service.ExamService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.ResponseService;

/**
 * REST controller for managing exams and related operations.
 * Provides end points for creating, retrieving, and deleting exams.
 * adding questions to exams, submitting exam responses, and retrieving user responses.
 */
@RestController
@RequestMapping("/api/exams")
public class ExamController {

	@Autowired
	private ExamService examService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private ResponseService responseService;

	/**
	 * Creates a new exam.
	 * @param exam Exam object to be created.
	 * @return The Exam object we created.
	 */
	@PostMapping
	public Exam createExam(@RequestBody Exam exam) {
		return examService.createExam(exam);
	}

	/**
	 * Retrieves all exams.
	 * @return A list of all Exam objects.
	 */
	@GetMapping
	public List<Exam> getAllExams() {
		return examService.getAllExams();
	}

	/**
	 * Adds a single question to an existing exam.
	 * @param examId The Id of the exam in which the questions will be added.
	 * @param question The Question object to be added.
	 * @return The added Question object.
	 */
	@PostMapping("/{examId}/questions")
	public Question addQuestion(@PathVariable Long examId, @RequestBody Question question) {
		Exam exam = examService.getExamById(examId);
		question.setExam(exam);
		return questionService.addQuestion(question);
	}

	/**
	 * Adds multiple questions to an existing exam.
	 * @param examId The ID of the exam to which the questions will be added.
	 * @param questions A list of Question objects to be added.
	 * @return A list of the added Question objects.
	 */
	@PostMapping("/{examId}/mulquestions")
	public List<Question> addMultipleQuestions(@PathVariable Long examId, @RequestBody List<Question> questions) {
		Exam exam = examService.getExamById(examId);
		for (Question q : questions) {
			q.setExam(exam);
		}
		return questionService.addMultipleQuestions(questions);

	}

	/**
	 * Retrieves all questions for a specific exam.
	 * @param examId The ID of the exam.
	 * @return A ResponseEntity containing a list of QuestionDTOs for the specified exam.
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
			dto.setMaxMarks(question.getMaxMarks());
			dtoList.add(dto);
		}
		return ResponseEntity.ok(dtoList);
	}

	/**
	 * Submits an exam with the user answers.
	 * @param examId The ID of the exam being submitted.
	 * @param submissionDTO The ExamSubmissionDTO containing the user's answers.
	 * @return A ResponseEntity containing a list of ResponseSummaryDTOs, detailing the submission outcome.
	 */
	@PostMapping("/{examId}/submit")
	public ResponseEntity<List<ResponseSummaryDTO >> submitExam(@PathVariable Long examId, @RequestBody ExamSubmissionDTO submissionDTO) {
		List<ResponseSummaryDTO> responseSummaries = new ArrayList<>();
		Exam exam = examService.getExamById(examId);

		for (AnswerSubmissionDTO ans : submissionDTO.getAnswers()) {
			Question question = questionService.getQuestionById(ans.getQuestionId());

			Response response = new Response();
			response.setUserId(submissionDTO.getUserId());
			response.setExam(exam);
			response.setQuestion(question);
			response.setSubmittedAnswer(ans.getSubmittedAnswer());

			int marks = question.getCorrectAnswer().equals(ans.getSubmittedAnswer()) ? question.getMaxMarks() : 0;
			response.setMarksObtained(marks);

			Response savedResponse = responseService.submitResponse(response);

			ResponseSummaryDTO summary = new ResponseSummaryDTO(
					savedResponse.getResponseId(),
					question.getQuestionId(),
					ans.getSubmittedAnswer(),
					marks
			);
			responseSummaries.add(summary);
		}

		return ResponseEntity.ok(responseSummaries);
	}

	/**
	 * Retrieves all responses for a given user, grouped by exam.
	 *
	 * @param userId The ID of the user.
	 * @return A ResponseEntity containing a map where the outer key is userId,
	 * the inner key is examId, and the value is a list of UserResponseDTOs.
	 */
	@GetMapping("/responses/{userId}")
	public ResponseEntity<Map<Long, Map<Long, List<UserResponseDTO>>>> getUserResponse(@PathVariable Long userId) {
		List<Response> responses = responseService.getResponsesByUser(userId);

		List<UserResponseDTO> userResponses = responses.stream()
				.map(r -> new UserResponseDTO(
						r.getUserId(),
						r.getQuestion().getQuestionId(),
						r.getSubmittedAnswer(),
						r.getMarksObtained(),
						r.getExam().getExamId(),
						r.getResponseId()
				))
				.collect(Collectors.toList());

		// Group by userId -> examId
		Map<Long, Map<Long, List<UserResponseDTO>>> grouped = userResponses.stream()
				.collect(Collectors.groupingBy(
						UserResponseDTO::getUserId,
						Collectors.groupingBy(UserResponseDTO::getExamId)
				));

		return ResponseEntity.ok(grouped);
	}
	
	@GetMapping("/exam/{examId}")
    public ResponseEntity<List<Response>> getResponsesByExamId(
            @PathVariable Long examId) {

        // The service layer will throw ResourceNotFoundException if examId is invalid.
        // The GlobalExceptionHandler will catch it and return a 404.
        List<Response> responses = responseService.getResponsesByExam(examId);

        // If the service returns an empty list (meaning no responses for a valid exam),
        // you would typically return 200 OK with an empty array.
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


	/**
	 * Retrieves responses of a specific user for a particular exam.
	 * @param examId The ID of the exam.
	 * @param userId The ID of the user.
	 * @return A ResponseEntity containing a list of UserResponseDTOs for the specified user and exam.
	 */
	@GetMapping("/{examId}/responses/{userId}")
	public ResponseEntity<List<UserResponseDTO>> getUserResponsesForExam(
			@PathVariable Long examId,
			@PathVariable Long userId) {
		List<Response> responses = responseService.getResponsesByUserAndExam(userId, examId);

		List<UserResponseDTO> userResponseDTOs = responses.stream()
				.map(r -> new UserResponseDTO(
						r.getUserId(),
						r.getQuestion().getQuestionId(),
						r.getSubmittedAnswer(),
						r.getMarksObtained(),
						r.getExam().getExamId(),
						r.getResponseId()
				))
				.collect(Collectors.toList());

		return ResponseEntity.ok(userResponseDTOs);
	}

	/**
	 * Deletes an exam by its ID.
	 * @param examId The ID of the exam to be deleted.
	 * @return A String message indicating the success of the deletion.
	 */
	@DeleteMapping("/{examId}")
	public String deleteExam(@PathVariable Long examId) {
		examService.deleteExam(examId);
		return "Exam deleted successfully.";
	}

	/**
	 * Deletes a question by its ID.
	 * @param questionId The ID of the question to be deleted.
	 * @return A String message indicating the success of the deletion.
	 */
	@DeleteMapping("{examid}/questions/{questionId}")
	public String deleteQuestion(@PathVariable Long questionId) {
		questionService.deleteQuestion(questionId);
		return "Question deleted successfully.";
	}
}