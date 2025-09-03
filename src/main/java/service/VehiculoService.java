package service;

import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.VehiculoNoResidenteRepository;
import repository.VehiculoOficialRepository;
import repository.VehiculoRepository;
import repository.VehiculoResidenteRepository;

import java.util.Calendar;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VehiculoOficialRepository oficialRepository;

    @Autowired
    private VehiculoResidenteRepository residenteRepository;

    @Autowired
    private VehiculoNoResidenteRepository noResidenteRepository;


    public Vehiculo saveVehiculo(Vehiculo vehiculo){
        if((vehiculo.getPlaca() != null || !vehiculo.getPlaca().isEmpty())
                && (vehiculo.getTipo() != null || !vehiculo.getTipo().isEmpty())){
            return vehiculoRepository.save(vehiculo);
        }
        return vehiculo;
    }

    public String registroEntrada(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa);
        if(vehiculo == null){
            return "No se encontró la placa";
        }

        Estancia estancia = new Estancia();
        estancia.setHoraInicial(today());
        vehiculo.getEstancias().add(estancia);
        vehiculoRepository.save(vehiculo);
        return "Se registró correctamente la hora de entrada!";
    }

    public String registroSalida(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa);
        if(vehiculo == null){
            return "No se encontró la placa";
        }
        if(vehiculo.getTipo().equals("Oficial")){
            VehiculoOficial oficial = (VehiculoOficial) vehiculo;
            oficial.getEstancias().get(oficial.getEstancias().size()-1).setHoraFinal(today());
            oficialRepository.save(oficial);
        }
        if(vehiculo.getTipo().equals("Residente")){
            VehiculoResidente residente = (VehiculoResidente) vehiculo;
            residente.getEstancias().get(residente.getEstancias().size()-1).setHoraFinal(today());
            residente.setTiempoAcumulado(residente.getTiempoAcumulado() + difEnMinutos(
                    residente.getEstancias().get(residente.getEstancias().size()-1).getHoraInicial(),
                    residente.getEstancias().get(residente.getEstancias().size()-1).getHoraFinal()));
            residenteRepository.save(residente);
            return "Tiempo acumulado: "+ String.valueOf(residente.getTiempoAcumulado());
        }
        if(vehiculo.getTipo().equals("No Residente")){
            VehiculoNoResidente noResidente = (VehiculoNoResidente) vehiculo;
            noResidente.getEstancias().get(noResidente.getEstancias().size()-1).setHoraFinal(today());
            noResidente.setTiempoAcumulado(difEnMinutos(
                    noResidente.getEstancias().get(noResidente.getEstancias().size()-1).getHoraInicial(),
                    noResidente.getEstancias().get(noResidente.getEstancias().size()-1).getHoraFinal()));
            noResidenteRepository.save(noResidente);
            return "Importe a pagar: "+ String.valueOf(noResidente.getTiempoAcumulado()*0.5);
        }

        return "Se registró correctamente la hora de salida!";
    }

    public String comenzarMes(){
        oficialRepository.eliminarEstanciasEnVehiculosOficiales();
        residenteRepository.inicializarTiempoAcumulado();
        return "Actualización exitosa!";
    }

    private static Calendar today(){
        Calendar unaFecha; // para almacenar una fecha
        unaFecha = Calendar.getInstance(); // obtiene la fecha actual
        return unaFecha;
    }

    /** Obtiene la diferencia en minutos entre dos fechas
     * @param inicial fecha inicial
     * @param finalDate fecha final
     * @return diferencia final-inicial en minutos
     */
    private static int difEnMinutos(Calendar inicial, Calendar finalDate) {
        return (int) ((finalDate.getTimeInMillis()-inicial.getTimeInMillis())/(60 * 1000));
    }
}
