package com.parkingservice.project.dao;

import com.parkingservice.project.entity.ParkingLot;
import com.parkingservice.project.entity.Slot;
import com.parkingservice.project.repository.ParkingLotRepository;
import com.parkingservice.project.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParkingLotDaoImpl implements ParkingLotDao {

    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Override
    public Slot getAvailableSlot(long parkingLotId, int size) {
        return slotRepository.getAvailableSlot(parkingLotId, size);
    }

    @Override
    public void updateSlotAvailability(Long slotId, int isAvailable) {
        slotRepository.updateSlotAvailability(slotId, isAvailable);
    }

    @Override
    public ParkingLot createNew(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }
}
