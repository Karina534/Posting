package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "subscription_type", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class SubscriptionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_type_id")
    private Integer subscriptionTypeId;

    @Column(length = 100)
    private String title;
}
