package com.parkingservice.project.dao;

import com.parkingservice.project.entity.ParkingLot;
import com.parkingservice.project.entity.Slot;

public interface ParkingLotDao {
    Slot getAvailableSlot(long parkingLotId, int size);

    void updateSlotAvailability(Long slotId, int intForBoolean);

    ParkingLot createNew(ParkingLot parkingLot);
}
