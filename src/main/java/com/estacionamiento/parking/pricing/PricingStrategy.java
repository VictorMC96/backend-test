package com.estacionamiento.parking.pricing;

public interface PricingStrategy {

    double amountForMinutes(int minutes);

    default void onExitSideEffects(String plate, int minutes) {}
}