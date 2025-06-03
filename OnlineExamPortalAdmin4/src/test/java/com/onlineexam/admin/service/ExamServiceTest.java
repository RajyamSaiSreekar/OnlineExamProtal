package com.onlineexam.admin.service;

import com.onlineexam.admin.dto.ExamDTO;
import com.onlineexam.admin.dto.ExamResponseDTO;
import com.onlineexam.admin.entity.Exam;
import com.onlineexam.admin.repository.ExamRepository;
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
public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamService examService;

    private Exam exam;
    private ExamDTO examDTO;
    private ExamResponseDTO examResponseDTO;

    @BeforeEach
    void setUp() {
        exam = new Exam(1, "Java Basics", "Test your Java knowledge", 60, 100);
        examDTO = new ExamDTO("Java Basics", "Test your Java knowledge", 60, 100);
        examResponseDTO = new ExamResponseDTO(1, "Java Basics", "Test your Java knowledge", 60, 100);
    }

    @Test
    void testCreateExam() {
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        ExamResponseDTO result = examService.createExam(examDTO);

        assertNotNull(result);
        assertEquals(exam.getExamId(), result.getExamId());
        assertEquals(exam.getTitle(), result.getTitle());
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void testGetAllExams() {
        when(examRepository.findAll()).thenReturn(Arrays.asList(exam));

        List<ExamResponseDTO> results = examService.getAllExams();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(exam.getTitle(), results.get(0).getTitle());
        verify(examRepository, times(1)).findAll();
    }

    @Test
    void testGetExamByIdFound() {
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));

        Optional<ExamResponseDTO> result = examService.getExamById(1);

        assertTrue(result.isPresent());
        assertEquals(exam.getExamId(), result.get().getExamId());
        verify(examRepository, times(1)).findById(1);
    }

    @Test
    void testGetExamByIdNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());

        Optional<ExamResponseDTO> result = examService.getExamById(1);

        assertFalse(result.isPresent());
        verify(examRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateExamFound() {
        Exam updatedExam = new Exam(1, "Advanced Java", "Updated description", 90, 150);
        ExamDTO updatedExamDTO = new ExamDTO("Advanced Java", "Updated description", 90, 150);

        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenReturn(updatedExam);

        ExamResponseDTO result = examService.updateExam(1, updatedExamDTO);

        assertNotNull(result);
        assertEquals(updatedExam.getTitle(), result.getTitle());
        assertEquals(updatedExam.getDuration(), result.getDuration());
        verify(examRepository, times(1)).findById(1);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void testUpdateExamNotFound() {
        ExamDTO updatedExamDTO = new ExamDTO("Advanced Java", "Updated description", 90, 150);
        when(examRepository.findById(1)).thenReturn(Optional.empty());

        ExamResponseDTO result = examService.updateExam(1, updatedExamDTO);

        assertNull(result);
        verify(examRepository, times(1)).findById(1);
        verify(examRepository, times(0)).save(any(Exam.class));
    }

    @Test
    void testDeleteExamExists() {
        when(examRepository.existsById(1)).thenReturn(true);
        doNothing().when(examRepository).deleteById(1);

        boolean result = examService.deleteExam(1);

        assertTrue(result);
        verify(examRepository, times(1)).existsById(1);
        verify(examRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteExamNotExists() {
        when(examRepository.existsById(1)).thenReturn(false);

        boolean result = examService.deleteExam(1);

        assertFalse(result);
        verify(examRepository, times(1)).existsById(1);
        verify(examRepository, times(0)).deleteById(anyInt());
    }

    @Test
    void testExamExists() {
        when(examRepository.existsById(1)).thenReturn(true);
        assertTrue(examService.examExists(1));
        verify(examRepository, times(1)).existsById(1);
    }

    @Test
    void testExamNotExists() {
        when(examRepository.existsById(1)).thenReturn(false);
        assertFalse(examService.examExists(1));
        verify(examRepository, times(1)).existsById(1);
    }
}