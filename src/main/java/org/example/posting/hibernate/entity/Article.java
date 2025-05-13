package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(length = 400)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String info;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
}
