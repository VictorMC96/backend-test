package com.neology.parkinglot.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import com.neology.parkinglot.dao.ParkingLotDao;
import com.neology.parkinglot.entity.vehicles.ConstantEnum;
import com.neology.parkinglot.entity.vehicles.Vehicle;

public class ReportGenerator {
	private static final DecimalFormat FORMAT = new DecimalFormat(ConstantEnum.FORMAT.getValue());
	
    public static void generarInformeResidentes(String fileName) {
        ParkingLotDao carDao = new ParkingLotDao(); 
        List<Vehicle> resident = carDao.getResidentCars();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar");
            writer.newLine();

            for (Vehicle vehicle : resident) {
                int minutos = vehicle.getTotalTime();
                double cantidad = minutos * Integer.valueOf(ConstantEnum.MONTAMOUNT.getValue());

                writer.write(vehicle.getVpNumber() + "\t" +
                             minutos + "\t\t\t\t" +
                             FORMAT.format(cantidad));
                writer.newLine();
            }

            System.out.println("Complete: " + fileName);

        } catch (IOException e) {
            System.err.println("Failed: " + e.getMessage());
        }
    }
}
