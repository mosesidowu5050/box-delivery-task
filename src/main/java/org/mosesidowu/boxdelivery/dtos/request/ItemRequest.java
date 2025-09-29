package org.mosesidowu.boxdelivery.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    public String name;
    public int weight;
    public String code;

}
