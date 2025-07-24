package com.parking.dto;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

public record Car(@Id String plate,
                  String type,
                  Boolean insideParkingLot,
                  Integer stayingMinutes,
                  Timestamp startDateTime,
                  Timestamp endDateTime) {

}
