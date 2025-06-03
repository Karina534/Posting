package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "comment_text", length = 3000, nullable = false)
    private String commentText;

    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate;

    @PrePersist
    public void prePersist() {
        if (publishedDate == null) {
            publishedDate = LocalDateTime.now();
        }
    }
}
