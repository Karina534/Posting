package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription", schema = "public")
@Entity
@ToString(exclude = "payments")
@EqualsAndHashCode(exclude = "payments")
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
    @Column(name = "start_date")
    private LocalDate startDate = LocalDate.now();

    @FutureOrPresent
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive = false;

    // N+1 problem join fetch в репозитории
    @Builder.Default
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    @Version
    private Long version;

    @AssertTrue(message = "Start date must be before end date.")
    public boolean isStartBeforeEnd() {
        if (endDate == null){
            return true;
        }
        return startDate.isBefore(endDate);
    }
}
