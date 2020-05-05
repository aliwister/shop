package com.badals.shop.service;

import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.PurchaseRepository;
import com.badals.shop.repository.projection.PurchaseQueue;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.service.dto.PurchaseItemDTO;
import com.badals.shop.service.mapper.PurchaseItemMapper;
import com.badals.shop.service.mapper.PurchaseMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Source;
import java.math.BigDecimal;
import java.util.*;
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

    public Optional<PurchaseDTO> findForPurchaseDetails(Long id) {
        return purchaseRepository.findForPurchaseDetails(id).map(purchaseMapper::toDto);
    }

    public List<PurchaseDTO> findForPurchaseList(List<OrderState> orderState, Integer limit, String searchText) {
        return purchaseRepository.findForPurchaseList(PageRequest.of(0,limit)).stream().map(purchaseMapper::toDto).collect(Collectors.toList());
    }

    public PurchaseDTO updatePurchase(PurchaseDTO dto, List<PurchaseItemDTO> items) {
        //Purchase purchase = purchaseRepository.findForUpdate(dto.getId()).orElse(null);
        Purchase purchase = purchaseMapper.toEntity(dto);
        if(purchase != null) {
            //throw InvalidPurchaseException("Product doesn't exist");
        }
        purchase.getPurchaseItems().clear();

        for(PurchaseItemDTO i : items) {
            PurchaseItem pi = purchaseItemMapper.toEntity(i);
            pi.setPurchase(purchase);
            purchase.getPurchaseItems().add(pi);
        }
        purchase.setTotal(calculateTotal(purchase));
        purchase.setSubtotal(calculateSubtotal(purchase));
        purchase = purchaseRepository.save(purchase);
        return purchaseMapper.toDto(purchase);
    }

    public List<PurchaseQueue> getPurchaseQueue() {
        return purchaseRepository.getPurchaseQueue();
    }

    public BigDecimal calculateSubtotal(Purchase order) {
        BigDecimal sum = BigDecimal.valueOf(order.getPurchaseItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum());
        return sum;
    }

    public BigDecimal calculateTotal(Purchase order) {
        BigDecimal sum = BigDecimal.valueOf(order.getPurchaseItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum());
        if(order.getDeliveryTotal() != null)
            sum = sum.add(order.getDeliveryTotal());
        if(order.getTaxesTotal() != null)
            sum = sum.add(order.getTaxesTotal());
        if(order.getDiscountTotal()!= null)
            sum = sum.subtract(order.getDiscountTotal());
        return sum;
    }
}
