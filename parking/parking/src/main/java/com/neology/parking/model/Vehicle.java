package com.neology.parking.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    public Vehicle() {}

    public Vehicle(String plate, VehicleType type) {
        this.plate = plate;
        this.type = type;
        this.setAccumulatedMinutes(0);
    }
    
	
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String plate;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    // acumulado en minutos (solo para residentes)
    private Integer accumulatedMinutes = 0;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stay> stays = new ArrayList<>();

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPlate() {
		return plate;
	}
	
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	public VehicleType getType() {
		return type;
	}
	
	public void setType(VehicleType type) {
		this.type = type;
	}
	
	public List<Stay> getStays() {
		return stays;
	}
	
	public void setStays(List<Stay> stays) {
		this.stays = stays;
	}

	public Integer getAccumulatedMinutes() {
		return accumulatedMinutes;
	}

	public void setAccumulatedMinutes(Integer accumulatedMinutes) {
		this.accumulatedMinutes = accumulatedMinutes;
	}
	
}
