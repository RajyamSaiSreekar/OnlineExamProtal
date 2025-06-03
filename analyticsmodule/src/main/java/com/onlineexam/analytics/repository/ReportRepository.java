package com.onlineexam.analytics.repository;

import com.onlineexam.analytics.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByExamId(Long examId);
    List<Report> findByUserId(Long userId);
    Optional<Report> findByUserIdAndExamId(Long userId, Long examId);
    
    @Query("SELECT r.userId, SUM(r.totalMarks) as total FROM Report r GROUP BY r.userId ORDER BY total DESC")
    List<Object[]> findTopperByTotalMarks();


}
