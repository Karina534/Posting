package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription", schema = "public")
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "subscription_type_id", nullable = false)
    private SubscriptionType subscriptionType;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate = LocalDate.now();

    @FutureOrPresent
    private LocalDate endDate;

    @NotNull
    private boolean isActive = false;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @AssertTrue(message = "Start date must be before end date.")
    public boolean isStartBeforeEnd() {
        if (endDate == null){
            return true;
        }
        return startDate.isBefore(endDate);
    }
}
