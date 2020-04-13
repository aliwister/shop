package com.badals.shop.service;


import com.badals.shop.domain.ShipmentDoc;
import com.badals.shop.repository.ShipmentDocRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing {@link ShipmentDoc}.
 */
@Service
@Transactional
public class ShipmentDocService {

    private final Logger log = LoggerFactory.getLogger(ShipmentDocService.class);

    private final ShipmentDocRepository shipmentDocRepository;


    public ShipmentDocService(ShipmentDocRepository shipmentDocRepository) {
        this.shipmentDocRepository = shipmentDocRepository;
    }

    /**
     * Save a shipmentDoc.
     *
     * @param shipmentDoc the entity to save.
     * @return the persisted entity.
     */
    public ShipmentDoc save(ShipmentDoc shipmentDoc) {
        log.debug("Request to save ShipmentDoc : {}", shipmentDoc);
        //ShipmentDoc shipmentDoc = shipmentDocMapper.toEntity(shipmentDocDTO);
        shipmentDoc = shipmentDocRepository.save(shipmentDoc);
        return shipmentDoc;
    }

    /**
     * Delete the shipmentDoc by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShipmentDoc : {}", id);
        shipmentDocRepository.deleteById(id);
    }
}
