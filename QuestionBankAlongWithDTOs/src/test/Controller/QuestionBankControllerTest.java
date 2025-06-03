import com.example.demo.Controller.QuestionBankController;
import com.example.demo.DTO.QuestionDTO;
import com.example.demo.Entity.QuestionBank;
import com.example.demo.Service.QuestionBankService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionBankControllerTest {

    @Mock
    private QuestionBankService qbService;

    @InjectMocks
    private QuestionBankController controller;

    @Test
    public void testGetQuestionById_ReturnsOk() {
        // Arrange
        int id = 1;
        QuestionBank question = new QuestionBank();
        question.setId(id);
        question.setQuestion("What is Java?");
        question.setAnswer("A programming language");

        when(qbService.getById(id)).thenReturn(Optional.of(question));

        // Act
        ResponseEntity<QuestionDTO> response = controller.getQuestionById(id);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("What is Java?", response.getBody().getQuestion());
    }

    @Test
    public void testGetQuestionById_NotFound() {
        // Arrange
        int id = 33;
        when(qbService.getById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<QuestionDTO> response = controller.getQuestionById(id);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
