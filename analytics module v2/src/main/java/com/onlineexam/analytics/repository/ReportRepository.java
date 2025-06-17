package com.onlineexam.analytics.repository;

import com.onlineexam.analytics.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	

	/**
     * Finds all reports associated with a specific exam ID.
     *
     * @param examId the ID of the exam
     * @return a list of reports for the given exam
     */
    List<Report> findByExamId(Long examId);

    /**
     * Finds all reports associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of reports for the given user
     */
    List<Report> findByUserId(Long userId);

    /**
     * Finds a report for a specific user and exam combination.
     *
     * @param userId the ID of the user
     * @param examId the ID of the exam
     * @return an optional containing the report if found, or empty otherwise
     */
    Optional<Report> findByUserIdAndExamId(Long userId, Long examId);
    

    /**
     * Retrieves the user ID and total marks for each user, ordered by total marks in descending order.
     * Used to determine the top-performing user(s).
     *
     * @return a list of object arrays where each array contains [userId, totalMarks]
     */
    @Query("SELECT r.userId, SUM(r.totalMarks) as total FROM Report r GROUP BY r.userId ORDER BY total DESC")
    List<Object[]> findTopperByTotalMarks();


}
