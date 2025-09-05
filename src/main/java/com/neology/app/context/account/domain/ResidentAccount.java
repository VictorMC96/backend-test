package com.neology.app.context.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neology.app.context.vehicle.domain.Vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resident_accounts")
@Getter
@Setter
@NoArgsConstructor
public class ResidentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int accumulatedMinutes;

    @OneToOne
    @JoinColumn(name = "plate", nullable = false, unique = true)
    @JsonIgnore
    private Vehicle vehicle;

    public ResidentAccount(int accumulatedMinutes, Vehicle vehicle) {
        this.accumulatedMinutes = accumulatedMinutes;
        this.vehicle = vehicle;
    }

}
