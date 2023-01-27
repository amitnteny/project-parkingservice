package com.parkingservice.project.entity;

import com.parkingservice.project.enums.BookingStatus;
import com.parkingservice.project.enums.VehicleSize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(name = "booking_time")
    private Long bookingTimestamp;

    @Column(name = "duration_minutes")
    private Long durationInMinutes;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(name = "license_plate_number", nullable = false)
    private String licensePlateNumber;

    @Column(name = "vehicle_size", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private VehicleSize vehicleSize;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

}
