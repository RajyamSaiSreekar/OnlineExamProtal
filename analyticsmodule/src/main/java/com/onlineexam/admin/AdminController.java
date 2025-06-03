package com.onlineexam.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/exams")
    public ResponseEntity<String> createExam() {
        return ResponseEntity.ok("Create Exam - Dummy");
    }

    @PostMapping("/questions")
    public ResponseEntity<String> addQuestion() {
        return ResponseEntity.ok("Add Question - Dummy");
    }

    @PostMapping("/roles")
    public ResponseEntity<String> assignRole() {
        return ResponseEntity.ok("Assign Role - Dummy");
    }
}
