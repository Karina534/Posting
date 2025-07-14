package org.example.posting.hibernate.controller;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.dto.JwtResponse;
import org.example.posting.hibernate.dto.LoginDto;
import org.example.posting.hibernate.dto.RefreshJwtRequest;
import org.example.posting.hibernate.dto.UserForRegistrationDto;
import org.example.posting.hibernate.service.AuthService;
import org.example.posting.hibernate.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthService authService;

    // Страница для регистрации пользователя
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserForRegistrationDto registrationDto, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        System.out.println("Валидация формы прошла успешно, переходим к регистрации");

        Long savedUserId = userService.registration(registrationDto);
        System.out.println("Пользователь зарегистрирован");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Пользователь зарегистрирован");
        response.put("userId", savedUserId);
        response.put("redirect", String.format("/%s/profile", savedUserId));

        return ResponseEntity.ok(response);
    }

    // Страница авторизации
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        System.out.println("Валидация сущности прошли успешно");

        System.out.println("Пытаемся авторизироваться");
        final JwtResponse token = authService.loginUser(loginDto);
        System.out.println("Пользователь авторизирован");
        System.out.println("Дополнительно получаем userId");
        Long userId = userService.getUserId(loginDto);
        System.out.println("Получили userId = " + userId);

        // UserId передавать не нужно, так как фронт может сам его вытянуть из токена. Обычно делают универсальный
        // путь /profile, а фронт сам решает какого пользователя рендерить. Но у меня нет фронта, поэтому будем передавать
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Пользователь авторизирован");
        response.put("userId", userId);
        response.put("accessToken", token.getAccessToken());
        response.put("refreshToken", token.getRefreshToken());
        response.put("redirect", String.format("/%s/profile", userId));
        return ResponseEntity.ok(response);
    }

    // Страница для получения нового accessToken
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request){
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    // Обновление accessToken и refreshToken
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNerRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
