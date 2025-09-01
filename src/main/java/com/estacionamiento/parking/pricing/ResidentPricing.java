package com.estacionamiento.parking.pricing;

import com.estacionamiento.parking.domain.ResidentAccount;
import com.estacionamiento.parking.domain.Vehicle;
import com.estacionamiento.parking.repository.ResidentAccountRepository;
import com.estacionamiento.parking.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResidentPricing implements PricingStrategy {

    private final VehicleRepository vehicleRepo;
    private final ResidentAccountRepository residentRepo;

    private static final double RATE_PER_MINUTE = 0.05;

    @Override public double amountForMinutes(int minutes) {
        return 0.0;
    }

    @Override public void onExitSideEffects(String plate, int minutes) {
        Vehicle v = vehicleRepo.findById(plate).orElseThrow();
        var account = residentRepo.findByVehicle(v)
                .orElseThrow(() -> new IllegalStateException("Cuenta de residente inexistente para " + plate));
        account.setAccumulatedMinutes(account.getAccumulatedMinutes() + minutes);
        residentRepo.save(account);
    }

    public static double amountForAccumulatedMinutes(int minutes) {
        return minutes * RATE_PER_MINUTE;
    }
}