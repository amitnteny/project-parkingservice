package com.parkingservice.project.dao;

import com.parkingservice.project.entity.ParkingTicket;
import com.parkingservice.project.repository.ParkingTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ParkingTicketDaoImpl implements ParkingTicketDao {

    @Autowired
    private ParkingTicketRepository repository;

    @Override
    public ParkingTicket createTicket(ParkingTicket parkingTicket) {
        return repository.save(parkingTicket);
    }

    @Override
    public Optional<ParkingTicket> getTicket(long ticketId) {
        return repository.findById(ticketId);
    }
}
