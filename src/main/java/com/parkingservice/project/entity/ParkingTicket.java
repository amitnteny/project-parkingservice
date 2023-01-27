package com.parkingservice.project.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "parking_ticket")
public class ParkingTicket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "arrive_time", nullable = false)
    private Long arrivalTimestamp;

    @Column(name = "depart_time")
    private Long departureTimestamp;

    @Column(name = "ticket_price", nullable = false)
    private BigDecimal ticketPrice;

}
