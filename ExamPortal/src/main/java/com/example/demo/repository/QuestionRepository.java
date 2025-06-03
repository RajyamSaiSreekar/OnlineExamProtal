package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Question;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	List<Question> findByExamExamId(Long examId);
}
