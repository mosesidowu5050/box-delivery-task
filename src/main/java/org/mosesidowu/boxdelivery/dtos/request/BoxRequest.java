package org.mosesidowu.boxdelivery.dtos.request;

import lombok.*;
import org.mosesidowu.boxdelivery.data.model.Box;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoxRequest {

    public String txref;
    public int weightLimit;
    public int batteryCapacity;
    public String address;

}
