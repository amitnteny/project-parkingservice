package com.parkingservice.project.service;

import com.parkingservice.project.dto.ParkingLotRequest;
import com.parkingservice.project.entity.Slot;
import com.parkingservice.project.exception.SlotUnavailableException;

public interface ParkingLotService {
    Slot getAvailableSlot(long parkingLotId, int size) throws SlotUnavailableException;

    void updateSlotAvailability(Slot slot, boolean isAvailable);

    void addNewParkingLot(ParkingLotRequest parkingLotRequest);
}
