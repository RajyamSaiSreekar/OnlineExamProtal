package com.onlineexam.admin.service;

import com.onlineexam.admin.dto.QuestionDTO;
import com.onlineexam.admin.dto.QuestionResponseDTO;
import com.onlineexam.admin.entity.Exam;
import com.onlineexam.admin.entity.Question;
import com.onlineexam.admin.repository.ExamRepository;
import com.onlineexam.admin.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private QuestionService questionService;

    private Exam exam;
    private Question question;
    private QuestionDTO questionDTO;
    private QuestionResponseDTO questionResponseDTO;

    @BeforeEach
    void setUp() {
        exam = new Exam(1, "Java Basics", "Test Java", 60, 100);
        question = new Question(1, "What is Java?", "Programming", "Easy", "A programming language", exam);
        questionDTO = new QuestionDTO("What is Java?", "Programming", "Easy", "A programming language");
        questionResponseDTO = new QuestionResponseDTO(1, "What is Java?", "Programming", "Easy", "A programming language", 1, "Java Basics");
    }

    @Test
    void testCreateQuestionSuccess() {
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        QuestionResponseDTO result = questionService.createQuestion(1, questionDTO);

        assertNotNull(result);
        assertEquals(question.getQuestionId(), result.getQuestionId());
        assertEquals(question.getQuestionText(), result.getQuestionText());
        verify(examRepository, times(1)).findById(1);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testCreateQuestionExamNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());

        QuestionResponseDTO result = questionService.createQuestion(1, questionDTO);

        assertNull(result);
        verify(examRepository, times(1)).findById(1);
        verify(questionRepository, times(0)).save(any(Question.class));
    }

    @Test
    void testGetAllQuestions() {
        when(questionRepository.findAll()).thenReturn(Arrays.asList(question));

        List<QuestionResponseDTO> results = questionService.getAllQuestions();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(question.getQuestionText(), results.get(0).getQuestionText());
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void testGetQuestionByIdFound() {
        when(questionRepository.findById(1)).thenReturn(Optional.of(question));

        Optional<QuestionResponseDTO> result = questionService.getQuestionById(1);

        assertTrue(result.isPresent());
        assertEquals(question.getQuestionId(), result.get().getQuestionId());
        verify(questionRepository, times(1)).findById(1);
    }

    @Test
    void testGetQuestionByIdNotFound() {
        when(questionRepository.findById(1)).thenReturn(Optional.empty());

        Optional<QuestionResponseDTO> result = questionService.getQuestionById(1);

        assertFalse(result.isPresent());
        verify(questionRepository, times(1)).findById(1);
    }

    @Test
    void testGetQuestionsByExamId() {
        when(questionRepository.findByExam_ExamId(1)).thenReturn(Arrays.asList(question));

        List<QuestionResponseDTO> results = questionService.getQuestionsByExamId(1);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(question.getQuestionText(), results.get(0).getQuestionText());
        verify(questionRepository, times(1)).findByExam_ExamId(1);
    }

    @Test
    void testUpdateQuestionFound() {
        Question updatedQuestion = new Question(1, "What is Spring Boot?", "Framework", "Medium", "A Java framework", exam);
        QuestionDTO updatedQuestionDTO = new QuestionDTO("What is Spring Boot?", "Framework", "Medium", "A Java framework");

        when(questionRepository.findById(1)).thenReturn(Optional.of(question));
        when(questionRepository.save(any(Question.class))).thenReturn(updatedQuestion);

        QuestionResponseDTO result = questionService.updateQuestion(1, updatedQuestionDTO);

        assertNotNull(result);
        assertEquals(updatedQuestion.getQuestionText(), result.getQuestionText());
        verify(questionRepository, times(1)).findById(1);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testUpdateQuestionNotFound() {
        QuestionDTO updatedQuestionDTO = new QuestionDTO("What is Spring Boot?", "Framework", "Medium", "A Java framework");
        when(questionRepository.findById(1)).thenReturn(Optional.empty());

        QuestionResponseDTO result = questionService.updateQuestion(1, updatedQuestionDTO);

        assertNull(result);
        verify(questionRepository, times(1)).findById(1);
        verify(questionRepository, times(0)).save(any(Question.class));
    }

    @Test
    void testDeleteQuestionExists() {
        when(questionRepository.existsById(1)).thenReturn(true);
        doNothing().when(questionRepository).deleteById(1);

        boolean result = questionService.deleteQuestion(1);

        assertTrue(result);
        verify(questionRepository, times(1)).existsById(1);
        verify(questionRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteQuestionNotExists() {
        when(questionRepository.existsById(1)).thenReturn(false);

        boolean result = questionService.deleteQuestion(1);

        assertFalse(result);
        verify(questionRepository, times(1)).existsById(1);
        verify(questionRepository, times(0)).deleteById(anyInt());
    }
}