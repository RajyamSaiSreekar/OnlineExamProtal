package com.onlineexam.analytics.service;

import com.onlineexam.analytics.model.Report;
import com.onlineexam.analytics.model.Response;
import com.onlineexam.analytics.repository.ReportRepository;
import com.onlineexam.analytics.repository.ResponseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service //Marks this class as a Spring service component

public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ResponseRepository responseRepository;

    public Report generateReport(Long userId, Long examId) {
        List<Response> responses = responseRepository.findByUserIdAndExamId(userId, examId);

        double totalMarks = responses.stream()
                .mapToDouble(Response::getMarks)
                .sum();

        String performance;
        if (totalMarks > 60) {
            performance = "First Class";
        } else if (totalMarks > 40) {
            performance = "Second Class";
        } else {
            performance = "Fail";
        }

        Report report = new Report(examId, userId, totalMarks, performance);
        return reportRepository.save(report);
    }


    public List<Report> getReportsByExamId(Long examId) {
        List<Report> reports = reportRepository.findByExamId(examId);
        if (reports.isEmpty()) {
            throw new IllegalArgumentException("Exam ID not found: " + examId);
        }
        return reports;
    }

    public List<Report> getReportsByUserId(Long userId) {
        List<Report> reports = reportRepository.findByUserId(userId);
        if (reports.isEmpty()) {
            throw new IllegalArgumentException("User ID not found: " + userId);
        }
        return reports;
    }

    public Optional<Report> getReportByUserIdAndExamId(Long userId, Long examId) {
        Optional<Report> report = reportRepository.findByUserIdAndExamId(userId, examId);
        if (report.isEmpty()) {
            throw new IllegalArgumentException("No report found for User ID " + userId + " and Exam ID " + examId);
        }
        return report;
    }

    
    public List<Report> getAllReports() {
    	 List<Report> reports = reportRepository.findAll();
         if (reports.isEmpty()) {
             throw new IllegalArgumentException("No reports found");
         }
         return reports;
    }
    
    
    public boolean deleteReports(Long userId, Long examId) {
        if (userId != null && examId != null) {
            Optional<Report> report = reportRepository.findByUserIdAndExamId(userId, examId);
            if (report.isPresent()) {
                reportRepository.delete(report.get());
                return true;
            } else {
                throw new IllegalArgumentException("No report found for the given userId and examId.");
            }
        } else if (userId != null) {
            List<Report> reports = reportRepository.findByUserId(userId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            } else {
                throw new IllegalArgumentException("No reports found for the given userId.");
            }
        } else if (examId != null) {
            List<Report> reports = reportRepository.findByExamId(examId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            } else {
                throw new IllegalArgumentException("No reports found for the given examId.");
            }
        } else {
            throw new IllegalArgumentException("At least one of userId or examId must be provided.");
        }
    }
    
    
    public void deleteAllReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            throw new IllegalArgumentException("No reports found to delete.");
        }
        reportRepository.deleteAll(reports);
    }


    
    
    
    public Optional<Long> returnTopper() {
        List<Object[]> result = reportRepository.findTopperByTotalMarks();
        if (!result.isEmpty()) {
            return Optional.of((Long) result.get(0)[0]); // userId of the topper
        }
        return Optional.empty();
    }
    
    public int returnRank(Long userId){
    	List<Object[]> userTotals = reportRepository.findTopperByTotalMarks();
    	if (!userTotals.isEmpty()) {
    		
    		    for (int i = 0; i < userTotals.size(); i++) {
    		        Object[] entry = userTotals.get(i);
    		        Long user = (Long) entry[0]; // userId is at index 0
    		        if (user.equals(userId)) {
    		            return i + 1; // rank is index + 1
    		        }
    		    }
    		}
    	 throw new IllegalArgumentException("No reports found for the given userId.");
    }
    /*
    public String exportReportsAsCSV() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            return "No data in reports";
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Report ID,Exam ID,User ID,Total Marks,Performance Metrics\n");
        for (Report report : reports) {
            csvBuilder.append(String.format("%d,%d,%d,%.2f,%s\n",
                report.getReportId(),
                report.getExamId(),
                report.getUserId(),
                report.getTotalMarks(),
                report.getPerformanceMetrics()));
        }
        return csvBuilder.toString();
    }*/
        
        public String exportReportsToFile() {
            List<Report> reports = reportRepository.findAll();
            if (reports.isEmpty()) {
                return "No data in reports";
            }

            String filePath = "reports_export.csv";
            try (PrintWriter writer = new PrintWriter(new File(filePath))) {
                writer.println("Report ID,Exam ID,User ID,Total Marks,Performance Metrics");
                for (Report report : reports) {
                    writer.printf("%d,%d,%d,%.2f,%s%n",
                        report.getReportId(),
                        report.getExamId(),
                        report.getUserId(),
                        report.getTotalMarks(),
                        report.getPerformanceMetrics());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Error exporting reports";
            }

            return filePath;
        }





}
