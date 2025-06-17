package com.onlineexam.questionbank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class QuestionBankController {

    @PostMapping
    public ResponseEntity<String> addQuestion() {
        return ResponseEntity.ok("Add question - Dummy");
    }

    @GetMapping("/categories")
    public ResponseEntity<String> getCategories() {
        return ResponseEntity.ok("Get categories - Dummy");
    }

    @PostMapping("/import")
    public ResponseEntity<String> importQuestions() {
        return ResponseEntity.ok("Import questions - Dummy");
    }
}
