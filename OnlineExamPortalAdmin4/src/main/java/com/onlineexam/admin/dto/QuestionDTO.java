package com.onlineexam.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    @NotBlank(message = "Question text cannot be blank")
    private String questionText;

    private String category;
    private String difficulty;

    @NotBlank(message = "Correct answer cannot be blank")
    private String correctAnswer;
}