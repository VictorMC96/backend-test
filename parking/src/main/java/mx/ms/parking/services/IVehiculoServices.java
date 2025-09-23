package mx.ms.parking.services;

import mx.ms.parking.entity.Vehiculo;

import java.io.IOException;

public interface IVehiculoServices {
    public Vehiculo saveAndFlush(Vehiculo vehiculo);
    public Vehiculo findByPlacas(String placas);
    public void resetTiempoResidentes();
    public void generarInformePagosResidentesExcel(String nombreArchivo) throws IOException;
}
