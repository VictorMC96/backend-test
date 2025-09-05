package com.neology.app.context.parking.application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.neology.app.context.account.domain.ResidentAccount;
import com.neology.app.context.account.domain.ResidentAccountRepository;
import com.neology.app.context.pricing.domain.ResidentPricingCalculatorStrategy;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingRegisterResidentReport {

    private final ResidentAccountRepository residentAccountRepository;

    public String run(String path) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.println("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar");

            List<ResidentAccount> accounts = residentAccountRepository.findAll();

            for (ResidentAccount a : accounts) {
                String plate = a.getVehicle().getPlate();
                int minutes = a.getAccumulatedMinutes();
                double amount = ResidentPricingCalculatorStrategy.getAmountForAccumulatedMinutes(minutes);
                out.printf("%s\t%d\t%.2f%n", plate, minutes, amount);
            }
        }

        return path;
    }
}
