package com.vutran0943.delivery_service.services;

import com.vutran0943.delivery_service.dtos.requests.ShipmentCreationRequest;
import com.vutran0943.delivery_service.dtos.responses.ShipmentResponse;
import com.vutran0943.delivery_service.entities.Shipment;
import com.vutran0943.delivery_service.exceptions.AppException;
import com.vutran0943.delivery_service.exceptions.ErrorCode;
import com.vutran0943.delivery_service.mappers.ShipmentMapper;
import com.vutran0943.delivery_service.repositories.ShipmentRepository;
import com.vutran0943.delivery_service.shared.DeliveryProvider;
import com.vutran0943.delivery_service.shared.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;

    public ShipmentResponse createShipment(ShipmentCreationRequest shipmentCreationRequest) {
        System.out.println(shipmentCreationRequest);
        Shipment shipment = shipmentMapper.toShipment(shipmentCreationRequest);

        if(shipmentCreationRequest.getCarrier() == null) shipment.setCarrier(DeliveryProvider.G18FAST.toString());
        shipment.setStatus(DeliveryStatus.PENDING.toString());

        return shipmentMapper.toShipmentResponse(shipmentRepository.save(shipment));
    }

    public ShipmentResponse getShipmentById(String id) {
        Shipment shipment = shipmentRepository.getShipmentsById(id);
        return shipmentMapper.toShipmentResponse(shipment);
    }

    public ShipmentResponse updateStatus(String shipmentId, String status) {
        try {
            Shipment shipment = shipmentRepository.getShipmentsById(shipmentId);

            if(shipment == null) throw new AppException(ErrorCode.SHIPMENT_NOTFOUND_ERROR);

            DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(status);

            shipment.setStatus(deliveryStatus.toString());

            return shipmentMapper.toShipmentResponse(shipmentRepository.save(shipment));
        } catch (IllegalArgumentException error) {
            throw new AppException(ErrorCode.SHIPMENT_STATUS_INVALID);
        }
    }
    public ShipmentResponse cancelShipment(String shipmentId) {
        Shipment shipment = shipmentRepository.getShipmentsById(shipmentId);

        if(shipment == null) throw new AppException(ErrorCode.SHIPMENT_NOTFOUND_ERROR);

        shipment.setStatus(DeliveryStatus.CANCELLED.toString());

        return shipmentMapper.toShipmentResponse(shipmentRepository.save(shipment));
    }
}
