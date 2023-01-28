package com.parkingservice.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingservice.project.enums.VehicleSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetails {

    @JsonProperty("ticket_id")
    private long ticketId;
    @JsonProperty("parking_lot_id")
    private long parkingLotId;
    @JsonProperty("floor_number")
    private long floorNumber;
    @JsonProperty("slot_number")
    private long slotNumber;
    @JsonProperty("duration_in_minutes")
    private Long durationInMinutes;
    @JsonProperty("license_plate_number")
    private String licensePlateNumber;
    @JsonProperty("parking_slot_size")
    private VehicleSize parkingSlotSize;
    @JsonProperty("ticket_price")
    private BigDecimal ticketPrice;
}
