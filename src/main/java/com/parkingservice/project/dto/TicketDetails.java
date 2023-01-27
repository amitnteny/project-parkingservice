package com.parkingservice.project.dto;

import com.parkingservice.project.enums.VehicleSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetails {

    private long ticketId;
    private long parkingLotId;
    private long floorNumber;
    private long slotNumber;
    private Long durationInMinutes;
    private String licensePlateNumber;
    private VehicleSize parkingSlotSize;
    private BigDecimal ticketPrice;
}
