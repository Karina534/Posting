package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "like_log")
@Data
@ToString(exclude = {"user", "article"})
@EqualsAndHashCode(exclude = {"user", "article"})
@NoArgsConstructor
@AllArgsConstructor
public class LikeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_log_id")
    private Long likeLogId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "like_date_time", nullable = false)
    private LocalDateTime likeDateTime;

    @PrePersist
    public void prePersist() {
        if (likeDateTime == null) {
            likeDateTime = LocalDateTime.now();
        }
    }
}
