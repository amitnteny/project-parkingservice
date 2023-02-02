package com.parkingservice.project.service;

import com.parkingservice.project.dao.BookingDao;
import com.parkingservice.project.dao.ParkingTicketDao;
import com.parkingservice.project.dto.BookingRequest;
import com.parkingservice.project.dto.TicketDetails;
import com.parkingservice.project.entity.*;
import com.parkingservice.project.enums.VehicleSize;
import com.parkingservice.project.exception.BadRequestException;
import com.parkingservice.project.exception.SlotUnavailableException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingDao bookingDao;
    @Mock
    private ParkingLotService parkingLotService;
    @Mock
    private ParkingTicketDao parkingTicketDao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(bookingService);
    }

    @Test
    public void testSlotBooking() throws SlotUnavailableException {
        Mockito.when(parkingLotService.getAvailableSlot(Mockito.anyLong(), Mockito.anyInt())).thenReturn(getAvailableSlot());
        Mockito.doNothing().when(parkingLotService).updateSlotAvailability(Mockito.any(), Mockito.anyBoolean());
        Mockito.when(bookingDao.persist(Mockito.any())).thenReturn(getBookingDetails());
        Mockito.when(parkingTicketDao.createTicket(Mockito.any())).thenReturn(getParkingTicket());
        BookingRequest bookingRequest = getBookingRequest();
        TicketDetails ticketDetails = bookingService.bookSlot(bookingRequest);
        Assert.assertEquals(200L, ticketDetails.getSlotNumber());
        Assert.assertEquals(2L, ticketDetails.getFloorNumber());
        Assert.assertEquals(VehicleSize.MEDIUM, ticketDetails.getParkingSlotSize());
        Assert.assertEquals("ABCDE", ticketDetails.getLicensePlateNumber());
    }

    @Test(expected = SlotUnavailableException.class)
    public void testSlotBookingFailure() throws SlotUnavailableException {
        Mockito.when(parkingLotService.getAvailableSlot(Mockito.anyLong(), Mockito.anyInt())).thenThrow(SlotUnavailableException.class);
        BookingRequest bookingRequest = getBookingRequest();
        bookingService.bookSlot(bookingRequest);
    }

    @Test
    public void testFreeSlotHappyPath() {
        Mockito.when(bookingDao.persist(Mockito.any())).thenReturn(new Booking());
        Optional<ParkingTicket> parkingTicket = Optional.of(getParkingTicket());
        Mockito.when(parkingTicketDao.getTicket(Mockito.anyLong())).thenReturn(parkingTicket);
        Mockito.doNothing().when(parkingLotService).updateSlotAvailability(Mockito.any(), Mockito.anyBoolean());
        bookingService.freeSlot(123L);
        Mockito.verify(parkingLotService, Mockito.times(1)).updateSlotAvailability(Mockito.any(), Mockito.anyBoolean());
    }

    @Test(expected = BadRequestException.class)
    public void testFreeSlotWithNullTicketId() throws Exception {
        bookingService.freeSlot(null);
    }

    @Test(expected = BadRequestException.class)
    public void testFreeSlotWithInvalidTicketId() throws Exception {
        Mockito.when(parkingTicketDao.getTicket(Mockito.anyLong())).thenReturn(Optional.empty());
        bookingService.freeSlot(123L);
    }

    private ParkingTicket getParkingTicket() {
        ParkingTicket parkingTicket = new ParkingTicket();
        parkingTicket.setBooking(getBookingDetails());
        parkingTicket.setTicketPrice(new BigDecimal(300));
        parkingTicket.setId(4321L);
        return parkingTicket;
    }

    private Booking getBookingDetails() {
        Booking booking = new Booking();
        booking.setId(54321L);
        Slot bookedSlot = getAvailableSlot();
        bookedSlot.setIsAvailable(false);
        booking.setSlot(bookedSlot);
        booking.setVehicleSize(bookedSlot.getVehicleSize());
        return booking;
    }

    private Slot getAvailableSlot() {
        Slot slot = new Slot();
        slot.setId(123L);
        slot.setSlotNumber(200L);
        slot.setVehicleSize(VehicleSize.MEDIUM);
        slot.setIsAvailable(true);
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(1L);
        Floor floor = new Floor();
        floor.setFloorNumber(2L);
        floor.setId(2L);
        floor.setParkingLot(parkingLot);
        slot.setFloor(floor);
        return slot;
    }

    private BookingRequest getBookingRequest() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setParkingLotId(1L);
        bookingRequest.setVehicleSize(VehicleSize.MEDIUM);
        bookingRequest.setLicensePlateNumber("ABCDE");
        bookingRequest.setBookingDurationInMinutes(200L);
        return bookingRequest;
    }

}