package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "view_log")
@Data
@ToString(exclude = {"user", "article"})
@EqualsAndHashCode(exclude = {"user", "article"})
@NoArgsConstructor
@AllArgsConstructor
public class ViewLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_log_id")
    private Long viewLogId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "view_date_time", nullable = false)
    private LocalDateTime viewDateTime;

    @PrePersist
    public void prePersist() {
        if (viewDateTime == null) {
            viewDateTime = LocalDateTime.now();
        }
    }
}
