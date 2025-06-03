package org.example.posting.hibernate;

import org.example.posting.hibernate.entity.Sex;
import org.example.posting.hibernate.entity.Subscription;
import org.example.posting.hibernate.entity.Users;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (var sessionFactory = configuration.buildSessionFactory();
            var session = sessionFactory.openSession()){
            session.beginTransaction();

            Subscription subscription = session.get(Subscription.class, 1);
            Users user = Users.builder().userId(1L).name("User").surname("Surname")
                    .lastName("LastName")
                    .email("email")
                    .hdPassword("password")
                    .birthDate(LocalDate.of(2000, 04, 03))
                    .sex(Sex.FEMALE)
                    .photo("photo")
                    .registrationDate(LocalDate.now())
                    .subscription(subscription).build();

            session.saveOrUpdate(user);

            session.getTransaction().commit();
        }
    }
}
