package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_stats_id")
    private Long likeStatsId;

    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "like_count", nullable = false)
    @Builder.Default
    private Long likeCount = 0L;

    @Version
    private Long version;

    public void setArticle(Article article){
        this.article = article;
        article.setLikeStats(this);
    }
}
