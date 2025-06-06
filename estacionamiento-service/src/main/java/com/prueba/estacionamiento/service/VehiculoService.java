package com.prueba.estacionamiento.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.prueba.estacionamiento.model.HibernateUtil;
import com.prueba.estacionamiento.model.TipoVehiculo;
import com.prueba.estacionamiento.model.Vehiculo;

@Service
public class VehiculoService {
	
	 public void registrarVehiculo(String placa, TipoVehiculo tipo) {
	        Vehiculo vehiculo = new Vehiculo();
	        vehiculo.setPlaca(placa);
	        vehiculo.setTipo(tipo);
	        vehiculo.setTiempoAcumulados(0L); // Solo para residentes

	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            Transaction tx = session.beginTransaction();
	            session.persist(vehiculo);
	            tx.commit();
	        }
	    }

	  public TipoVehiculo obtenerTipoPorPlaca(String placa) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            Vehiculo vehiculo = session.get(Vehiculo.class, placa);
	            if (vehiculo == null) {
	                throw new IllegalArgumentException("Vehículo no encontrado: " + placa);
	            }
	            return vehiculo.getTipo(); 
	        }
	    }
	  
	  public void acumularTiempo(String placa, long minutos) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            Transaction tx = session.beginTransaction();
	            Vehiculo vehiculo = session.get(Vehiculo.class, placa);
	            if (vehiculo != null && vehiculo.getTipo() == TipoVehiculo.RESIDENTE) {
	                vehiculo.setTiempoAcumulados(vehiculo.getTiempoAcumulados() + minutos);
	                session.merge(vehiculo);
	            }
	            tx.commit();
	        }
	    }
	  
	  public void reiniciarTiempoResidentes() {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            Transaction tx = session.beginTransaction();
	            List<Vehiculo> residentes = session.createQuery(
	                "FROM Vehiculo WHERE tipo = :tipo", Vehiculo.class)
	                .setParameter("tipo", TipoVehiculo.RESIDENTE)
	                .list();

	            for (Vehiculo v : residentes) {
	                v.setTiempoAcumulados(0L);
	                session.merge(v);
	            }

	            tx.commit();
	        }
	    }

}
