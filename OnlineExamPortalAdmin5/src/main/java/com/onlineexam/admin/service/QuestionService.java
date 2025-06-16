package com.onlineexam.admin.service;

import com.onlineexam.admin.dto.QuestionDTO;
import com.onlineexam.admin.dto.QuestionResponseDTO;
import com.onlineexam.admin.entity.Exam;
import com.onlineexam.admin.entity.Question;
import com.onlineexam.admin.repository.ExamRepository;
import com.onlineexam.admin.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    // Injected to check exam existence and set relationship
    private ExamRepository examRepository;

    public QuestionResponseDTO createQuestion(Integer examId, QuestionDTO questionDTO) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        // Find the associated exam
        if (optionalExam.isPresent()) {
            Exam exam = optionalExam.get();
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setCategory(questionDTO.getCategory());
            question.setDifficulty(questionDTO.getDifficulty());
            question.setCorrectAnswer(questionDTO.getCorrectAnswer());
            question.setExam(exam); // Set the relationship to the exam entity

            Question savedQuestion = questionRepository.save(question);
            return convertToQuestionResponseDTO(savedQuestion);
        }
        return null; 
    }

    public List<QuestionResponseDTO> getAllQuestions() {
        // When fetching all questions, ensure exam details are loaded if needed for DTO
        // This might require a join fetch or a separate query if performance is critical
        return questionRepository.findAll().stream()
                .map(this::convertToQuestionResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<QuestionResponseDTO> getQuestionById(Integer id) {
        // Consider fetching exam details if needed for DTO here as well
        return questionRepository.findById(id)
                .map(this::convertToQuestionResponseDTO);
    }

    public List<QuestionResponseDTO> getQuestionsByExamId(Integer examId) {
        // findByExam_ExamId will correctly fetch questions linked to the exam
        // The Exam object within each Question entity will be populated correctly
        return questionRepository.findByExam_ExamId(examId).stream()
                .map(this::convertToQuestionResponseDTO)
                .collect(Collectors.toList());
    }

    public QuestionResponseDTO updateQuestion(Integer id, QuestionDTO questionDetails) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Question existingQuestion = optionalQuestion.get();
            existingQuestion.setQuestionText(questionDetails.getQuestionText());
            existingQuestion.setCategory(questionDetails.getCategory());
            existingQuestion.setDifficulty(questionDetails.getDifficulty());
            existingQuestion.setCorrectAnswer(questionDetails.getCorrectAnswer());
            Question updatedQuestion = questionRepository.save(existingQuestion);
            return convertToQuestionResponseDTO(updatedQuestion);
        } else {
            return null; // Or throw an exception
        }
    }

    public boolean deleteQuestion(Integer id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Helper method to convert Entity to DTO
    private QuestionResponseDTO convertToQuestionResponseDTO(Question question) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setQuestionText(question.getQuestionText());
        dto.setCategory(question.getCategory());
        dto.setDifficulty(question.getDifficulty());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        // Critically, if exam is LAZY loaded, you need to ensure it's accessed within the transaction
        // or fetched eagerly, otherwise you'll get LazyInitializationException
        if (question.getExam() != null) {
            dto.setExamId(question.getExam().getExamId());
            dto.setExamTitle(question.getExam().getTitle());
        }
        return dto;
    }
}