package org.example.posting.hibernate.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.event.PaymentSuccessfulEvent;
import org.example.posting.hibernate.dto.PaymentCreateDto;
import org.example.posting.hibernate.entity.Payment;
import org.example.posting.hibernate.entity.PaymentMethod;
import org.example.posting.hibernate.repository.PaymentMethodRepository;
import org.example.posting.hibernate.repository.PaymentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long pay(PaymentCreateDto paymentCreateDto){
        // Создание платежа из дто
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentCreateDto.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method wast found"));
        System.out.println("Нашли paymentMethod по id: " + paymentMethod.getTitle());

        Payment payment = Payment.builder()
                .price(paymentCreateDto.getPrice())
                .isPaid(false)
                .paymentMethod(paymentMethod)
                .build();
        System.out.println("Построили payment по дто");

        // ! Оплата в стороннем сервисе

        Payment paymentSaved = paymentRepository.save(payment);

        return paymentSaved.getPaymentId();
        // Не забыть добавить слушателя в подписку
        // У пользователя нужно обновить ссылку на подписку???
    }

    @Transactional
    public void markAsPaid(Long paymentId, Long subscriptionId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment with such id was not found"));
        payment.setPaid(true);
        payment.setPaidDateTime(LocalDateTime.now());

        // В будущем можно реализовать прослушивание через Kafka и разделить платежи и подписки на микросервисы
        applicationEventPublisher.publishEvent(new PaymentSuccessfulEvent(paymentId, subscriptionId));
    }
}
