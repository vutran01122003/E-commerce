package com.vutran0943.delivery_service.mappers;

import com.vutran0943.delivery_service.dtos.requests.ShipmentCreationRequest;
import com.vutran0943.delivery_service.dtos.responses.ShipmentResponse;
import com.vutran0943.delivery_service.entities.Shipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    ShipmentResponse toShipmentResponse(Shipment shipment);
    Shipment toShipment(ShipmentCreationRequest shipmentCreationRequest);
}
