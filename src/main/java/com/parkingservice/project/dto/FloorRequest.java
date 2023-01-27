package com.parkingservice.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloorRequest {

    @JsonProperty(value = "slots")
    private List<SlotRequest> slots;
}
