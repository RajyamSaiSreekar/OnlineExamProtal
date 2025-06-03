package com.onlineexam.analytics.controller;

import com.onlineexam.analytics.model.Response;
import com.onlineexam.analytics.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responses")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    @PostMapping
    public ResponseEntity<Response> createResponse(@RequestBody Response response) {
        Response saved = responseService.saveResponse(response);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Response>> getAllResponses() {
        return ResponseEntity.ok(responseService.getAllResponses());
    }

    @GetMapping("/user/{userId}/exam/{examId}")
    public ResponseEntity<List<Response>> getResponsesByUserAndExam(@PathVariable Long userId, @PathVariable Long examId) {
        List<Response> responses = responseService.getResponsesByUserAndExam(userId, examId);
        return ResponseEntity.ok(responses);
    }
}
