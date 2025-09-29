package org.mosesidowu.boxdelivery.service;

import org.mosesidowu.boxdelivery.dtos.request.BoxRequest;
import org.mosesidowu.boxdelivery.dtos.request.ItemRequest;
import org.mosesidowu.boxdelivery.dtos.response.BoxResponse;
import org.mosesidowu.boxdelivery.dtos.response.ItemResponse;

import java.util.List;

public interface BoxService {
    BoxResponse createBox(BoxRequest req);

    BoxResponse loadBoxWithItems(Long boxId, List<ItemRequest> items);

    List<ItemResponse> checkLoadedItems(Long boxId);

    List<BoxResponse> checkAvailableBoxesForLoading();

    int checkBatteryLevel(Long boxId);

}
