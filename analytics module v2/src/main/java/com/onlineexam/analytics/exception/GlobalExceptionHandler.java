package com.onlineexam.analytics.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;



 // Global exception handler for the application.
 // Catches and handles common exceptions across all controllers.

@ControllerAdvice
public class GlobalExceptionHandler {


	// This method handles exceptions of type MethodArgumentTypeMismatchException
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		// The response body includes a message indicating which parameter was invalid
        return ResponseEntity.badRequest().body("Invalid input: " + ex.getName() + " must be a number.");
    }
    

    

    // Handles IllegalArgumentExceptions thrown anywhere in the application.
    
   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
       // Returns a 400 Bad Request response with the exception message as the response body
       return ResponseEntity.badRequest().body(ex.getMessage());
   }



}
