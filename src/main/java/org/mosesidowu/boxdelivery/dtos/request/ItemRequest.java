package org.mosesidowu.boxdelivery.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemRequest {

    public String name;
    public int weight;
    public String code;

}
