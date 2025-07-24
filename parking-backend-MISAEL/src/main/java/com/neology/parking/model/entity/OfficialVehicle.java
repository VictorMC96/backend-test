package com.neology.parking.model.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("OFFICIAL")
public class OfficialVehicle extends Vehicle {}
