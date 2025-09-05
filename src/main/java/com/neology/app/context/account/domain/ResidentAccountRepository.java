package com.neology.app.context.account.domain;

import java.util.List;
import java.util.Optional;

public interface ResidentAccountRepository {
    ResidentAccount save(ResidentAccount account);

    Optional<ResidentAccount> findByVehiclePlate(String plate);

    List<ResidentAccount> findAll();
}
