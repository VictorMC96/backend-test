package com.neology.parkinglot.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.neology.parkinglot.entity.vehicles.Vehicle;
import com.neology.parkinglot.util.HibernateUtil;
import org.hibernate.query.Query;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ParkingLotDao {
	
	public Vehicle getCarByLicensePlate(String licensePlate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Vehicle.class, licensePlate);
        }
    }
	
	 public void saveVehicle(Vehicle vehicle) {
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
	 
	 public List<Vehicle> getResidentCars() {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            String hql = "FROM Car WHERE type = :type";
	            Query<Vehicle> query = session.createQuery(hql, Vehicle.class);
	            query.setParameter("type", "RESIDENT");
	            return query.list();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return List.of();
	        }
	    }
}
