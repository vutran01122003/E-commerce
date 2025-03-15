package com.vutran0943.delivery_service.controllers;

import com.vutran0943.delivery_service.dtos.requests.ShipmentCreationRequest;
import com.vutran0943.delivery_service.dtos.requests.UpdateShipmentStatusRequest;
import com.vutran0943.delivery_service.dtos.responses.ApiResponse;
import com.vutran0943.delivery_service.dtos.responses.ShipmentResponse;
import com.vutran0943.delivery_service.services.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class ShipmentController {
    private final ShipmentService shipmentService;

    @PostMapping
    public ApiResponse<ShipmentResponse> createShipment(@RequestBody ShipmentCreationRequest request) {
        ShipmentResponse res = shipmentService.createShipment(request);

        ApiResponse<ShipmentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(res);
        apiResponse.setMessage("Create shipment successful");

        return apiResponse;
    }

    @GetMapping("/{shipmentId}")
    public ApiResponse<ShipmentResponse> getShipment(@PathVariable String shipmentId) {
        ShipmentResponse res = shipmentService.getShipmentById(shipmentId);

        ApiResponse<ShipmentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(res);
        apiResponse.setMessage("Retrieved shipment successful");

        return apiResponse;
    }

    @PatchMapping("/{shipmentId}")
    public ApiResponse<ShipmentResponse> updateShipmentStatus(@PathVariable String shipmentId, @RequestBody UpdateShipmentStatusRequest request) {
        String status = request.getStatus();
        ShipmentResponse res = shipmentService.updateStatus(shipmentId, status);

        ApiResponse<ShipmentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(res);
        apiResponse.setMessage("Update shipment status successful");

        return apiResponse;
    }

    @DeleteMapping("/{shipmentId}")
    public ApiResponse<ShipmentResponse> cancelShipment(@PathVariable String shipmentId) {
        ShipmentResponse res = shipmentService.cancelShipment(shipmentId);

        ApiResponse<ShipmentResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(res);
        apiResponse.setMessage("Cancel shipment successful");

        return apiResponse;
    }
}
