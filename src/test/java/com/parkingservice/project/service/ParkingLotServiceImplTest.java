package com.parkingservice.project.service;

import com.parkingservice.project.dao.ParkingLotDao;
import com.parkingservice.project.dto.FloorRequest;
import com.parkingservice.project.dto.ParkingLotRequest;
import com.parkingservice.project.dto.SlotRequest;
import com.parkingservice.project.entity.Floor;
import com.parkingservice.project.entity.ParkingLot;
import com.parkingservice.project.entity.Slot;
import com.parkingservice.project.enums.VehicleSize;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParkingLotServiceImplTest {

    @InjectMocks
    private ParkingLotServiceImpl parkingLotService;

    @Mock
    private ParkingLotDao parkingLotDao;

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @Mock
    private HashOperations<String, String, Object> hashOperations;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(parkingLotService);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.when(redisTemplate.opsForList()).thenReturn(listOperations);
        Mockito.when(listOperations.leftPush(Mockito.any(), Mockito.any())).thenReturn(1L);
        Mockito.when(listOperations.leftPop(Mockito.any(), Mockito.any())).thenReturn(getSlot1());
        Mockito.doNothing().when(hashOperations).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
        Mockito.when(hashOperations.delete(Mockito.any(), Mockito.anyLong())).thenReturn(1L);
    }

    @Test
    public void testOnboardParkingLot() throws Exception {
        ParkingLotRequest parkingLotRequest = createParkingLotRequest();
        Mockito.when(parkingLotDao.createNew(Mockito.any())).thenReturn(getParkingLotEntity());
        parkingLotService.addNewParkingLot(parkingLotRequest);
        Mockito.verify(parkingLotDao, Mockito.times(1)).createNew(Mockito.any());
    }

    @Test
    public void testGetAvailableSlot() {
        Mockito.when(parkingLotDao.getAvailableSlot(Mockito.anyLong(), Mockito.anyInt())).thenReturn(getSlot1());
        Slot availableSlot = parkingLotService.getAvailableSlot(1L, VehicleSize.MEDIUM.ordinal());
        Assert.assertEquals(123L, availableSlot.getId().longValue());
        Assert.assertEquals(VehicleSize.MEDIUM, availableSlot.getVehicleSize());
    }

    @Test
    public void testUpdateSlotAvailabilityAfterFreeingSlot() {
        Mockito.doNothing().when(parkingLotDao).updateSlotAvailability(Mockito.anyLong(), Mockito.anyInt());
        parkingLotService.updateSlotAvailability(getSlot1(), true);
        Mockito.verify(parkingLotDao, Mockito.times(1))
                .updateSlotAvailability(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    public void testUpdateSlotAvailabilityAfterBookingSlot() {

        Mockito.doNothing().when(parkingLotDao).updateSlotAvailability(Mockito.anyLong(), Mockito.anyInt());
        parkingLotService.updateSlotAvailability(getSlot1(), true);
        Mockito.verify(parkingLotDao, Mockito.times(1))
                .updateSlotAvailability(Mockito.anyLong(), Mockito.anyInt());
    }

    private ParkingLotRequest createParkingLotRequest() {
        ParkingLotRequest parkingLotRequest = new ParkingLotRequest();
        parkingLotRequest.setName("Indiranagar");
        parkingLotRequest.setZipCode("560001");
        FloorRequest floorRequest1 = getFloorRequest1();
        FloorRequest floorRequest2 = getFloorRequest2();
        List<FloorRequest> floorRequests = new ArrayList<>();
        floorRequests.add(floorRequest1);
        floorRequests.add(floorRequest2);
        parkingLotRequest.setFloors(floorRequests);
        return parkingLotRequest;
    }

    private FloorRequest getFloorRequest1() {
        List<SlotRequest> slotRequests = new ArrayList<>();
        SlotRequest slotRequest1 = new SlotRequest();
        slotRequest1.setBaySize(VehicleSize.MEDIUM);
        slotRequest1.setCount(1);
        SlotRequest slotRequest2 = new SlotRequest();
        slotRequest2.setBaySize(VehicleSize.SMALL);
        slotRequest2.setCount(1);
        slotRequests.add(slotRequest1);
        slotRequests.add(slotRequest2);
        FloorRequest floorRequest1 = new FloorRequest();
        floorRequest1.setSlots(slotRequests);
        return floorRequest1;
    }

    private FloorRequest getFloorRequest2() {
        FloorRequest floorRequest2 = new FloorRequest();
        SlotRequest slotRequest3 = new SlotRequest();
        slotRequest3.setBaySize(VehicleSize.LARGE);
        slotRequest3.setCount(1);
        SlotRequest slotRequest4 = new SlotRequest();
        slotRequest4.setBaySize(VehicleSize.XL);
        slotRequest4.setCount(1);
        List<SlotRequest> slotRequests2 = new ArrayList<>();
        slotRequests2.add(slotRequest3);
        slotRequests2.add(slotRequest4);
        floorRequest2.setSlots(slotRequests2);
        return floorRequest2;
    }

    private Floor getFloor1() {
        Floor floor = new Floor();
        floor.setId(12345L);
        floor.setFloorNumber(1L);
        floor.setSlotCount(2L);
        List<Slot> slots = new ArrayList<>();
        slots.add(getSlot1());
        slots.add(getSlot2());
        floor.setSlots(slots);
        return floor;
    }

    private Floor getFloor2() {
        Floor floor = new Floor();
        floor.setId(12346L);
        floor.setFloorNumber(2L);
        floor.setSlotCount(2L);
        List<Slot> slots = new ArrayList<>();
        slots.add(getSlot3());
        slots.add(getSlot4());
        floor.setSlots(slots);
        return floor;
    }

    private Slot getSlot1() {
        Slot slot = new Slot();
        slot.setId(123L);
        slot.setSlotNumber(200L);
        slot.setVehicleSize(VehicleSize.MEDIUM);
        slot.setIsAvailable(true);
        Floor floor = new Floor();
        floor.setFloorNumber(1L);
        floor.setId(12345L);
        floor.setParkingLot(getParkingLot());
        slot.setFloor(floor);
        return slot;
    }

    private Slot getSlot2() {
        Slot slot = new Slot();
        slot.setId(125L);
        slot.setSlotNumber(201L);
        slot.setVehicleSize(VehicleSize.SMALL);
        slot.setIsAvailable(true);
        Floor floor = new Floor();
        floor.setFloorNumber(1L);
        floor.setId(12345L);
        floor.setParkingLot(getParkingLot());
        slot.setFloor(floor);
        return slot;
    }

    private Slot getSlot3() {
        Slot slot = new Slot();
        slot.setId(127L);
        slot.setSlotNumber(203L);
        slot.setVehicleSize(VehicleSize.LARGE);
        slot.setIsAvailable(true);
        Floor floor = new Floor();
        floor.setFloorNumber(2L);
        floor.setId(12346L);
        floor.setParkingLot(getParkingLot());
        slot.setFloor(floor);
        return slot;
    }

    private Slot getSlot4() {
        Slot slot = new Slot();
        slot.setId(129L);
        slot.setSlotNumber(204L);
        slot.setVehicleSize(VehicleSize.XL);
        slot.setIsAvailable(true);
        Floor floor = new Floor();
        floor.setFloorNumber(2L);
        floor.setId(12346L);
        floor.setParkingLot(getParkingLot());
        slot.setFloor(floor);
        return slot;
    }

    private ParkingLot getParkingLot() {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(1L);
        parkingLot.setFloorCount(2L);
        parkingLot.setName("Indiranagar");
        parkingLot.setZipCode("560001");
        parkingLot.setSlotCount(4L);
        List<Floor> floors = new ArrayList<>();
        parkingLot.setFloor(floors);
        return parkingLot;
    }

    private ParkingLot getParkingLotEntity() {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(1L);
        parkingLot.setFloorCount(2L);
        parkingLot.setName("Indiranagar");
        parkingLot.setZipCode("560001");
        parkingLot.setSlotCount(4L);
        List<Floor> floors = new ArrayList<>();
        parkingLot.setFloor(floors);
        return parkingLot;
    }
}