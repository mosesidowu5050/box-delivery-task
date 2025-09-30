package org.mosesidowu.boxdelivery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mosesidowu.boxdelivery.data.model.Box;
import org.mosesidowu.boxdelivery.data.model.Item;
import org.mosesidowu.boxdelivery.data.repository.BoxRepository;
import org.mosesidowu.boxdelivery.data.repository.ItemRepository;
import org.mosesidowu.boxdelivery.dtos.request.BoxRequest;
import org.mosesidowu.boxdelivery.dtos.request.ItemRequest;
import org.mosesidowu.boxdelivery.dtos.response.BoxResponse;
import org.mosesidowu.boxdelivery.exception.BoxDeliveryException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoxServiceImplTest {

    private BoxRepository boxRepository;
    private ItemRepository itemRepository;
    private BoxServiceImpl boxService;

    @BeforeEach
    void setUp() {
        boxRepository = mock(BoxRepository.class);
        itemRepository = mock(ItemRepository.class);
        boxService = new BoxServiceImpl(boxRepository, itemRepository);
    }

    @Test
    @DisplayName("Should create a box successfully when within weight limit")
    void createBoxSuccess() {
        BoxRequest request = BoxRequest.builder()
                .txref("BOX_123")
                .weightLimit(400)
                .batteryCapacity(80)
                .build();

        Box savedBox = Box.builder()
                .id(1L)
                .txref("BOX_123")
                .weightLimit(400)
                .batteryCapacity(80)
                .state(Box.State.IDLE)
                .build();

        when(boxRepository.save(any(Box.class))).thenReturn(savedBox);

        BoxResponse response = boxService.createBox(request);
        assertThat(response.getTxref()).isEqualTo("BOX_123");
        assertThat(response.getWeightLimit()).isEqualTo(400);
        assertThat(response.getBatteryCapacity()).isEqualTo(80);
        assertThat(response.getState()).isEqualTo("IDLE");

        verify(boxRepository, times(1)).save(any(Box.class));
    }

    @Test
    @DisplayName("Should throw exception if weight limit exceeds 500g")
    void createBoxFailsWeightLimit() {
        BoxRequest request = BoxRequest.builder()
                .txref("BOX_HEAVY")
                .weightLimit(600)
                .batteryCapacity(80)
                .build();

        assertThatThrownBy(() -> boxService.createBox(request))
                .isInstanceOf(BoxDeliveryException.class)
                .hasMessageContaining("Weight limit cannot exceed 500g");
    }

    @Test
    @DisplayName("Should return available boxes for loading")
    void checkAvailableBoxesForLoading() {
        Box box1 = Box.builder().id(1L).txref("BOX_A").weightLimit(300).batteryCapacity(70).state(Box.State.IDLE).build();
        Box box2 = Box.builder().id(2L).txref("BOX_B").weightLimit(200).batteryCapacity(50).state(Box.State.IDLE).build();

        when(boxRepository.findByState(Box.State.IDLE)).thenReturn(List.of(box1, box2));
        List<BoxResponse> responses = boxService.checkAvailableBoxesForLoading();
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(BoxResponse::getTxref).containsExactly("BOX_A", "BOX_B");
    }

    @Test
    @DisplayName("Should return loaded items for a given box")
    void checkLoadedItems() {
        Box box = Box.builder().id(1L).txref("BOX_123").weightLimit(300).batteryCapacity(70).state(Box.State.IDLE).build();
        Item firstItem = Item.builder().id(1L).name("Item1").weight(100).code("C001").box(box).build();
        Item secondItem = Item.builder().id(2L).name("Item2").weight(150).code("C002").box(box).build();

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(itemRepository.findByBox(box)).thenReturn(List.of(firstItem, secondItem));
        var items = boxService.checkLoadedItems(1L);

        assertThat(items).hasSize(2);
        assertThat(items).extracting("name").contains("Item1", "Item2");
    }

    @Test
    @DisplayName("Should throw exception if box not found when checking items")
    void checkLoadedItemsBoxNotFound() {
        when(boxRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boxService.checkLoadedItems(99L))
                .isInstanceOf(BoxDeliveryException.class)
                .hasMessageContaining("Box not found");
    }

    @Test
    @DisplayName("Should check battery level of a box")
    void checkBatteryLevel() {
        Box box = Box.builder().id(1L).txref("BOX_BAT").batteryCapacity(85).state(Box.State.IDLE).build();
        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        int battery = boxService.checkBatteryLevel(1L);
        assertThat(battery).isEqualTo(85);
    }

    @Test
    @DisplayName("Should load items into a box successfully")
    void loadBoxWithItemsSuccess() {
        Box box = Box.builder()
                .id(1L)
                .txref("BOX_LOAD")
                .weightLimit(500)
                .batteryCapacity(90)
                .state(Box.State.IDLE)
                .items(new HashSet<>())
                .build();

        ItemRequest itemRequest = ItemRequest.builder().name("Item1").weight(100).code("C001").build();
        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(boxRepository.save(any(Box.class))).thenReturn(box);

        BoxResponse response = boxService.loadBoxWithItems(1L, List.of(itemRequest));
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().iterator().next().getName()).isEqualTo("Item1");
        assertThat(response.getState()).isEqualTo("LOADING");
    }

    @Test
    @DisplayName("Should not load items if battery is below 25%")
    void loadBoxWithItemsFailsLowBattery() {
        Box box = Box.builder().id(1L).txref("BOX_LOW").weightLimit(500).batteryCapacity(20).state(Box.State.IDLE).build();

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));
        ItemRequest itemReq = ItemRequest.builder().name("Item1").weight(50).code("C001").build();
        assertThatThrownBy(() -> boxService.loadBoxWithItems(1L, List.of(itemReq)))
                .isInstanceOf(BoxDeliveryException.class)
                .hasMessageContaining("Battery must be at least 25%");
    }

    @Test
    @DisplayName("Should not load items if total weight exceeds limit")
    void loadBoxWithItemsFailsOverWeight() {
        Box box = Box.builder()
                .id(1L)
                .txref("BOX_FULL")
                .weightLimit(100)
                .batteryCapacity(90)
                .state(Box.State.IDLE)
                .items(new HashSet<>())
                .build();

        ItemRequest itemRequest = ItemRequest.builder().name("HeavyItem").weight(200).code("H001").build();

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));
        assertThatThrownBy(() -> boxService.loadBoxWithItems(1L, List.of(itemRequest)))
                .isInstanceOf(BoxDeliveryException.class)
                .hasMessageContaining("Items exceed box weight limit");
    }
}
