package com.onlineexam.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineexam.admin.dto.QuestionDTO;
import com.onlineexam.admin.dto.QuestionResponseDTO;
import com.onlineexam.admin.service.ExamService;
import com.onlineexam.admin.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

   
    private QuestionService questionService;

    private ExamService examService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuestionDTO questionDTO;
    private QuestionResponseDTO questionResponseDTO;

    @BeforeEach
    void setUp() {
        questionDTO = new QuestionDTO("What is an API?", "General", "Easy", "Application Programming Interface");
        questionResponseDTO = new QuestionResponseDTO(1, "What is an API?", "General", "Easy", "Application Programming Interface", 1, "Software Engineering Exam");
    }

    @Test
    void testCreateQuestionSuccess() throws Exception {
        when(examService.examExists(1)).thenReturn(true);
        when(questionService.createQuestion(eq(1), any(QuestionDTO.class))).thenReturn(questionResponseDTO);

        mockMvc.perform(post("/api/admin/questions/exam/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("Question created successfully"));
        verify(examService, times(1)).examExists(1);
        verify(questionService, times(1)).createQuestion(eq(1), any(QuestionDTO.class));
    }

    @Test
    void testCreateQuestionExamNotFound() throws Exception {
        when(examService.examExists(1)).thenReturn(false);

        mockMvc.perform(post("/api/admin/questions/exam/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionDTO)))
                .andExpect(status().isNotFound());
        verify(examService, times(1)).examExists(1);
        verify(questionService, times(0)).createQuestion(anyInt(), any(QuestionDTO.class));
    }

    @Test
    void testGetAllQuestions() throws Exception {
        when(questionService.getAllQuestions()).thenReturn(Arrays.asList(questionResponseDTO));

        mockMvc.perform(get("/api/admin/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionId").value(1))
                .andExpect(jsonPath("$[0].questionText").value("What is an API?"));
        verify(questionService, times(1)).getAllQuestions();
    }

    @Test
    void testGetQuestionByIdFound() throws Exception {
        when(questionService.getQuestionById(1)).thenReturn(Optional.of(questionResponseDTO));

        mockMvc.perform(get("/api/admin/questions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1))
                .andExpect(jsonPath("$.questionText").value("What is an API?"));
        verify(questionService, times(1)).getQuestionById(1);
    }

    @Test
    void testGetQuestionByIdNotFound() throws Exception {
        when(questionService.getQuestionById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/questions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(questionService, times(1)).getQuestionById(1);
    }

    @Test
    void testGetQuestionsByExamIdFound() throws Exception {
        when(examService.examExists(1)).thenReturn(true);
        when(questionService.getQuestionsByExamId(1)).thenReturn(Arrays.asList(questionResponseDTO));

        mockMvc.perform(get("/api/admin/questions/exam/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionId").value(1))
                .andExpect(jsonPath("$[0].examId").value(1));
        verify(examService, times(1)).examExists(1);
        verify(questionService, times(1)).getQuestionsByExamId(1);
    }

    @Test
    void testGetQuestionsByExamIdExamNotFound() throws Exception {
        when(examService.examExists(1)).thenReturn(false);

        mockMvc.perform(get("/api/admin/questions/exam/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(examService, times(1)).examExists(1);
        verify(questionService, times(0)).getQuestionsByExamId(anyInt());
    }

    @Test
    void testUpdateQuestionFound() throws Exception {
        QuestionDTO updatedQuestionDTO = new QuestionDTO("What is JPA?", "ORM", "Medium", "Java Persistence API");
        QuestionResponseDTO updatedQuestionResponseDTO = new QuestionResponseDTO(1, "What is JPA?", "ORM", "Medium", "Java Persistence API", 1, "Software Engineering Exam");

        when(questionService.updateQuestion(eq(1), any(QuestionDTO.class))).thenReturn(updatedQuestionResponseDTO);

        mockMvc.perform(put("/api/admin/questions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedQuestionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText").value("What is JPA?"))
                .andExpect(jsonPath("$.category").value("ORM"));
        verify(questionService, times(1)).updateQuestion(eq(1), any(QuestionDTO.class));
    }

    @Test
    void testUpdateQuestionNotFound() throws Exception {
        when(questionService.updateQuestion(eq(1), any(QuestionDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/admin/questions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionDTO)))
                .andExpect(status().isNotFound());
        verify(questionService, times(1)).updateQuestion(eq(1), any(QuestionDTO.class));
    }

    @Test
    void testDeleteQuestionSuccess() throws Exception {
        when(questionService.deleteQuestion(1)).thenReturn(true);

        mockMvc.perform(delete("/api/admin/questions/1"))
                .andExpect(status().isNoContent());
        verify(questionService, times(1)).deleteQuestion(1);
    }

    @Test
    void testDeleteQuestionNotFound() throws Exception {
        when(questionService.deleteQuestion(1)).thenReturn(false);

        mockMvc.perform(delete("/api/admin/questions/1"))
                .andExpect(status().isNotFound());
        verify(questionService, times(1)).deleteQuestion(1);
    }
}