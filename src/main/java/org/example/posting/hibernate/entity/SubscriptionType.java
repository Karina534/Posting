package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription_type", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionTypeId;

    @Column(length = 100)
    private String title;
}
