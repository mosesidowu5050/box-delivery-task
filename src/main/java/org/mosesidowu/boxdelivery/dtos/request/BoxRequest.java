package org.mosesidowu.boxdelivery.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoxRequest {

    public String txref;
    public int weightLimit;
    public int batteryCapacity;

}
