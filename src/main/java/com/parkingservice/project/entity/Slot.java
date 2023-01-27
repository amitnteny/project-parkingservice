package com.parkingservice.project.entity;

import com.parkingservice.project.enums.VehicleSize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "slot")
public class Slot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long id;

    @Column(name = "slot_number", nullable = false)
    private Long slotNumber;

    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private VehicleSize vehicleSize;

    @Column(name = "is_available", columnDefinition = "tinyint default 1", nullable = false)
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;
}
