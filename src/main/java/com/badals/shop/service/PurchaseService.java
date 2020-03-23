package com.badals.shop.service;

import com.badals.shop.domain.Purchase;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.PurchaseRepository;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.service.dto.PurchaseItemDTO;
import com.badals.shop.service.mapper.PurchaseItemMapper;
import com.badals.shop.service.mapper.PurchaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Purchase}.
 */
@Service
@Transactional
public class PurchaseService {

    private final Logger log = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseRepository purchaseRepository;

    private final PurchaseMapper purchaseMapper;
    private final PurchaseItemMapper purchaseItemMapper;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper, PurchaseItemMapper purchaseItemMapper) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.purchaseItemMapper = purchaseItemMapper;
    }

    /**
     * Save a purchase.
     *
     * @param purchaseDTO the entity to save.
     * @return the persisted entity.
     */
    public PurchaseDTO save(PurchaseDTO purchaseDTO) {
        log.debug("Request to save Purchase : {}", purchaseDTO);
        Purchase purchase = purchaseMapper.toEntity(purchaseDTO);
        purchase = purchaseRepository.save(purchase);
        return purchaseMapper.toDto(purchase);
    }

    /**
     * Get all the purchases.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseDTO> findAll() {
        log.debug("Request to get all Purchases");
        return purchaseRepository.findAll().stream()
            .map(purchaseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one purchase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseDTO> findOne(Long id) {
        log.debug("Request to get Purchase : {}", id);
        return purchaseRepository.findById(id)
            .map(purchaseMapper::toDto);
    }

    /**
     * Delete the purchase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Purchase : {}", id);
        purchaseRepository.deleteById(id);
    }

    public Optional<PurchaseDTO> findPurchaseJoinMerchantJoinPurchaseItemsJoinDeliveryAddress(Long id) {
        return purchaseRepository.findPurchaseJoinMerchantJoinPurchaseItemsJoinDeliveryAddress(id).map(purchaseMapper::toDto);
    }

    public List<PurchaseDTO> findAllByOrderByCreatedDateDesc(List<OrderState> orderState, Integer limit, String searchText) {
        return purchaseRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(0,limit)).stream().map(purchaseMapper::toDto).collect(Collectors.toList());
    }

    public PurchaseDTO createOrUpdatePurchase(PurchaseDTO dto, List<PurchaseItemDTO> items) {
        Purchase p = purchaseRepository.findById(dto.getId()).orElse(null);

        if(p != null) {
            dto.setId(p.getId());
        }
        Purchase n = purchaseMapper.toEntity(dto);
        n.setPurchaseItems(items.stream().map(purchaseItemMapper::toEntity).collect(Collectors.toSet()));
        n = purchaseRepository.save(n);
        return purchaseMapper.toDto(n);
    }
}
