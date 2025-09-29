package org.mosesidowu.boxdelivery.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoxRequest {

    public String txref;
    public int weightLimit;
    public int batteryCapacity;

}
