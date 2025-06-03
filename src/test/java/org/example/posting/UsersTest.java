package org.example.posting;

import lombok.Cleanup;
import org.example.posting.hibernate.entity.Users;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.provider.HibernateUtils;

public class UsersTest {

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
