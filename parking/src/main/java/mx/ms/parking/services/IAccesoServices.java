package mx.ms.parking.services;

import mx.ms.parking.entity.Acceso;

import java.util.Calendar;

public interface IAccesoServices {
    public Acceso saveAndFlush(Acceso acceso);
    public Acceso findByIdVehiculoAndFhEntrada(Long idVehiculo, Calendar fhEntrada);
    public void eliminarAccesosOficiales();
}
