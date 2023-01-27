package com.parkingservice.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "parking_lot")
public class ParkingLot implements Serializable {

    @Id
    @Column(name = "parking_lot_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "floor_count", nullable = false)
    private Long floorCount;

    @Column(name = "slot_count", nullable = false)
    private Long slotCount;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Floor> floor;
}
