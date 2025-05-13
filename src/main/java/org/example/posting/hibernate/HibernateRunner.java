package org.example.posting.hibernate;

import org.example.posting.hibernate.entity.Sex;
import org.example.posting.hibernate.entity.Users;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();

//        try (var sessionFactory = configuration.buildSessionFactory();
//            var session = sessionFactory.openSession()){
//            session.beginTransaction();
//
//            session.saveOrUpdate(Users.builder().user_id(1L).name("User").surname("Surname")
//                    .last_name("LastName")
//                    .email("email")
//                    .hd_password("password")
//                    .birth_date(LocalDate.of(2000, 04, 03))
//                    .sex(Sex.FEMALE)
//                    .photo("photo")
//                    .registration_date(LocalDate.now())
//                    .subscription_id(1L).build());
//
//            session.getTransaction().commit();
//        }
    }
}
