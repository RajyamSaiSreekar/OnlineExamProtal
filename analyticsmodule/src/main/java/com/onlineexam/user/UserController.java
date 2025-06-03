package com.onlineexam.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<String> registerUser() {
        return ResponseEntity.ok("User registration - Dummy");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser() {
        return ResponseEntity.ok("User login - Dummy");
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<String> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok("User profile - Dummy");
    }
}
