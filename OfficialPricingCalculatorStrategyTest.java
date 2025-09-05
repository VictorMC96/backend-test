package com.neology.app.context.pricing.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfficialPricingCalculatorStrategyTest {

    private final OfficialPricingCalculatorStrategy strategy = new OfficialPricingCalculatorStrategy();

    @Test
    void shouldReturnZeroAmountForAnyMinutes() {
        assertEquals(0.0, strategy.getAmountByMinutes(60));
        assertEquals(0.0, strategy.getAmountByMinutes(0));
        assertEquals(0.0, strategy.getAmountByMinutes(1000));
    }

    @Test
    void accumulateMinuteShouldDoNothing() {
        assertDoesNotThrow(() -> strategy.accumulateMinute("any-plate", 120));
    }
}
