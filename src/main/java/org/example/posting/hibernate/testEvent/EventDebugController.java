package org.example.posting.hibernate.testEvent;

import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.dto.PaymentCreateDto;
import org.example.posting.hibernate.entity.Payment;
import org.example.posting.hibernate.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventDebugController {

    private final PaymentService paymentService;

    @GetMapping("/test-event")
    public void testEvent(){
        System.out.println("Начинаем тестирование события и слушателя");
        System.out.println("Создаем дто для платежа");
        PaymentCreateDto paymentCreateDto = new PaymentCreateDto(1500, 1L, 2L);

        System.out.println("Вызываем метод pay для этой дто");
        Long paymentId = paymentService.pay(paymentCreateDto);

        System.out.println("Публикуем событие");
        paymentService.markAsPaid(paymentId, paymentCreateDto.getSubscriptionTypeId());

        System.out.println("Все прошло успешно");
    }
}
