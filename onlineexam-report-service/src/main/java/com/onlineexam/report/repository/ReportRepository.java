package com.onlineexam.report.repository;

import com.onlineexam.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

	@Query("SELECT r FROM Report r WHERE r.totalMarks = (SELECT MAX(r2.totalMarks) FROM Report r2)")
	List<Report> findTopperByTotalMarks();

	List<Report> findByExamExamId(Integer examId);

	List<Report> findByUserUserId(Integer userId);

	Optional<Report> findByUserUserIdAndExamExamId(Integer userId, Integer examId);
}
