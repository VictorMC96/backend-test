package com.neology.parkinglot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.neology.parkinglot.entity.vehicles.Vehicle;
import com.neology.parkinglot.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ParkingLotDao {
	
	public Vehicle getCarByLicensePlate(String licensePlate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Vehicle.class, licensePlate);
        }
    }
	
	 public void saveCar(Vehicle vehicle) {
	        Transaction tx = null;
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            tx = session.beginTransaction();
	            session.saveOrUpdate(vehicle);
	            tx.commit();
	        } catch (Exception e) {
	            if (tx != null) tx.rollback();
	            e.printStackTrace();
	        }
	    }
}
