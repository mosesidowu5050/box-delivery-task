package org.mosesidowu.boxdelivery.dtos.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {

    public String name;
    public int weight;
    public String code;

}
