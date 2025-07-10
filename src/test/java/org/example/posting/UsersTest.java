package org.example.posting;

import lombok.Cleanup;
import org.example.posting.hibernate.entity.*;
import org.example.posting.util.HibernateUtil;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UsersTest {
    @Test
    public void checkH2(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var category = Category.builder().title("Finans").build();
        session.save(category);

        session.getTransaction().commit();
    }

    @Test
    public void checkOneToOneArticleLikeStats(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Article article = session.get(Article.class, 4L);
        LikeStats likeStats = LikeStats.builder().likeStatsId(1L).build();

        session.saveOrUpdate(article);
        likeStats.setArticle(article);
        session.save(likeStats);

        session.getTransaction().commit();
    }

    @Test
    public void checkOrphanRemovalForArticleAndImage(){
        var configuration = new Configuration();
        configuration.configure();

        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Article article = session.get(Article.class, 4L);
        article.getImages().removeIf(images -> images.getImageId().equals(1L));

        session.getTransaction().commit();
    }

    @Test
    public void checkArticleSetOfImg(){
        var configuration = new Configuration();
        configuration.configure();

        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Article article = Article.builder()
                .articleId(2L)
                .user(session.get(Users.class, 1L))
                .title("Title")
                .info("Info")
                .publishedDate(LocalDate.now())
                .category(session.get(Category.class, 1L))
                .build();
        Images image = new Images(1L, article, "url1", LocalDate.now());

        article.addImage(image);
        session.saveOrUpdate(article);

        session.getTransaction().commit();
    }

//    @Test
//    public void checkArticleSetOfImg(){
//        var configuration = new Configuration();
//        configuration.configure();
//
//        @Cleanup var sessionFactory = configuration.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        var article = session.get(Article.class, 1L);
//        System.out.println(article.getImages());
//
//        session.getTransaction().commit();
//    }

    @Test
    public void oneToManyNotificationsTest(){
        var configuration = new Configuration();
        configuration.configure();

        @Cleanup var sessionFactory = configuration.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Users user = session.get(Users.class, 1L);
        System.out.println(user);
        System.out.println("----------");
        System.out.println(user.getArticles());
        session.getTransaction().commit();
    }
}
