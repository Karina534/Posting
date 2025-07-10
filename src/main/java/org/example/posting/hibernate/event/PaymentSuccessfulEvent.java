package org.example.posting.hibernate.event;

import lombok.Getter;

@Getter
public class PaymentSuccessfulEvent {
    private final Long paymentId;
    private final Long subscriptionTypeId;
    public PaymentSuccessfulEvent(Long paymentId, Long subscriptionTypeId) {
        this.paymentId = paymentId;
        this.subscriptionTypeId = subscriptionTypeId;
    }
}
