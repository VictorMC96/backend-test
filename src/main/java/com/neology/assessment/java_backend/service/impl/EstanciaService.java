package com.neology.assessment.java_backend.service.impl;

import com.neology.assessment.java_backend.controller.BusinessMessage;
import com.neology.assessment.java_backend.dto.TipoVehiculoEnum;
import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.dto.response.EstanciaResumen;
import com.neology.assessment.java_backend.entity.Estancia;
import com.neology.assessment.java_backend.entity.TipoVehiculo;
import com.neology.assessment.java_backend.entity.Vehiculo;
import com.neology.assessment.java_backend.exception.BusinessException;
import com.neology.assessment.java_backend.repository.EstanciaRepository;
import com.neology.assessment.java_backend.repository.VehiculoRepository;
import com.neology.assessment.java_backend.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstanciaService implements com.neology.assessment.java_backend.service.EstanciaService {

    @Autowired
    private EstanciaRepository estanciaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    public Estancia registarEntrada(VehiculoRequest vehiculoRequest) throws BusinessException {
            Estancia estancia = null;
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(vehiculoRequest.getPlaca()).orElse(null);
        if (vehiculo != null) {
            Estancia estancia2 = estanciaRepository.findByVehiculoAndFechaSalidaIsNull(vehiculo).orElse(null);
            if (estancia2 != null) {
                throw new BusinessException(null, BusinessMessage.ESTANCIA_ACTIVA);
            }
            else {
                Estancia estancia1 = new Estancia();
                estancia1.setVehiculo(vehiculo);
                estancia1.setFechaEntrada(Calendar.getInstance());
                estancia1.setActivo(true);
                estancia1.setMesActual(true);
                estancia = estanciaRepository.save(estancia1);

            }
        }
        else {
            //vehiculo no residente
            Estancia estancia1 = new Estancia();
            Vehiculo vehiculo1 = new Vehiculo();
            vehiculo1.setPlaca(vehiculoRequest.getPlaca());
            vehiculo1.setFechaRegistro(Calendar.getInstance());
            TipoVehiculo tipoVehiculo = new TipoVehiculo();
            tipoVehiculo.setId(TipoVehiculoEnum.NO_RESIDENTE.getCodigo());
            vehiculo1.setTipoVehiculo(tipoVehiculo);
            estancia1.setVehiculo(vehiculo);
            estancia1.setFechaEntrada(Calendar.getInstance());
            estancia1.setActivo(true);
            estancia1.setMesActual(true);
            estancia1.setVehiculo(vehiculo1);
            estancia = estanciaRepository.save(estancia1);
        }

        return estancia;
    }

    @Override
    public Estancia registrarSalida(VehiculoRequest vehiculoRequest) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(vehiculoRequest.getPlaca()).orElse(null);
        Estancia estancia = null;
        if (vehiculo != null) {
            Estancia estancia2 = estanciaRepository.findByVehiculoAndFechaSalidaIsNull(vehiculo).orElse(null);
            if (estancia2 != null) {
                estancia2.setFechaSalida(Calendar.getInstance());
                estancia2.setTiempoMinutos(DateUtil.difEnMinutos(estancia2.getFechaEntrada(), estancia2.getFechaSalida()) + 1);
                estancia2.setMesActual(true);
                estancia2.setMontoPagar(vehiculo.getTipoVehiculo().getTarifa() * estancia2.getTiempoMinutos());
                estancia = estanciaRepository.save(estancia2);
            }
            else {
                throw new BusinessException(null, BusinessMessage.SIN_ESTANCIA_ACTIVA);
            }
        }
        else{
            throw new BusinessException(null, BusinessMessage.SIN_ESTANCIA_ACTIVA);
        }

        return estancia;
    }


    @Override
    public void iniciarMes() {
        List<Estancia> estanciasResidentes = estanciaRepository.findByVehiculoTipoVehiculoIdAndMesActualTrue(TipoVehiculoEnum.RESIDENTE.getCodigo());
        estanciasResidentes.forEach(estancia -> {
            estancia.setMesActual(false);
        });
        estanciaRepository.saveAll(estanciasResidentes);

        List<Estancia> estanciasOficiales = estanciaRepository.findByVehiculoTipoVehiculoIdAndMesActualTrue(TipoVehiculoEnum.OFICIAL.getCodigo());
        estanciasOficiales.forEach(estancia -> {
            estancia.setActivo(false);
            estancia.setMesActual(false);
        });
        estanciaRepository.saveAll(estanciasOficiales);

        List<Estancia> estanciasNoResidentes = estanciaRepository.findByVehiculoTipoVehiculoIdAndMesActualTrue(TipoVehiculoEnum.NO_RESIDENTE.getCodigo());
        estanciasNoResidentes.forEach(estancia -> {
            estancia.setMesActual(false);
        });
        estanciaRepository.saveAll(estanciasOficiales);
    }

    @Override
    public ByteArrayResource generarReporteResidentes() throws IOException {
        List<Estancia> estancias = estanciaRepository.findByVehiculoTipoVehiculoIdAndMesActualTrue(TipoVehiculoEnum.RESIDENTE.getCodigo());


        List<EstanciaResumen> estanciaResumenList = estancias.stream()
                .collect(Collectors.groupingBy(estancia -> estancia.getVehiculo().getPlaca())).entrySet().stream()
                .map(entry -> {
                    String placa = entry.getKey();
                    Integer tiempoTotalMinutos = entry.getValue().stream()
                            .mapToInt(Estancia::getTiempoMinutos)  // Sumamos tiempoMinutos de cada estancia
                            .sum();
                    Double montoTotalPagar = entry.getValue().stream()
                            .mapToDouble(Estancia::getMontoPagar)  // Sumamos tiempo Total a pagar de cada estancia
                            .sum();
                    return new EstanciaResumen(placa, tiempoTotalMinutos, montoTotalPagar);  // Creamos un nuevo objeto con el resultado
                })
                .toList();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(baos);
        writer.write("Num placa, Tiempo estacionado(min), cantidad a pagar");
        writer.write("\n");
        for (EstanciaResumen estanciaResumen : estanciaResumenList) {
            writer.write(estanciaResumen.getPlaca() + "," + estanciaResumen.getTiempoTotalMinutos()+","+estanciaResumen.getMontoTotalPagar());
            writer.write("\n");
        }
        writer.flush();
        baos.flush();
       return new ByteArrayResource(baos.toByteArray());

    }

}
