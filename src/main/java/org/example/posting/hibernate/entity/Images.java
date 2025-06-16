package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    @NotNull
    @Column(name = "uploaded_date", nullable = false)
    private LocalDate uploadedDate;
}
