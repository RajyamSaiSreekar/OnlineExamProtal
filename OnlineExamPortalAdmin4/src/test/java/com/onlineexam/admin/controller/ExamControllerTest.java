package com.onlineexam.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineexam.admin.dto.ExamDTO;
import com.onlineexam.admin.dto.ExamResponseDTO;
import com.onlineexam.admin.service.ExamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
public class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

   
    private ExamService examService;

    @Autowired
    private ObjectMapper objectMapper;

    private ExamDTO examDTO;
    private ExamResponseDTO examResponseDTO;

    @BeforeEach
    void setUp() {
        examDTO = new ExamDTO("Spring Boot Test", "Description for Spring Boot Test", 45, 80);
        examResponseDTO = new ExamResponseDTO(1, "Spring Boot Test", "Description for Spring Boot Test", 45, 80);
    }

    @Test
    void testCreateExam() throws Exception {
        when(examService.createExam(any(ExamDTO.class))).thenReturn(examResponseDTO);

        mockMvc.perform(post("/api/admin/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("Exam created successfully"));
        verify(examService, times(1)).createExam(any(ExamDTO.class));
    }

    @Test
    void testGetAllExams() throws Exception {
        when(examService.getAllExams()).thenReturn(Arrays.asList(examResponseDTO));

        mockMvc.perform(get("/api/admin/exams")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].examId").value(1))
                .andExpect(jsonPath("$[0].title").value("Spring Boot Test"));
        verify(examService, times(1)).getAllExams();
    }

    @Test
    void testGetExamByIdFound() throws Exception {
        when(examService.getExamById(1)).thenReturn(Optional.of(examResponseDTO));

        mockMvc.perform(get("/api/admin/exams/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.examId").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot Test"));
        verify(examService, times(1)).getExamById(1);
    }

    @Test
    void testGetExamByIdNotFound() throws Exception {
        when(examService.getExamById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/exams/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(examService, times(1)).getExamById(1);
    }

    @Test
    void testUpdateExamFound() throws Exception {
        ExamDTO updatedExamDTO = new ExamDTO("Updated Spring Boot Test", "Updated Description", 50, 90);
        ExamResponseDTO updatedExamResponseDTO = new ExamResponseDTO(1, "Updated Spring Boot Test", "Updated Description", 50, 90);

        when(examService.updateExam(eq(1), any(ExamDTO.class))).thenReturn(updatedExamResponseDTO);

        mockMvc.perform(put("/api/admin/exams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedExamDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Spring Boot Test"))
                .andExpect(jsonPath("$.duration").value(50));
        verify(examService, times(1)).updateExam(eq(1), any(ExamDTO.class));
    }

    @Test
    void testUpdateExamNotFound() throws Exception {
        when(examService.updateExam(eq(1), any(ExamDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/admin/exams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examDTO)))
                .andExpect(status().isNotFound());
        verify(examService, times(1)).updateExam(eq(1), any(ExamDTO.class));
    }

    @Test
    void testDeleteExamSuccess() throws Exception {
        when(examService.deleteExam(1)).thenReturn(true);

        mockMvc.perform(delete("/api/admin/exams/1"))
                .andExpect(status().isNoContent());
        verify(examService, times(1)).deleteExam(1);
    }

    @Test
    void testDeleteExamNotFound() throws Exception {
        when(examService.deleteExam(1)).thenReturn(false);

        mockMvc.perform(delete("/api/admin/exams/1"))
                .andExpect(status().isNotFound());
        verify(examService, times(1)).deleteExam(1);
    }
}