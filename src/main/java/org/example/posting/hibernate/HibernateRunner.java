package org.example.posting.hibernate;

import org.example.posting.hibernate.dao.PaymentMethodRepository;
import org.example.posting.hibernate.dao.PaymentRepository;
import org.example.posting.hibernate.dao.SubscriptionRepository;
import org.example.posting.hibernate.entity.Sex;
import org.example.posting.hibernate.entity.Subscription;
import org.example.posting.hibernate.entity.Users;
import org.example.posting.hibernate.mapper.PaymentCreateMapper;
import org.example.posting.hibernate.mapper.PaymentMethodReadMapper;
import org.example.posting.hibernate.mapper.PaymentReadMapper;
import org.example.posting.hibernate.mapper.SubscriptionReadMapper;
import org.example.posting.hibernate.service.PaymentService;
import org.example.posting.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.Proxy;
import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = (Session) Proxy.newProxyInstance(
                     SessionFactory.class.getClassLoader(),
                     new Class[]{Session.class},
                     ((proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1)))){
            session.beginTransaction();

            var subscriptionReadMapper = new SubscriptionReadMapper();
            var paymentMethodReadMapper = new PaymentMethodReadMapper();
            var paymentReadMApper = new PaymentReadMapper(subscriptionReadMapper, paymentMethodReadMapper);
            var paymentMethodRepository = new PaymentMethodRepository(session);
            var subscriptionRepository = new SubscriptionRepository(session);
            var paymentCreateMapper = new PaymentCreateMapper(paymentMethodRepository, subscriptionRepository);

            var paymentRepository = new PaymentRepository(session);
            var paymentMethodRepository = new PaymentMethodRepository(session);
            var paymentService = new PaymentService(paymentRepository, paymentReadMApper, paymentCreateMapper);

            paymentService.findById(1L).ifPresent(System.out::println);

            session.getTransaction().commit();
        }
    }
}
