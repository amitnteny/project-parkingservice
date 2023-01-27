package com.parkingservice.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingservice.project.enums.VehicleSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotRequest {

    @JsonProperty(value = "bay_size")
    private VehicleSize baySize;

    @JsonProperty(value = "count")
    private long count;
}
