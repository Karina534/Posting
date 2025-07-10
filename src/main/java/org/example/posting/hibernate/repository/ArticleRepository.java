package org.example.posting.hibernate.repository;

import org.example.posting.hibernate.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("""
            select a from Article a
            join fetch a.category
            join fetch a.user
            where a.user.userId = :userId
            """)
    List<Article> findArticlesByUserId(@Param("userId") Long id);
}
