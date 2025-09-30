package org.mosesidowu.boxdelivery.controller;

import lombok.RequiredArgsConstructor;
import org.mosesidowu.boxdelivery.dtos.request.BoxRequest;
import org.mosesidowu.boxdelivery.dtos.request.ItemRequest;
import org.mosesidowu.boxdelivery.dtos.response.ApiResponse;
import org.mosesidowu.boxdelivery.dtos.response.BoxResponse;
import org.mosesidowu.boxdelivery.dtos.response.ItemResponse;
import org.mosesidowu.boxdelivery.service.BoxService;
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
    public ApiResponse<BoxResponse> createBox(@RequestBody BoxRequest req) {
        return ApiResponse.success(boxService.createBox(req));
    }

    @PostMapping("/{id}/load-items")
    public ApiResponse<BoxResponse> loadBoxWithItems(@PathVariable Long id, @RequestBody List<ItemRequest> requests) {
        return ApiResponse.success(boxService.loadBoxWithItems(id, requests));
    }

    @GetMapping("/{id}/get-items")
    public ApiResponse<List<ItemResponse>> getLoadedItems(@PathVariable Long id) {
        return ApiResponse.success(boxService.checkLoadedItems(id));
    }

    @GetMapping("/get_available-items")
    public ApiResponse<List<BoxResponse>> getAvailableBoxesForLoading() {
        return ApiResponse.success(boxService.checkAvailableBoxesForLoading());
    }

    @GetMapping("/{id}/battery-level")
    public ApiResponse<?> getBatteryLevel(@PathVariable Long id) {
        int batteryLevel = boxService.checkBatteryLevel(id);
        return ApiResponse.success(Map.of("batteryLevel", batteryLevel));
    }
}
