package com.onlineexam.analytics.controller;

import com.onlineexam.analytics.model.Report;
import com.onlineexam.analytics.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestParam Long userId, @RequestParam Long examId) {
        Report report = reportService.generateReport(userId, examId);
        return ResponseEntity.ok(report);
    }


    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<Report>> getReportsByExam(@PathVariable Long examId) {
        List<Report> reports = reportService.getReportsByExamId(examId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Report>> getReportsByUser(@PathVariable Long userId) {
        List<Report> reports = reportService.getReportsByUserId(userId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }
    @GetMapping("/user/{userId}/exam/{examId}")
    public ResponseEntity<Report> getReportByUserAndExam(@PathVariable Long userId, @PathVariable Long examId) {
        return reportService.getReportByUserIdAndExamId(userId, examId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteReports(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long examId) {
        boolean deleted = reportService.deleteReports(userId, examId);
        if (deleted) {
            return ResponseEntity.ok("Reports deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    
    @GetMapping("/topper")
    public ResponseEntity<Long> getTopper() {
        return reportService.returnTopper()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    

}
