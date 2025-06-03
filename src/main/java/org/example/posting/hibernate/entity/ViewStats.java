package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "view_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_stats_id")
    private Long viewStatsId;

    @OneToOne(optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
}
