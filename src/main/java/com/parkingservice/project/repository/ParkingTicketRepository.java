package com.parkingservice.project.repository;

import com.parkingservice.project.entity.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

}
