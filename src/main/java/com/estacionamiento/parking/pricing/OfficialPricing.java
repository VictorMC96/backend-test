package com.estacionamiento.parking.pricing;

public class OfficialPricing implements PricingStrategy {
    @Override public double amountForMinutes(int minutes) {
        return 0.0;
    }
}