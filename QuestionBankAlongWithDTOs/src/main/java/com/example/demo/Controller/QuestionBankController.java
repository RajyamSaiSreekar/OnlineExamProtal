package com.example.demo.Controller;

import com.example.demo.DTO.*;
import com.example.demo.Mapper.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.QuestionBank;
import com.example.demo.Service.QuestionBankService;


@RestController
@RequestMapping("/qb")
public class QuestionBankController {
	
	@Autowired
	private QuestionBankService qbService;
	
	@PostMapping("/addQuestion")
	public ResponseEntity<QuestionDTO> addQuestion(@RequestBody QuestionDTO dto) {
		QuestionBank saved=QuestionMapper.toEntity(dto);
		qbService.addQues(saved);
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping("/addMultipleQuestions")
	public ResponseEntity<Void> addMultipleQuestions(@RequestBody List<QuestionDTO> dtos) {
		List<QuestionBank> questions = dtos.stream()
			.map(QuestionMapper::toEntity)   //It is method reference
		// map(dto->QuestionMapper.toEntity(dto)) 
			.toList();
		qbService.addMulQues(questions);
		return ResponseEntity.noContent().build();
}
	
@PostMapping("/uploadFile")
public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
	try {
		qbService.saveQuestionsFromFile(file);
		return ResponseEntity.ok("File uploaded and data saved successfully.");
	} catch (Exception e) {
		return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
	}
}


	@GetMapping("/getquestion/{id}")
	public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable int id) {
		return qbService.getById(id)
			.map(QuestionMapper::toDTO)
			.map(ResponseEntity::ok)    //dto->ResponseEntity.ok(dto)
			.orElse(ResponseEntity.notFound().build());
}

	@GetMapping("/getAll")
	public List<QuestionDTO> getAllQuestions() {
		return qbService.getAll().stream()
		.map(QuestionMapper::toDTO)
		.toList();
}

	@GetMapping("/attemptQuestions")
	public List<QuestionAttemptDTO> getQuestionsForAttempt() {
		return qbService.getAll().stream()
			.map(QuestionMapper::toAttemptDTO)
			.toList();
}


	@PutMapping("/updQuestion/{id}")
	public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable int id, @RequestBody QuestionDTO dto) {
		return qbService.update(id, QuestionMapper.toEntity(dto))
		.map(QuestionMapper::toDTO) 
		.map(ResponseEntity::ok)
		.orElse(ResponseEntity.notFound().build());
}

	
	@DeleteMapping("/delQuestion/{id}")
	public ResponseEntity<Void> deleteQuestion(@PathVariable int id)
	{
		qbService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/getByCategory/{category}")
	public List<QuestionDTO> getByCategory(@PathVariable String category) {
		return qbService.getQuestionByCategory(category).stream()
				.map(QuestionMapper::toDTO)
				.toList();
}
	
	@GetMapping("/getByDifficulty/{difficulty}")
	public List<QuestionDTO> getByDifficulty(@PathVariable String difficulty) {
		return qbService.getQuestionByDifficulty(difficulty).stream()
		.map(QuestionMapper::toDTO)
		.toList();
}


}
