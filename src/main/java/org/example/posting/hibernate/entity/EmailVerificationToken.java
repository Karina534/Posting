package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verification_token", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_verification_id")
    private Long emailVerificationId;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne()
    @JoinColumn(nullable = false, name = "user_id")
    private Users user;

    @Column(nullable = false)
    private LocalDateTime endDate;

    public static EmailVerificationToken of(Users user){
        return EmailVerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .endDate(LocalDateTime.now().plusMinutes(5))
                .build();
    }

    public boolean isOverdue(){
        return endDate.isBefore(LocalDateTime.now());
    }
}
