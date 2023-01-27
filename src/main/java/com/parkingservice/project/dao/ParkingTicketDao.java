package com.parkingservice.project.dao;

import com.parkingservice.project.entity.ParkingTicket;

import java.util.Optional;

public interface ParkingTicketDao {
    ParkingTicket createTicket(ParkingTicket parkingTicket);

    Optional<ParkingTicket> getTicket(long ticketId);
}
