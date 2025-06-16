package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "notification", schema = "public")
@Data
@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @NotNull
    @Column(name = "send_date_time", nullable = false)
    private LocalDateTime sendDateTime;

    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;
}
