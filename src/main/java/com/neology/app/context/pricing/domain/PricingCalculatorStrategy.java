package com.neology.app.context.pricing.domain;

public interface PricingCalculatorStrategy {
    double getAmountByMinutes(int minutes);
}
