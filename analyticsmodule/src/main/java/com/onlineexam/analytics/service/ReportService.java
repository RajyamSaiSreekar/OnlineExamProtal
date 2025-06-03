package com.onlineexam.analytics.service;

import com.onlineexam.analytics.model.Report;
import com.onlineexam.analytics.model.Response;
import com.onlineexam.analytics.repository.ReportRepository;
import com.onlineexam.analytics.repository.ResponseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
            performance = "Good";
        } else if (totalMarks > 40) {
            performance = "Average";
        } else {
            performance = "Bad";
        }

        Report report = new Report(examId, userId, totalMarks, performance);
        return reportRepository.save(report);
    }


    public List<Report> getReportsByExamId(Long examId) {
        return reportRepository.findByExamId(examId);
    }

    public List<Report> getReportsByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
    public Optional<Report> getReportByUserIdAndExamId(Long userId, Long examId) {
        return reportRepository.findByUserIdAndExamId(userId, examId);
    }
    public boolean deleteReports(Long userId, Long examId) {
        if (userId != null && examId != null) {
            Optional<Report> report = reportRepository.findByUserIdAndExamId(userId, examId);
            if (report.isPresent()) {
                reportRepository.delete(report.get());
                return true;
            }
        } else if (userId != null) {
            List<Report> reports = reportRepository.findByUserId(userId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            }
        } else if (examId != null) {
            List<Report> reports = reportRepository.findByExamId(examId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            }
        }
        return false;
    }
    
    
    
    public Optional<Long> returnTopper() {
        List<Object[]> result = reportRepository.findTopperByTotalMarks();
        if (!result.isEmpty()) {
            return Optional.of((Long) result.get(0)[0]); // userId of the topper
        }
        return Optional.empty();
    }



}
