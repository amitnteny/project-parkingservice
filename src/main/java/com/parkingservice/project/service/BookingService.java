package com.parkingservice.project.service;

import com.parkingservice.project.dto.BookingRequest;
import com.parkingservice.project.dto.TicketDetails;
import com.parkingservice.project.exception.BadRequestException;
import com.parkingservice.project.exception.SlotUnavailableException;

public interface BookingService {

    TicketDetails bookSlot(BookingRequest bookingRequest) throws SlotUnavailableException;

    void freeSlot(Long ticketId) throws BadRequestException;
}
