package com.badals.shop.service;

import com.badals.shop.domain.Shipment;
import com.badals.shop.domain.ShipmentTracking;
import com.badals.shop.domain.enumeration.ShipmentStatus;
import com.badals.shop.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

//import com.badals.admin.repository.search.ShipmentSearchRepository;

/**
 * Service Implementation for managing {@link Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Shipment> findOne(Long id) {
        log.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id);
    }

    public void setStatus(Long shipmentId, ShipmentStatus status, String details) {
        int id = 2001;
        switch (status) {
            case DELIVERED:
               id =  3000;
               break;
            case FAILED:
                id = 2001;
                break;
        }

        Shipment shipment = shipmentRepository.findById(shipmentId).get();
        shipment.setShipmentStatus(status);
        shipment.addShipmentTracking(new ShipmentTracking().shipment(shipment).shipmentEventId(id).details(details).eventDate(LocalDateTime.now()));
        shipmentRepository.save(shipment);
    }

    /**
     * Search for the shipment corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
/*    @Transactional(readOnly = true)
    public List<ShipmentDTO> search(String query) {
        log.debug("Request to search Shipments for query {}", query);
        return StreamSupport
            .stream(shipmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(shipmentMapper::toDto)
            .collect(Collectors.toList());
    }*/
}
