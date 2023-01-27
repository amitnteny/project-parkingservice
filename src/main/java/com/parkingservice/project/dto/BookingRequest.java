package com.parkingservice.project.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingservice.project.enums.VehicleSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest implements Serializable {

    @JsonProperty(value = "parking_lot_id")
    private long parkingLotId;

    @JsonProperty(value = "vehicle_size")
    private VehicleSize vehicleSize;

    @JsonProperty(value = "license_plate_number")
    private String licensePlateNumber;

    @JsonProperty(value = "booking_duration_in_minutes")
    private Long bookingDurationInMinutes;
}
