package com.prueba.estacionamiento.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.prueba.estacionamiento.model.HibernateUtil;
import com.prueba.estacionamiento.model.Pago;
import com.prueba.estacionamiento.model.TipoVehiculo;
import com.prueba.estacionamiento.model.Vehiculo;

@Service
public class PagoService {

	public Pago registrarPago(String placa, TipoVehiculo tipo, BigDecimal monto) {
        Pago pago = new Pago();
        pago.setPlaca(placa);
        pago.setTipo(tipo);
        pago.setMonto(monto);
        pago.setFechaPago(Calendar.getInstance());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(pago);
            tx.commit();
        }

        return pago;
    }
	
	public void generarInformeResidentes(String nombreArchivo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Vehiculo> residentes = session.createQuery(
                    "FROM Vehiculo WHERE tipo = :tipo", Vehiculo.class)
                    .setParameter("tipo", TipoVehiculo.RESIDENTE)
                    .list();

            try (PrintWriter writer = new PrintWriter(nombreArchivo)) {
                writer.println("Placa,Minutos Acumulados,Total a Pagar");
                for (Vehiculo v : residentes) {
                    long minutos = v.getTiempoAcumulados();
                    BigDecimal monto = BigDecimal.valueOf(minutos).multiply(new BigDecimal("0.05"));
                    writer.printf("%s,%d,%.2f%n", v.getPlaca(), minutos, monto);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al generar informe", e);
        }
    }
}
