package org.mosesidowu.boxdelivery.controller;


import lombok.RequiredArgsConstructor;
import org.mosesidowu.boxdelivery.dtos.request.BoxRequest;
import org.mosesidowu.boxdelivery.dtos.request.ItemRequest;
import org.mosesidowu.boxdelivery.dtos.response.BoxResponse;
import org.mosesidowu.boxdelivery.dtos.response.ItemResponse;
import org.mosesidowu.boxdelivery.service.BoxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RestControllerAdvice
@RequestMapping("/api/boxes")
@RequiredArgsConstructor
public class BoxController {
    private final BoxService boxService;

    @PostMapping
    public ResponseEntity<BoxResponse> createBox(@RequestBody BoxRequest req) {
        return ResponseEntity.ok(boxService.createBox(req));
    }

    @PostMapping("/{id}/load")
    public ResponseEntity<BoxResponse> loadBoxWithItems(@PathVariable Long id, @RequestBody List<ItemRequest> requests) {
        return ResponseEntity.ok(boxService.loadBoxWithItems(id, requests));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemResponse>> getLoadedItems(@PathVariable Long id) {
        return ResponseEntity.ok(boxService.checkLoadedItems(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<BoxResponse>> getAvailableBoxesForLoading() {
        return ResponseEntity.ok(boxService.checkAvailableBoxesForLoading());
    }

    @GetMapping("/{id}/battery")
    public ResponseEntity<?> getBatteryLevel(@PathVariable Long id) {
        int batteryLevel = boxService.checkBatteryLevel(id);
        return ResponseEntity.ok().body(Map.of("batteryLevel", batteryLevel));
    }
}
