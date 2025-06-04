package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "images", schema = "public")
@Data
@ToString(exclude = "article")
@EqualsAndHashCode(exclude = "article")
@NoArgsConstructor
@AllArgsConstructor
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(name = "uploaded_date", nullable = false)
    private LocalDate uploadedDate;
}
