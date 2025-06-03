package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "paid_date_time", nullable = false)
    private LocalDateTime paidDateTime;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
}
