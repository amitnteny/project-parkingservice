package com.parkingservice.project.service;

import com.parkingservice.project.dao.ParkingLotDao;
import com.parkingservice.project.dto.FloorRequest;
import com.parkingservice.project.dto.ParkingLotRequest;
import com.parkingservice.project.dto.SlotRequest;
import com.parkingservice.project.entity.Floor;
import com.parkingservice.project.entity.ParkingLot;
import com.parkingservice.project.entity.Slot;
import com.parkingservice.project.exception.SlotUnavailableException;
import com.parkingservice.project.utils.ParkingServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.parkingservice.project.utils.ParkingServiceUtils.getAvailableStackKey;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    private static final String BOOKED_SLOT_MAP = "BOOKED_SLOT_MAP";
    @Autowired
    private ParkingLotDao parkingLotDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addNewParkingLot(ParkingLotRequest parkingLotRequest) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(parkingLotRequest.getName());
        parkingLot.setZipCode(parkingLotRequest.getZipCode());
        List<FloorRequest> floors = parkingLotRequest.getFloors();
        parkingLot.setFloorCount(floors.stream().count());
        parkingLot.setSlotCount(floors.stream()
                .flatMap(floor -> floor.getSlots().stream())
                .count());
        List<Floor> floorEntities = getFloors(parkingLotRequest.getFloors());
        parkingLot.setFloor(floorEntities);
        ParkingLot createdParkingLot = parkingLotDao.createNew(parkingLot);
        long parkingLotId = createdParkingLot.getId();
        createdParkingLot.getFloor().stream()
                .flatMap(floor -> floor.getSlots().stream())
                        .forEach(slot -> {
                            String key = getAvailableStackKey(parkingLotId, slot.getVehicleSize().ordinal());
                            redisTemplate.opsForList().leftPush(key, slot);
                        });
    }

    @Override
    public Slot getAvailableSlot(long parkingLotId, int size) throws SlotUnavailableException {
        String slotKey = getAvailableStackKey(parkingLotId, size);
        Slot availableSlot = null;
        try {
            availableSlot = (Slot) redisTemplate.opsForList().leftPop(slotKey);
            if (Objects.isNull(availableSlot)) {
                availableSlot = getLargerAvailableSlot(parkingLotId, size, true);
            }
        } catch (Exception e) {
            availableSlot = parkingLotDao.getAvailableSlot(parkingLotId, size);
            if (Objects.isNull(availableSlot)) {
                availableSlot = getLargerAvailableSlot(parkingLotId, size, false);
            }
        }
        if (Objects.isNull(availableSlot)) {
            throw new SlotUnavailableException("NO SLOT FOUND!!!");
        }
        return availableSlot;
    }

    private Slot getLargerAvailableSlot(long parkingLotId, int size, boolean isCacheLookup) throws SlotUnavailableException {
        Slot availableSlot = null;
        for (int newSlotSize = size + 1; newSlotSize < 4; newSlotSize++) {
            availableSlot = isCacheLookup ? getAvailableSlot(parkingLotId, newSlotSize)
                    : parkingLotDao.getAvailableSlot(parkingLotId, size);
            if (Objects.nonNull(availableSlot)) {
                return availableSlot;
            }
        }
        return availableSlot;
    }

    @Override
    public void updateSlotAvailability(Slot slot, boolean isAvailable) {
        slot.setIsAvailable(isAvailable);
        if (isAvailable) {
            //Release Slot
            redisTemplate.opsForHash().delete(BOOKED_SLOT_MAP, slot.getId());
            updateReleaseInDb(slot);
        } else {
            //Book Slot
            redisTemplate.opsForHash().put(BOOKED_SLOT_MAP, String.valueOf(slot.getId()), slot);
            updateSlotBookingInDb(slot);
        }
    }

    @Async
    private void updateSlotBookingInDb(Slot slot) {
        parkingLotDao.updateSlotAvailability(slot.getId(), ParkingServiceUtils.getIntForBoolean(slot.getIsAvailable()));
    }

    @Async
    private void updateReleaseInDb(Slot slot) {
        parkingLotDao.updateSlotAvailability(slot.getId(), ParkingServiceUtils.getIntForBoolean(slot.getIsAvailable()));
        redisTemplate.opsForList().leftPush(getAvailableStackKey(slot.getFloor().getParkingLot().getId(),
                slot.getVehicleSize().ordinal()), slot);
    }

    private List<Floor> getFloors(List<FloorRequest> floorsList) {
        List<Floor> floors = new ArrayList<>();
        for (int floorCount = 0; floorCount < floorsList.size(); floorCount++) {
            floors.add(convertToFloorEntity(floorsList.get(floorCount), floorCount + 1));
        }
        return floors;
    }

    private Floor convertToFloorEntity(FloorRequest floorRequest, int floorNumber) {
        Floor floorEntity = new Floor();
        List<Slot> slots = new ArrayList<>();
        for (int slotNumber = 0; slotNumber < floorRequest.getSlots().size(); slotNumber++) {
            slots.add(convertToSlotEntity(floorRequest.getSlots().get(slotNumber), slotNumber + 1));
        }
        floorEntity.setSlots(slots);
        floorEntity.setFloorNumber(Long.valueOf(floorNumber));
        floorEntity.setSlotCount(floorRequest.getSlots().stream().count());
        return floorEntity;
    }

    private Slot convertToSlotEntity(SlotRequest slotRequest, int slotNumber) {
        Slot slotEntity = new Slot();
        slotEntity.setVehicleSize(slotRequest.getBaySize());
        slotEntity.setSlotNumber(Long.valueOf(slotNumber));
        slotEntity.setIsAvailable(true);
        return slotEntity;
    }
}
