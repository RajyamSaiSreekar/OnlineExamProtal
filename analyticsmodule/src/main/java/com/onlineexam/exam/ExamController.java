package com.onlineexam.exam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exams")
public class ExamController {

    @GetMapping("/{examId}/questions")
    public ResponseEntity<String> getQuestions(@PathVariable Long examId) {
        return ResponseEntity.ok("Fetch questions - Dummy");
    }

    @PostMapping("/{examId}/responses")
    public ResponseEntity<String> submitResponses(@PathVariable Long examId) {
        return ResponseEntity.ok("Submit responses - Dummy");
    }

    @GetMapping("/{examId}/results/{userId}")
    public ResponseEntity<String> getResult(@PathVariable Long examId, @PathVariable Long userId) {
        return ResponseEntity.ok("Get results - Dummy");
    }
}
