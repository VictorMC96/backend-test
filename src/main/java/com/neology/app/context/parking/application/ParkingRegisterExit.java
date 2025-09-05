package com.neology.app.context.parking.application;

import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neology.app.context.account.domain.ResidentAccountRepository;
import com.neology.app.context.amount.domain.Amount;
import com.neology.app.context.parking.domain.ParkingRegisterRepository;
import com.neology.app.context.pricing.domain.OfficialPricingCalculatorStrategy;
import com.neology.app.context.pricing.domain.PricingCalculatorStrategy;
import com.neology.app.context.pricing.domain.ResidentPricingCalculatorStrategy;
import com.neology.app.context.shaed.DateUtils;
import com.neology.app.context.vehicle.domain.Vehicle;
import com.neology.app.context.vehicle.domain.VehicleRepository;
import com.neology.app.context.vehicle.domain.VehicleType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParkingRegisterExit {

    private final ParkingRegisterRepository parkingRegisterRepository;
    private final VehicleRepository vehicleRepository;
    private final ResidentAccountRepository residentAccountRepository;

    @Transactional
    public Amount run(String plate) {
        Vehicle vehicle = this.vehicleRepository.findById(plate).orElseThrow(
                () -> new IllegalArgumentException("the plate not exists"));

        var register = this.parkingRegisterRepository.findByPlateAndIsOpenTrue(plate)
                .orElseThrow(() -> new IllegalArgumentException("The entry not exist"));
        register.setExitAt(Calendar.getInstance());
        register.setOpen(false);

        this.parkingRegisterRepository.save(register);
        var pricingCalculatorStrategy = this.getPricingCalculatorStrategy(vehicle.getType());
        var minutes = DateUtils.difEnMinutes(register.getEntryAt(), register.getExitAt());
        pricingCalculatorStrategy.accumulateMinute(plate, minutes);

        return new Amount(plate, minutes, pricingCalculatorStrategy.getAmountByMinutes(minutes));
    }

    private PricingCalculatorStrategy getPricingCalculatorStrategy(VehicleType type) {
        return switch (type) {
            case RESIDENT -> new ResidentPricingCalculatorStrategy(this.residentAccountRepository);
            case OFFICIAL -> new OfficialPricingCalculatorStrategy();
        };
    }

}
