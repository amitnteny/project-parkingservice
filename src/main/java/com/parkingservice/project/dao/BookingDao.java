package com.parkingservice.project.dao;

import com.parkingservice.project.entity.Booking;

public interface BookingDao {
    Booking persist(Booking booking);
}
