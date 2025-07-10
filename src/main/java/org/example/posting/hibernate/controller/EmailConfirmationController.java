package org.example.posting.hibernate.controller;

import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.entity.EmailVerificationToken;
import org.example.posting.hibernate.repository.TokenRepository;
import org.example.posting.hibernate.service.EmailService;
import org.example.posting.hibernate.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/api/email")
@RequiredArgsConstructor
@RestController
public class EmailConfirmationController {
    private final EmailService emailService;

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token){
        String message = emailService.checkToken(token);
        return ResponseEntity.ok(message);

        // В будущем, если токен просрочен, то отправить новый
    }
}
