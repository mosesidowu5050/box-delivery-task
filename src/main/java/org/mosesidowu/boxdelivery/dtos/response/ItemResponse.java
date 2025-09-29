package org.mosesidowu.boxdelivery.dtos.response;

import lombok.Data;
import org.mosesidowu.boxdelivery.data.model.Item;

@Data
public class ItemResponse {
    private String name;
    private int weight;
    private String code;

    public static ItemResponse fromEntity(Item item) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setName(item.getName());
        itemResponse.setWeight(item.getWeight());
        itemResponse.setCode(item.getCode());

        return itemResponse;
    }
}