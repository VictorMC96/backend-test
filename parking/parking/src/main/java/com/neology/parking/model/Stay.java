package com.neology.parking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "stays")
public class Stay {
	
    public Stay() {}
    
    public Stay(Vehicle vehicle, Calendar entryTime) {
        this.vehicle = vehicle;
        this.entryTime = entryTime;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar exitTime;

    private Integer durationMinutes;

    @Column(precision = 10, scale = 2)
    private BigDecimal amountPaid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Calendar getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Calendar entryTime) {
		this.entryTime = entryTime;
	}

	public Calendar getExitTime() {
		return exitTime;
	}

	public void setExitTime(Calendar exitTime) {
		this.exitTime = exitTime;
	}

	public Integer getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}
	
}
