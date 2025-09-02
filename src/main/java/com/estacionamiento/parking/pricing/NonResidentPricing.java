package com.estacionamiento.parking.pricing;

public class NonResidentPricing implements PricingStrategy {
    private static final double RATE_PER_MINUTE = 0.5;

    @Override public double amountForMinutes(int minutes) {
        return minutes * RATE_PER_MINUTE;
    }
}