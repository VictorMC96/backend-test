package com.neology.app.context.pricing.domain;

public class OfficialPricingCalculatorStrategy implements PricingCalculatorStrategy {

    @Override
    public double getAmountByMinutes(int minutes) {
        return 0.0;
    }

}
