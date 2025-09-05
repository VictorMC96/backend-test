package com.neology.app.context.account.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neology.app.context.account.domain.ResidentAccount;
import com.neology.app.context.account.domain.ResidentAccountRepository;

public interface H2ResidentAccountRepository extends JpaRepository<ResidentAccount, Long>, ResidentAccountRepository {
}
