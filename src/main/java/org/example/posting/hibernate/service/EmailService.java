package org.example.posting.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.entity.EmailVerificationToken;
import org.example.posting.hibernate.entity.Users;
import org.example.posting.hibernate.repository.TokenRepository;
import org.example.posting.hibernate.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public String checkToken(String token){
        Optional<EmailVerificationToken> isToken = tokenRepository.findByToken(token);

        if (isToken.isEmpty()){
            return "Такого токена не существует";
        }

        EmailVerificationToken verificationToken = isToken.get();

        if (verificationToken.isOverdue()){
            return "Токен устарел";
        }

        Users user = verificationToken.getUser();
        user.setEmailConfirmed(true);
        tokenRepository.delete(verificationToken);
        userRepository.save(user);

        return "Токен подтвержден";
    }
}
