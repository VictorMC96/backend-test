package com.neology.app.context.pricing.domain;

import com.neology.app.context.account.domain.ResidentAccount;
import com.neology.app.context.account.domain.ResidentAccountRepository;
import com.neology.app.context.vehicle.domain.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentPricingCalculatorStrategyTest {

    @Mock
    private ResidentAccountRepository residentAccountRepository;

    private ResidentPricingCalculatorStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ResidentPricingCalculatorStrategy(residentAccountRepository);
    }

    @Test
    void shouldCalculateCorrectAmountForGivenMinutes() {
        assertEquals(6.0, strategy.getAmountByMinutes(120));
        assertEquals(0.0, strategy.getAmountByMinutes(0));
    }

    @Test
    void shouldCalculateCorrectAmountForAccumulatedMinutesStatically() {
        assertEquals(5.0, ResidentPricingCalculatorStrategy.getAmountForAccumulatedMinutes(100));
        assertEquals(0.0, ResidentPricingCalculatorStrategy.getAmountForAccumulatedMinutes(0));
    }

    @Test
    void shouldAccumulateMinutesForExistingResident() {
        String plate = "R123";
        int initialMinutes = 50;
        int minutesToAdd = 30;
        Vehicle vehicle = new Vehicle(plate, null, null);
        ResidentAccount account = new ResidentAccount(initialMinutes, vehicle);

        when(residentAccountRepository.findByVehiclePlate(plate)).thenReturn(Optional.of(account));

        strategy.accumulateMinute(plate, minutesToAdd);

        assertEquals(initialMinutes + minutesToAdd, account.getAccumulatedMinutes());
        verify(residentAccountRepository, times(1)).save(account);
    }

    @Test
    void accumulateMinuteShouldThrowExceptionIfResidentNotFound() {
        String plate = "R404";
        when(residentAccountRepository.findByVehiclePlate(plate)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> strategy.accumulateMinute(plate, 10));

        assertEquals("The resident account not exist", exception.getMessage());
        verify(residentAccountRepository, never()).save(any());
    }
}
