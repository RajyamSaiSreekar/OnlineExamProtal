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
    
 // Create a new report for a specific user and exam using query parameters
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestParam Long userId, @RequestParam Long examId) {
        Report report = reportService.generateReport(userId, examId);
        return ResponseEntity.ok(report);
    }
    
    
// Get all reports in the system
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }
    
    
 // Get all reports for a specific exam using a path variable
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<Report>> getReportsByExam(
        @PathVariable Long examId) {
        
        List<Report> reports = reportService.getReportsByExamId(examId);
        return ResponseEntity.ok(reports);
    }
    
    
 // Get all reports for a specific user using a path variable
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Report>> getReportsByUser(
        @PathVariable Long userId) {
        
        List<Report> reports = reportService.getReportsByUserId(userId);
        return ResponseEntity.ok(reports);
    }
    

    
 // Get a specific report for a user and exam using path variables
    @GetMapping("/user/{userId}/exam/{examId}")
    public ResponseEntity<Report> getReportByUserAndExam(
        @PathVariable Long userId,
        @PathVariable Long examId) {
        
        return reportService.getReportByUserIdAndExamId(userId, examId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    

 // This method handles DELETE requests to delete reports based on optional userId and/or examId.
    
    @DeleteMapping
    public ResponseEntity<String> deleteReports(
        @RequestParam(required = false)  Long userId,
        @RequestParam(required = false)  Long examId) {
        
        boolean deleted = reportService.deleteReports(userId, examId);
        if (deleted) {
            return ResponseEntity.ok("Reports deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
// Get the ID of the top-performing user
    @GetMapping("/topper")
    public ResponseEntity<Long> getTopper() {
        return reportService.returnTopper()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/rank")
    public int getRank(@RequestParam Long userId){
    	return reportService.returnRank(userId);
    }
    
 // Delete all reports in the system
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllReports() {
        reportService.deleteAllReports();
        return ResponseEntity.ok("All reports have been deleted successfully.");
    }

    
    

    
    /*
    @GetMapping("/export")
    public ResponseEntity<String> exportReports() {
        String csv = reportService.exportReportsAsCSV();
        return ResponseEntity.ok(csv);
    }*/
    
    @GetMapping("/export")
    public ResponseEntity<?> exportReportsToFile() {
        String result = reportService.exportReportsToFile();
        if (result.equals("No data in reports")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok("Reports exported successfully to: " + result);
    }
    
    
 

}
