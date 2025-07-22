//package org.example.posting.hibernate.service;
//
//import io.jsonwebtoken.Claims;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.security.auth.message.AuthException;
//import jakarta.transaction.Transactional;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.example.posting.hibernate.dto.JwtAuthentication;
//import org.example.posting.hibernate.dto.JwtResponse;
//import org.example.posting.hibernate.dto.LoginDto;
//import org.example.posting.hibernate.entity.Users;
//import org.example.posting.hibernate.repository.UserRepository;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final Map<String, String> refreshStorage = new HashMap<>();
//
//    // Авторизация
//    @Transactional
//    public JwtResponse loginUser(@NonNull LoginDto loginDto){
//        System.out.println("Пытаемся найти пользователя по email");
//
//        Users user = userRepository.findByEmail(loginDto.getEmail())
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//
//        System.out.println("Проверяем пароли");
//        if (passwordEncoder.matches(loginDto.getPassword(), user.getHdPassword())){
//            return new JwtResponse(accessToken, refreshToken);
//
//        } else {
//            throw new BadCredentialsException("Неверный пароль");
//        }
//    }
//
//    public JwtResponse getAccessToken(@NonNull String refreshToken){
//        if (jwtProvider.validateRefreshToken(refreshToken)){
//            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
//            final String login = claims.getSubject();
//            final String saveRefreshToken = refreshStorage.get(login);
//
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)){
//                Users user = userRepository.findByEmail(login)
//                        .orElseThrow(() -> new EntityNotFoundException("Пользователь не был найден"));
//                final String accessToken = jwtProvider.generateAccessToken(user);
//                return new JwtResponse(accessToken, null);
//            }
//        }
//        return new JwtResponse(null, null);
//    }
//
//    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
//        if (jwtProvider.validateRefreshToken(refreshToken)){
//            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
//            final String login = claims.getSubject();
//            final String saveRefreshToken = refreshStorage.get(login);
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)){
//                Users user = userRepository.findByEmail(login)
//                        .orElseThrow(() -> new EntityNotFoundException("Пользователь не был найден"));
//                final String accessToken = jwtProvider.generateAccessToken(user);
//                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
//                refreshStorage.put(user.getEmail(), newRefreshToken);
//                return new JwtResponse(accessToken, newRefreshToken);
//            }
//        }
//        throw new AuthException("Невалидный JWT токен");
//    }
//
//    public JwtAuthentication getAuthInfo(){
//        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
