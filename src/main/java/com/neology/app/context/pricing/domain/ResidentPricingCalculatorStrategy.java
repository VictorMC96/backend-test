package com.neology.app.context.pricing.domain;

import com.neology.app.context.account.domain.ResidentAccountRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResidentPricingCalculatorStrategy implements PricingCalculatorStrategy {
    private ResidentAccountRepository residentAccountRepository;

    @Override
    public double getAmountByMinutes(int minutes) {
        return 0.0;
    }

    @Override
    public void accumulateMinute(String plate, int minutes) {
        var account = this.residentAccountRepository.findByVehiclePlate(plate)
                .orElseThrow(() -> new IllegalArgumentException("Resident account not found"));

        account.setAccumulatedMinutes(account.getAccumulatedMinutes() + minutes);
        this.residentAccountRepository.save(account);
    }

    public static double getAmountForAccumulatedMinutes(int minutes) {
        return minutes * 0.05;
    }

}
