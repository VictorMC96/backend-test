package com.neology.app.context.pricing.domain;

public class NonResidentPricingCalculatorStrategy implements PricingCalculatorStrategy {

    @Override
    public double getAmountByMinutes(int minutes) {

        return minutes * 0.5;
    }
}
