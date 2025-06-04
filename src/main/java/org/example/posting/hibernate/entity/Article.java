package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "article")
@Data
@Builder
@ToString(exclude = {"user", "category", "images", "comments", "likeLogs", "viewLogs"})
@EqualsAndHashCode(exclude = {"images", "comments", "likeLogs", "viewLogs"})
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(length = 400)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String info;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "article", cascade = CascadeType.ALL)
    public LikeStats likeStats;

    @OneToOne(mappedBy = "article", cascade = CascadeType.ALL)
    public ViewStats viewStats;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Images> images = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeLog> likeLogs = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ViewLog> viewLogs = new HashSet<>();

    public void addImage(Images image){
        images.add(image);
        image.setArticle(this);
    }

    public void addComments(Comment comment){
        comments.add(comment);
        comment.setArticle(this);
    }

    public void addLikeLog(LikeLog likeLog){
        likeLogs.add(likeLog);
        likeLog.setArticle(this);
    }

    public void addViewLog(ViewLog viewLog){
        viewLogs.add(viewLog);
        viewLog.setArticle(this);
    }
}
