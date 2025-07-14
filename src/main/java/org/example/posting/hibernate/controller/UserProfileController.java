package org.example.posting.hibernate.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.dto.*;
import org.example.posting.hibernate.entity.Users;
import org.example.posting.hibernate.service.AuthService;
import org.example.posting.hibernate.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;

    // Страница профиля
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileReadDto> getProfile(@PathVariable Long id){
        return ResponseEntity.ok(userService.getProfile(id));
        // Сделать загрузку статистики статей не ленивой
        // Переделать загрузку подписки?
    }

    // Страница для редактирования полей профиля
    @PatchMapping("/reduction/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody UserEditDto editDto, BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        System.out.println("Валидация профиля прошла успешно");

        Long updatedUserId = userService.editUser(editDto);
        System.out.println("Пользователь обновлен");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Пользователь отредактирован");
        response.put("userId", updatedUserId);
        response.put("redirect", String.format("/%s/profile", updatedUserId));

        return ResponseEntity.ok(response);
    }

    // Страница для удаления профиля
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id){
        try {
            boolean isDeleted = userService.deleteUser(id);
            if (isDeleted){
                return ResponseEntity.ok(Map.of(
                        "message", "Пользователь успешно удален",
                        "status", "success"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ошибка при удалении пользователя");
            }
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Пользователь не найден"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Не удалось удалить пользователя", "error", e.getMessage()));
        }
    }
}













