package com.onlineexam.admin.controller;

import com.onlineexam.admin.dto.IdResponseDTO;
import com.onlineexam.admin.dto.QuestionDTO;
import com.onlineexam.admin.dto.QuestionResponseDTO;
import com.onlineexam.admin.service.ExamService;
import com.onlineexam.admin.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService; // To check if exam exists before adding questions

    @PostMapping("/exam/{examId}")
    public ResponseEntity<IdResponseDTO> createQuestion(@PathVariable Integer examId, @Valid @RequestBody QuestionDTO questionDTO) {
        if (!examService.examExists(examId)) {
            return new ResponseEntity<>(new IdResponseDTO(null, "Exam with ID " + examId + " not found"), HttpStatus.NOT_FOUND);
        }
        QuestionResponseDTO createdQuestion = questionService.createQuestion(examId, questionDTO);
        if (createdQuestion != null) {
            return new ResponseEntity<>(new IdResponseDTO(createdQuestion.getQuestionId(), "Question created successfully"), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new IdResponseDTO(null, "Failed to create question"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {
        List<QuestionResponseDTO> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable Integer id) {
        Optional<QuestionResponseDTO> question = questionService.getQuestionById(id);
        return question.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByExamId(@PathVariable Integer examId) {
        if (!examService.examExists(examId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<QuestionResponseDTO> questions = questionService.getQuestionsByExamId(examId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(@PathVariable Integer id, @Valid @RequestBody QuestionDTO questionDetails) {
        QuestionResponseDTO updatedQuestion = questionService.updateQuestion(id, questionDetails);
        if (updatedQuestion != null) {
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id) {
        if (questionService.deleteQuestion(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}