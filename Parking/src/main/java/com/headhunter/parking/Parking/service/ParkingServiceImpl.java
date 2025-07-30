package com.headhunter.parking.Parking.service;

import com.headhunter.parking.Parking.constants.CatTypeVehicleENUM;
import com.headhunter.parking.Parking.dto.RespuestaServicioDto;
import com.headhunter.parking.Parking.repository.dao.CatTypeVehiRepository;
import com.headhunter.parking.Parking.repository.dao.VehicleRepository;
import com.headhunter.parking.Parking.repository.entity.CatTypeVehicle;
import com.headhunter.parking.Parking.repository.entity.VehicleEnt;
import com.headhunter.parking.Parking.utils.RespuestaUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private CatTypeVehiRepository catTypeVehiRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public RespuestaServicioDto addVehicle(String numberPlate, CatTypeVehicleENUM typeVehicle, LocalDateTime dateInit, LocalDateTime dateExit) {
        insertVehicle(numberPlate, typeVehicle, dateInit, dateExit);
        return RespuestaUtils.procesaRespuesta("EXITO", 0);
    }

    @Override
    public RespuestaServicioDto generateFile(String fileName) {
        List<VehicleEnt> listResi = vehicleRepository.findAll();
        Path ruta = Paths.get("C:\\Projects\\" + fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(ruta)) {
            // Encabezado
            writer.write("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar");
            writer.newLine();

            // Escribir registros usando stream
            listResi.stream()
                    .map(r -> String.format("%s\t%d\t%.2f",
                            r.getNumberPlate(),
                            r.getTimeAcum(),
                            r.getToPay()))
                    .forEach(linea -> {
                        try {
                            writer.write(linea);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException("Error al escribir línea: " + linea, e);
                        }
                    });

            System.out.println("Archivo generado en: " + ruta.toString());

        } catch (IOException e) {
            System.err.println("Error al generar archivo: " + e.getMessage());
        }
        return RespuestaUtils.procesaRespuesta("EXITO", 0);
    }

    @Override
    public RespuestaServicioDto restartResident() {
        List<VehicleEnt> listResi = vehicleRepository.findByType(CatTypeVehicleENUM.RESIDENT.getId());
        List<VehicleEnt> listMod = new ArrayList<>();
        listResi.stream().forEach(vehi -> {
            vehi.setTimeAcum(0);
            vehi.setDateExit(null);
            vehi.setDateInit(null);
            listMod.add(vehi);
        });
        vehicleRepository.saveAll(listMod);
        return RespuestaUtils.procesaRespuesta("EXITO", 0);
    }

    private void insertVehicle(String numberPlate, CatTypeVehicleENUM typeVehicle, LocalDateTime dateInit, LocalDateTime dateExit) {
        VehicleEnt vehiEnt = new VehicleEnt();
        Optional<VehicleEnt> vehiOPT = vehicleRepository.findByNumberPlate(numberPlate);
        if (vehiOPT.isEmpty()) {
            if (vehiEnt.getIdType() == null && (dateInit != null || dateExit != null)) {
                CatTypeVehicle type = catTypeVehiRepository.getTypeVehicleByCode(CatTypeVehicleENUM.NOTRESIDENT.getId()).orElseThrow(() -> new EntityNotFoundException("Tipo de vehículo no encontrado"));
                vehiEnt.setIdType(type);
            } else {
                CatTypeVehicle type = catTypeVehiRepository.getTypeVehicleByCode(typeVehicle.getId()).orElseThrow(() -> new EntityNotFoundException("Tipo de vehículo no encontrado"));
                vehiEnt.setIdType(type);
            }
        } else {
            vehiEnt = vehiOPT.get();
        }
        vehiEnt.setNumberPlate(numberPlate);
        if (dateInit != null) {
            vehiEnt.setDateInit(dateInit);
        }
        if (dateExit != null) {
            vehiEnt.setDateExit(dateExit);
        }
        vehiEnt.setTimeAcum(calculateRate(vehiEnt));
        vehiEnt.setToPay(evaluateTime(vehiEnt.getTimeAcum(), CatTypeVehicleENUM.fromId(vehiEnt.getIdType().getCode())));
        vehicleRepository.save(vehiEnt);
    }

    private Integer calculateRate(VehicleEnt vehi) {
        Long minRate = 0L;
        if (vehi.getDateExit() != null && vehi.getDateInit() != null) {
            minRate = Duration.between(vehi.getDateInit(), vehi.getDateExit()).toMinutes();
        }
        return minRate.intValue();
    }

    public double evaluateTime(int minutos, CatTypeVehicleENUM typeVehicle) {
        double tarifaPorMinuto = 0;
        switch (typeVehicle) {
            case RESIDENT:
                tarifaPorMinuto = 0.05;
                break;
            case OFFICIAL:

                break;
            case NOTRESIDENT:
                tarifaPorMinuto = 0.50;
                break;
            default:
                System.out.println("Tipo de vehículo desconocido.");
        }
        return minutos * tarifaPorMinuto;
    }
}
