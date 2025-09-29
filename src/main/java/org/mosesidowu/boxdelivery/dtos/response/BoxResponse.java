package org.mosesidowu.boxdelivery.dtos.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoxResponse {

    private String txref;
    private int weightLimit;
    private int batteryCapacity;
    private String state;
    private Set<ItemResponse> items;


}