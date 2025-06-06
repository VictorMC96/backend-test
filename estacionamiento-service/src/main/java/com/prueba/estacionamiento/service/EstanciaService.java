package com.prueba.estacionamiento.service;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.prueba.estacionamiento.model.Estancia;
import com.prueba.estacionamiento.model.HibernateUtil;
import com.prueba.estacionamiento.model.TipoVehiculo;

@Service
public class EstanciaService {
	

    public Estancia registrarEntrada(String placa) {
        Estancia estancia = new Estancia();
        estancia.setPlaca(placa);
        estancia.setEntrada(Calendar.getInstance());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(estancia);
            tx.commit();
        }

        return estancia;
    }
    

    public Estancia registrarSalida(String placa) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Estancia estancia = session.createQuery(
                "FROM Estancia WHERE placa = :placa AND salida IS NULL", Estancia.class)
                .setParameter("placa", placa)
                .uniqueResult();

            if (estancia != null) {
                Transaction tx = session.beginTransaction();
                estancia.setSalida(Calendar.getInstance());
                session.merge(estancia);
                tx.commit();
            }

            return estancia;
        }
    }
    
    public void eliminarEstanciasOficiales() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<String> placasOficiales = session.createQuery(
                "SELECT v.placa FROM Vehiculo v WHERE v.tipo = :tipo", String.class)
                .setParameter("tipo", TipoVehiculo.OFICIAL)
                .list();

            Transaction tx = session.beginTransaction();
            for (String placa : placasOficiales) {
                List<Estancia> estancias = session.createQuery(
                    "FROM Estancia WHERE placa = :placa", Estancia.class)
                    .setParameter("placa", placa)
                    .list();

                for (Estancia e : estancias) {
                    session.remove(e);
                }
            }
            tx.commit();
        }
    }

}
