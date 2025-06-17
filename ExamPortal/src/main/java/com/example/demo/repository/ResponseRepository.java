package com.example.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
	

	//<Response> findByUserId(Long userId);

	List<Response> findByUserId(Long userId);

	List<Response> findByUserIdAndExamExamId(Long userId, Long examId);

	List<Response> findByExamExamId(Long examId);

}
