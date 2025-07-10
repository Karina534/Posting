package org.example.posting.hibernate.handler;

import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.entity.Payment;
import org.example.posting.hibernate.entity.Subscription;
import org.example.posting.hibernate.entity.SubscriptionType;
import org.example.posting.hibernate.event.PaymentSuccessfulEvent;
import org.example.posting.hibernate.repository.PaymentRepository;
import org.example.posting.hibernate.repository.SubscriptionRepository;
import org.example.posting.hibernate.repository.SubscriptionTypeRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SubscriptionAfterPaymentHandler {
    private final PaymentRepository paymentRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final SubscriptionRepository subscriptionRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(PaymentSuccessfulEvent paymentEvent){
        System.out.println("Мы зашли в слушателя");
        Payment payment = paymentRepository.findById(paymentEvent.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(paymentEvent.getSubscriptionTypeId())
                .orElseThrow(() -> new RuntimeException("subscriptionType was not found"));

        System.out.println("Получили платеж и тип подписки по id");

        Subscription subscription = Subscription.builder()
                .subscriptionType(subscriptionType)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .isActive(true)
                .build();
        System.out.println("Составили подписку");

        Subscription subscriptionSaved = subscriptionRepository.save(subscription);

        System.out.println("Сохранили подписку");
        payment.setSubscription(subscriptionSaved);
        subscriptionSaved.getPayments().add(payment);

        // Чтобы бд обновил subscriptionId
        paymentRepository.save(payment);
    }
}
