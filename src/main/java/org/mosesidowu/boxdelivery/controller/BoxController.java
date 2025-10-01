package org.mosesidowu.boxdelivery.controller;

import lombok.RequiredArgsConstructor;
import org.mosesidowu.boxdelivery.dtos.request.BoxRequest;
import org.mosesidowu.boxdelivery.dtos.request.ItemRequest;
import org.mosesidowu.boxdelivery.dtos.response.ApiResponse;
import org.mosesidowu.boxdelivery.service.BoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boxes")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;

    @PostMapping("/create-box")
    public ResponseEntity<?> createBox(@RequestBody BoxRequest boxRequest) {
        return new ResponseEntity<>(new ApiResponse<>(
                true, "Box created successfully",
                boxService.createBox(boxRequest)), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/load-items")
    public ResponseEntity<?> loadBoxWithItems(@PathVariable Long id, @RequestBody List<ItemRequest> requests) {
        return new ResponseEntity<>(new ApiResponse<>(
                true, "Items loaded successfully",
                boxService.loadBoxWithItems(id, requests)), HttpStatus.OK);
    }

    @GetMapping("/{id}/get-items")
    public ResponseEntity<?> getLoadedItems(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse<>(
                true, "Loaded items retrieved successfully",
                boxService.checkLoadedItems(id)), HttpStatus.OK
        );
    }

    @GetMapping("/get-available-items")
    public ResponseEntity<?> getAvailableBoxesForLoading() {
        return new ResponseEntity<>(new ApiResponse<>(
                true, "Available boxes retrieved",
                boxService.checkAvailableBoxesForLoading()), HttpStatus.OK
        );
    }

    @GetMapping("/{id}/battery-level")
    public ResponseEntity<?> getBatteryLevel(@PathVariable Long id) {
        int batteryLevel = boxService.checkBatteryLevel(id);
        return new ResponseEntity<>(new ApiResponse<>(
                true, "Battery level retrieved",
                Map.of("batteryLevel", batteryLevel)), HttpStatus.OK
        );
    }
}
