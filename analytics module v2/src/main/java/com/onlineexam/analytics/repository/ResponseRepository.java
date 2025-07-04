package com.onlineexam.analytics.repository;

import com.onlineexam.analytics.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByUserIdAndExamId(Long userId, Long examId);
}
