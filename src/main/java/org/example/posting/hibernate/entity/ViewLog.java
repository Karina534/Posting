package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "view_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_log_id")
    private Long viewLogId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
