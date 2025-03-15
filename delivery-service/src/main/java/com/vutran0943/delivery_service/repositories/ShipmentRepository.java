package com.vutran0943.delivery_service.repositories;

import com.vutran0943.delivery_service.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
    Shipment getShipmentsById(String id);
}
