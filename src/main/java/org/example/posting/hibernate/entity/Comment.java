package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@ToString(exclude = {"user", "article"})
@EqualsAndHashCode(exclude = {"article", "user"})
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @NotNull
    @Column(name = "comment_text", length = 3000, nullable = false)
    private String commentText;

    @NotNull
    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate;

    @PrePersist
    public void prePersist() {
        if (publishedDate == null) {
            publishedDate = LocalDateTime.now();
        }
    }
}
