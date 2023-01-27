package com.parkingservice.project.dao;

import com.parkingservice.project.entity.Booking;
import com.parkingservice.project.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookingDaoImpl implements BookingDao {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking persist(Booking booking) {
        return bookingRepository.save(booking);
    }

}
