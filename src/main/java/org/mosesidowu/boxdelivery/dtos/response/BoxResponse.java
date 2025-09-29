package org.mosesidowu.boxdelivery.dtos.response;

import lombok.Data;
import java.util.Set;

@Data
public class BoxResponse {
    private Long id;
    private String txref;
    private int weightLimit;
    private int batteryCapacity;
    private String state;
    private Set<ItemResponse> items;
}