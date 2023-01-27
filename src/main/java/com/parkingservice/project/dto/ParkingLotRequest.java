package com.parkingservice.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotRequest {

    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "zip_code")
    private String zipCode;
    @JsonProperty(value = "floors")
    private List<FloorRequest> floors;
}
