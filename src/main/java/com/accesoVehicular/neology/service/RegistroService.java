package com.accesoVehicular.neology.service;

import com.accesoVehicular.neology.dto.RegistroVehiculo;
import com.accesoVehicular.neology.model.Registro;
import com.accesoVehicular.neology.model.TipoVehiculo;
import com.accesoVehicular.neology.model.Vehiculo;
import com.accesoVehicular.neology.repository.IRegistroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

@Service
public class RegistroService {

    private final VehiculoService vehiculoService;
    private final TipoVehiculoService tipoVehiculoService;
    private final IRegistroRepository registroRepository;
    private final AcumuladoService acumuladoService;

    public RegistroService(VehiculoService vehiculoService, TipoVehiculoService tipoVehiculoService, IRegistroRepository registroRepository, AcumuladoService acumuladoService) {
        this.vehiculoService = vehiculoService;
        this.tipoVehiculoService = tipoVehiculoService;
        this.registroRepository = registroRepository;
        this.acumuladoService = acumuladoService;
    }

    //Registrar entrada
    @Transactional
    public boolean registrarEntrada(String placa) throws Exception {

        //Validamos que la placa no tenga una entrada sin una salida registrada
        Optional<Registro> registroExistente = this.registroRepository.findByPlacaAndFechaSalidaIsNull(placa);
        if(registroExistente.isPresent()){
            throw new Exception("Placa ya tiene una entrada sin salida registrada");
        }

        Vehiculo vehiculo = this.vehiculoService.getByPlaca(placa);
        //Validamos que el vehiculo exista
        //Si no existe, lo agregamos como visitante
        if (vehiculo == null) {
            TipoVehiculo tipoVehiculo = this.tipoVehiculoService.findByNombre("Visitante");
            RegistroVehiculo registroVehiculo = new RegistroVehiculo(placa, tipoVehiculo);
            try {
                vehiculo = this.vehiculoService.registrarVehiculo(registroVehiculo);
            } catch (Exception e) {
                System.out.println("Error al registrar el vehiculo: " + e.getMessage());
                return false;
            }

        }

        //Se agrega el registro de entrada
        Registro registro = new Registro(vehiculo, Calendar.getInstance());
        this.registroRepository.saveAndFlush(registro);
        return true;
    }

    @Transactional
    public BigDecimal registrarSalida(String placa) throws Exception {

        Optional<Registro> registroExistente = this.registroRepository.findByPlacaAndFechaSalidaIsNull(placa);
        if(!registroExistente.isPresent()){
            throw new Exception("No hay una entrada registrada para la placa: " + placa);
        }
        Registro registro = registroExistente.get();
        registro.setFechaHoraSalida(Calendar.getInstance());

        Long diferencia = this.difEnMinutos(registro.getFechaHoraEntrada(), registro.getFechaHoraSalida());
        Double tarifa = registro.getVehiculo().getTipoVehiculo().getTarifa();
        BigDecimal totalAPagar = BigDecimal.valueOf(diferencia * tarifa);

        //Calculamos y asignamos el importe a pagar
        registro.setImporte( totalAPagar );
        this.registroRepository.saveAndFlush(registro);

        if(registro.getVehiculo().getTipoVehiculo().getNombre().equals("RESIDENTE")) {
            //Si es residente, se registra el acumulado y el total a pagar por la salida es 0;
            this.acumuladoService.registrarAcumulado(registro.getVehiculo(), diferencia);
            totalAPagar = BigDecimal.valueOf(0L);
        }

        return totalAPagar;
    }

    //Usando el metodo propuesto...
    private Long difEnMinutos(Calendar inicio, Calendar fin) {
        long diffMillis = fin.getTimeInMillis() - inicio.getTimeInMillis();
        return (diffMillis / (60 * 1000));
    }
}
