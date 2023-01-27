package com.parkingservice.project.service;

import com.parkingservice.project.dao.BookingDao;
import com.parkingservice.project.dao.ParkingTicketDao;
import com.parkingservice.project.dto.BookingRequest;
import com.parkingservice.project.dto.TicketDetails;
import com.parkingservice.project.entity.Booking;
import com.parkingservice.project.entity.ParkingTicket;
import com.parkingservice.project.entity.Slot;
import com.parkingservice.project.enums.BookingStatus;
import com.parkingservice.project.enums.VehicleSize;
import com.parkingservice.project.exception.BadRequestException;
import com.parkingservice.project.exception.SlotUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Value(value = "${default.booking.duration.in.minutes}")
    private long DEFAULT_BOOKING_DURATION;

    @Value(value = "${default.booking.price.small}")
    private long DEFAULT_BOOKING_PRICE_SMALL;

    @Value(value = "${default.booking.price.medium}")
    private long DEFAULT_BOOKING_PRICE_MEDIUM;

    @Value(value = "${default.booking.price.large}")
    private long DEFAULT_BOOKING_PRICE_LARGE;

    @Value(value = "${default.booking.price.xl}")
    private long DEFAULT_BOOKING_PRICE_XL;
    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private ParkingTicketDao parkingTicketDao;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TicketDetails bookSlot(BookingRequest request) throws SlotUnavailableException {
        /* Todo: Validate Request */
        Slot availableSlot = parkingLotService.getAvailableSlot(request.getParkingLotId(), request.getVehicleSize().ordinal());
        parkingLotService.updateSlotAvailability(availableSlot, false);
        Booking bookingEntity = getBookingEntity(request, availableSlot);
        Booking booking = bookingDao.persist(bookingEntity);
        ParkingTicket createdTicket = createTicket(getParkingTicket(availableSlot, bookingEntity));
        return getTicket(createdTicket, booking, availableSlot, request);
    }

    @Override
    public void freeSlot(Long ticketId) throws BadRequestException {
        if (Objects.isNull(ticketId)) {
            throw new BadRequestException("Ticket ID is null!!!");
        }
        Optional<ParkingTicket> ticketOptional = parkingTicketDao.getTicket(ticketId);
        if (ticketOptional.isPresent()) {
            ParkingTicket ticket = ticketOptional.get();
            Booking bookingDetails = ticket.getBooking();
            updateBookingStatus(bookingDetails);
            updateDepartureTimestampInTicket(ticket);
            releaseParkingSlot(bookingDetails.getSlot());
        } else {
            throw new BadRequestException("INVALID TICKET ID!!!");
        }
    }

    private void releaseParkingSlot(Slot slot) {
        parkingLotService.updateSlotAvailability(slot, true);
    }

    private void updateDepartureTimestampInTicket(ParkingTicket ticket) {
        ticket.setDepartureTimestamp(Instant.now().getEpochSecond());
    }

    private void updateBookingStatus(Booking booking) {
        booking.setBookingStatus(BookingStatus.RELEASED);
        bookingDao.persist(booking);
    }

    private TicketDetails getTicket(ParkingTicket createdTicket, Booking booking, Slot slot, BookingRequest request) {
        TicketDetails ticketDetails = new TicketDetails();
        ticketDetails.setTicketId(createdTicket.getId());
        ticketDetails.setTicketPrice(createdTicket.getTicketPrice());
        ticketDetails.setParkingLotId(request.getParkingLotId());
        ticketDetails.setFloorNumber(slot.getFloor().getFloorNumber());
        ticketDetails.setLicensePlateNumber(request.getLicensePlateNumber());
        ticketDetails.setSlotNumber(slot.getSlotNumber());
        ticketDetails.setDurationInMinutes(request.getBookingDurationInMinutes());
        ticketDetails.setParkingSlotSize(booking.getVehicleSize());
        return ticketDetails;
    }

    private ParkingTicket getParkingTicket(Slot availableSlot, Booking bookingEntity) {
        ParkingTicket parkingTicket = new ParkingTicket();
        parkingTicket.setBooking(bookingEntity);
        parkingTicket.setTicketPrice(getTicketPrice(availableSlot.getVehicleSize()));
        parkingTicket.setArrivalTimestamp(bookingEntity.getBookingTimestamp());
        return parkingTicket;
    }

    private ParkingTicket createTicket(ParkingTicket parkingTicket) {
        return parkingTicketDao.createTicket(parkingTicket);
    }

    private BigDecimal getTicketPrice(VehicleSize vehicleSize) {
        switch (vehicleSize) {
            case SMALL:
                return BigDecimal.valueOf(DEFAULT_BOOKING_PRICE_SMALL);
            case MEDIUM:
                return BigDecimal.valueOf(DEFAULT_BOOKING_PRICE_MEDIUM);
            case LARGE:
                return BigDecimal.valueOf(DEFAULT_BOOKING_PRICE_LARGE);
            default:
                return BigDecimal.valueOf(DEFAULT_BOOKING_PRICE_XL);
        }
    }

    private Booking getBookingEntity(BookingRequest request, Slot availableSlot) {
        Booking booking = new Booking();
        booking.setBookingTimestamp(Instant.now().getEpochSecond());
        booking.setSlot(availableSlot);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setVehicleSize(availableSlot.getVehicleSize());
        booking.setLicensePlateNumber(request.getLicensePlateNumber());
        booking.setDurationInMinutes(Objects.nonNull(request.getBookingDurationInMinutes()) ?
                request.getBookingDurationInMinutes() : DEFAULT_BOOKING_DURATION);
        return booking;
    }
}
