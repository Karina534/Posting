package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "view_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_stats_id")
    private Long viewStatsId;

    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    @Version
    private Long version;

    public void setArticle(Article article){
        this.article = article;
        article.setViewStats(this);
    }
}
