package com.onlineexam.admin.controller;

import com.onlineexam.admin.dto.ExamDTO;
import com.onlineexam.admin.dto.ExamResponseDTO;
import com.onlineexam.admin.dto.IdResponseDTO;
import com.onlineexam.admin.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/exams")
public class ExamController {
    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<IdResponseDTO> createExam(@Valid @RequestBody ExamDTO examDTO) {
        ExamResponseDTO createdExam = examService.createExam(examDTO);
        return new ResponseEntity<>(new IdResponseDTO(createdExam.getExamId(), "Exam created successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExamResponseDTO>> getAllExams() {
        List<ExamResponseDTO> exams = examService.getAllExams();
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> getExamById(@PathVariable Integer id) {
        Optional<ExamResponseDTO> exam = examService.getExamById(id);
        return exam.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> updateExam(@PathVariable Integer id, @Valid @RequestBody ExamDTO examDetails) {
        ExamResponseDTO updatedExam = examService.updateExam(id, examDetails);
        if (updatedExam != null) {
            return new ResponseEntity<>(updatedExam, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Integer id) {
        if (examService.deleteExam(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}