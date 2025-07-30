package com.neology.parkinglot.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.neology.parkinglot.entity.vehicles.Vehicle;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Vehicle.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al construir SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}