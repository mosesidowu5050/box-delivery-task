package org.mosesidowu.boxdelivery.service;

import lombok.RequiredArgsConstructor;
import org.mosesidowu.boxdelivery.data.model.Box;
import org.mosesidowu.boxdelivery.data.model.Item;
import org.mosesidowu.boxdelivery.data.repository.BoxRepository;
import org.mosesidowu.boxdelivery.data.repository.ItemRepository;
import org.mosesidowu.boxdelivery.dtos.request.BoxRequest;
import org.mosesidowu.boxdelivery.dtos.request.ItemRequest;
import org.mosesidowu.boxdelivery.dtos.response.BoxResponse;
import org.mosesidowu.boxdelivery.dtos.response.ItemResponse;
import org.mosesidowu.boxdelivery.exception.BoxDeliveryException;
import org.mosesidowu.geolocation_core.service.googleService.GoogleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoxServiceImpl implements BoxService {
    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;
    private final GoogleService googleService;


    @Override
    public BoxResponse createBox(BoxRequest boxRequest) {
        if (boxRequest.getWeightLimit() > 500) throw new BoxDeliveryException("Weight limit cannot exceed 500g");
        googleService.getCoordinates(boxRequest.address); // validate address

        Box box = Box.builder()
                .txref(boxRequest.getTxref())
                .weightLimit(boxRequest.getWeightLimit())
                .batteryCapacity(boxRequest.getBatteryCapacity())
                .state(Box.State.IDLE)
                .build();
        return toResponse(boxRepository.save(box));
    }

    @Override
    public List<BoxResponse> checkAvailableBoxesForLoading() {
        return boxRepository.findByState(Box.State.IDLE).stream()
                .map(this::toResponse).toList();
    }

    @Override
    public List<ItemResponse> checkLoadedItems(Long boxId) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new BoxDeliveryException("Box not found"));
        return itemRepository.findByBox(box).stream().map(ItemResponse::fromEntity).toList();
    }


    @Override
    public int checkBatteryLevel(Long boxId) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new BoxDeliveryException("Box not found"));
        return box.getBatteryCapacity();
    }

    @Transactional
    @Override
    public BoxResponse loadBoxWithItems(Long boxId, List<ItemRequest> items) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new BoxDeliveryException("Box not found"));
        if (box.getBatteryCapacity() < 25)
            throw new BoxDeliveryException("Battery must be at least 25% to load items.");

        int currentWeight = box.getItems() == null ? 0 :
                box.getItems().stream().mapToInt(Item::getWeight).sum();

        int incomingWeight = items.stream().mapToInt(ItemRequest::getWeight).sum();
        if (currentWeight + incomingWeight > box.getWeightLimit())
            throw new BoxDeliveryException("Items exceed box weight limit.");

        Set<Item> newItems = new HashSet<>();
        for (ItemRequest itemRequest : items) {
            Item item = Item.builder()
                    .name(itemRequest.getName())
                    .weight(itemRequest.getWeight())
                    .code(itemRequest.getCode())
                    .box(box)
                    .build();
            newItems.add(itemRepository.save(item));
        }
        if (box.getItems() == null)
            box.setItems(new HashSet<>());
        box.getItems().addAll(newItems);

        box.setState(Box.State.LOADING);
        return toResponse(boxRepository.save(box));
    }

    public BoxResponse toResponse(Box box) {
        BoxResponse boxResponse = new BoxResponse();
        boxResponse.setTxref(box.getTxref());
        boxResponse.setWeightLimit(box.getWeightLimit());
        boxResponse.setBatteryCapacity(box.getBatteryCapacity());
        boxResponse.setState(box.getState().name());
        if (box.getItems() != null)
            boxResponse.setItems(box.getItems()
                    .stream()
                    .map(ItemResponse::fromEntity)
                    .collect(Collectors.toSet()));
        return boxResponse;
    }
}
