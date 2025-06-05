package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@ToString(exclude = "articles")
@EqualsAndHashCode(exclude = "articles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(length = 100, nullable = false)
    private String title;

    @Builder.Default
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();
}
