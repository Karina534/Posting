package org.example.posting.hibernate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCreateDto {
    @NotNull
    private Integer price;

    @NotNull
    private Long paymentMethodId;

    @NotNull
    private Long subscriptionTypeId;
}
