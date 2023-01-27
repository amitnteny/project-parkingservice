package com.parkingservice.project.repository;

import com.parkingservice.project.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query(value = "SELECT s.* FROM FLOOR f, SLOT s " +
            "WHERE f.parking_lot_id = ?1 " +
            "AND f.floor_id = s.floor_id " +
            "AND s.size = ?2 AND s.is_available = 1 LIMIT 1", nativeQuery = true)
    Slot getAvailableSlot(long parkingLotId, int size);

    @Modifying
    @Query(value = "UPDATE SLOT SET IS_AVAILABLE = ?2 WHERE SLOT_ID = ?1", nativeQuery = true)
    void updateSlotAvailability(Long slotId, int isAvailable);


}
