package org.example.posting.hibernate.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.dto.LoginDto;
import org.example.posting.hibernate.dto.UserEditDto;
import org.example.posting.hibernate.dto.UserForRegistrationDto;
import org.example.posting.hibernate.dto.UserProfileReadDto;
import org.example.posting.hibernate.entity.Article;
import org.example.posting.hibernate.entity.EmailVerificationToken;
import org.example.posting.hibernate.entity.Subscription;
import org.example.posting.hibernate.entity.Users;
import org.example.posting.hibernate.mapper.UserCreateForRegistrationMapper;
import org.example.posting.hibernate.mapper.UserEditSetterMapper;
import org.example.posting.hibernate.mapper.UserReadMapper;
import org.example.posting.hibernate.repository.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateForRegistrationMapper registrationMapper;
    private final UserEditSetterMapper userEditMapper;
    private final NotificationRepository notificationRepository;
    private final ArticleRepository articleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileReadDto getProfile(Long id){
        // Загружаем пользователя и подписку
        Users user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        // Загружаем количество уведомлений
        Long notificationCount = notificationRepository.countByUserId(id);

        // Загружаем статьи (Загружается статистика и мб что-то еще, надо подумать)
        List<Article> articles = articleRepository.findArticlesByUserId(id);

        return userReadMapper.mapFrom(user, notificationCount, articles);
    }

    @Transactional
    public Long registration(UserForRegistrationDto user){
        // Проверка почты на уникальность
        System.out.println("Начинаем проверку почты на уникальность");
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        System.out.println("Почта проверена на уникальность");

        // Составим сущность, добавив время регистрации и базовую подписку
        Users userEntity = registrationMapper.mapFrom(user);
        System.out.println("Сформировали сущность на основе dto");
        user.setHdPassword(null);
        Subscription baseSubscription = subscriptionRepository.findById(1L).orElseThrow(
                () -> new RuntimeException("Базовая подписка не найдена")
        );
        System.out.println("Нашли базовую подписку в бд");
        userEntity.setSubscription(baseSubscription);

        // Сохраним сущность в базу
        Users savesUser = userRepository.save(userEntity);
        System.out.println("Сохранили сущность в базу");

        // Создание и сохранение токена подтверждения почты
        EmailVerificationToken token = EmailVerificationToken.of(savesUser);
        tokenRepository.save(token);
        System.out.println("Создали и сохранили токен");

        // Отправляем сообщение для подтверждения почты
        sendVerificationEmail(savesUser.getEmail(), token.toString());
        System.out.println("Сообщение для подтверждения почты отправлено");

        return savesUser.getUserId();
    }

    protected void sendVerificationEmail(String email, String token){
        String link = "http://localhost:8080/api/confirm?token=" + token;
        String subject = "Подтверждение регистрации";
        String body = "Перейдите по ссылке для подтверждения " + link;

        // Здесь будет какая-то отправка письма, Kafka?
        System.out.println("Заглушка с отправлением вместо почты токена " + token);
    }

    @Transactional
    public Long editUser(UserEditDto editDto){
        System.out.println("Ищем существующего пользователя в базе");
        Users existsUser = userRepository.findById(editDto.getUserId())
                        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        System.out.println("Редактируем сущность по dto");
        Users user = userEditMapper.mapFrom(existsUser, editDto);
        userRepository.save(user);
        System.out.println("Сохранили пользователя в базе");
        return editDto.getUserId();
    }

    @Transactional
    public boolean deleteUser(Long userId){
        System.out.println("Находим пользователя по id");
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        // Удалить подписку если не базовая
        Subscription subscription = user.getSubscription();
        if (subscription.getSubscriptionId() != 1L){
            subscriptionRepository.delete(subscription);
            System.out.println("Подписка не базовая, удаляем");
        }
        System.out.println("Подписка базовая, не удаляем");

        userRepository.delete(user);
        System.out.println("Удалили пользователя");

        // У всех статей автор установится null автоматически, все уведомления пользователя удаляться автоматически
        return true;
    }

    @Transactional
    public Long loginUser(LoginDto loginDto){
        System.out.println("Пытаемся найти пользователя по email");
        Users user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не был найден"));

        System.out.println("Проверяем пароли");
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getHdPassword())){
            throw new BadCredentialsException("Неверный пароль");
        }

        return user.getUserId();
    }
}










