package org.example.posting.hibernate.dto;

import jakarta.validation.constraints.NotNull;

public class SubscriptionCreateDto {
    // все остальные поля устанавливаем в сервисе автоматически
    // Устанавливаем платеж в сервисе для оплаты сервиса. Подписка создается после положительного платежа
    @NotNull(message = "Тип подписки обязателен")
    private Long subscriptionTypeId;
}
