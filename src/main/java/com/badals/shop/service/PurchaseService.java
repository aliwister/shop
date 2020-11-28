package com.badals.shop.service;

import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.OrderResponse;
import com.badals.shop.domain.pojo.PurchaseResponse;
import com.badals.shop.repository.MerchantRepository;
import com.badals.shop.repository.OrderItemRepository;
import com.badals.shop.repository.PurchaseItemRepository;
import com.badals.shop.repository.PurchaseRepository;
import com.badals.shop.repository.projection.PurchaseQueue;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.OrderItemDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.service.dto.PurchaseItemDTO;
import com.badals.shop.service.mapper.MerchantMapper;
import com.badals.shop.service.mapper.OrderItemMapper;
import com.badals.shop.service.mapper.PurchaseItemMapper;
import com.badals.shop.service.mapper.PurchaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PurchaseItemRepository purchaseItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    private final PurchaseMapper purchaseMapper;
    private final PurchaseItemMapper purchaseItemMapper;
    private final OrderItemMapper orderItemMapper;
    //private final PurchaseItemMapper purchaseItemMapper;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository, OrderItemRepository orderItemRepository, MerchantRepository merchantRepository, MerchantMapper merchantMapper, PurchaseMapper purchaseMapper, PurchaseItemMapper purchaseItemMapper, OrderItemMapper orderItemMapper) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
        this.purchaseMapper = purchaseMapper;
        this.purchaseItemMapper = purchaseItemMapper;
        this.orderItemMapper = orderItemMapper;
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

    public PurchaseResponse findForPurchaseList(List<OrderState> orderState, Integer offset, Integer limit, String searchText) {
        Page<Purchase> orders = purchaseRepository.findForPurchaseList(/*orderState, */PageRequest.of((int) offset/limit,limit));
        PurchaseResponse response = new PurchaseResponse();
        response.setTotal(orders.getNumber());
        response.setItems(orders.getContent().stream().map(purchaseMapper::toDto).collect(Collectors.toList()));
        response.setHasMore(orders.hasNext());
        return response;
    }

    public PurchaseDTO updatePurchase(PurchaseDTO dto, List<PurchaseItemDTO> items) {
        //
        Purchase purchase = purchaseRepository.findForUpdate(dto.getId()).orElse(null);
        if(purchase != null) {
            purchase.getPurchaseItems().forEach(x -> x.getOrderItems().forEach( o -> orderItemRepository.save(o.purchaseItem(null))));
            purchase.getPurchaseItems().clear();
            purchase = purchaseRepository.save(purchase);
        }


        if(purchase == null) {
            //throw InvalidPurchaseException("Product doesn't exist");
            purchase = purchaseMapper.toEntity(dto);
        }

        purchase = purchaseRepository.save(purchase);



        int sequence = 1;
        double sum = 0;
        for(PurchaseItemDTO i : items) {
            PurchaseItem pi = purchaseItemMapper.toEntity(i);
            pi.setSequence(sequence++);
            pi.setPurchase(purchase);
            purchaseItemRepository.save(pi);
            for (OrderItemDTO oi : i.getOrderItems()) {
                OrderItem o = orderItemRepository.getOne(oi.getId());
                if (o.getQuantity().intValue() > pi.getQuantity().intValue()) {
                    OrderItem oNew = orderItemMapper.toEntity(orderItemMapper.toDto(o));
                    oNew.setQuantity(pi.getQuantity().intValue());
                    o.setQuantity(o.getQuantity().intValue() - pi.getQuantity().intValue());
                    o.setLineTotal(BigDecimal.valueOf(Math.round(o.getQuantity()*o.getPrice().doubleValue()*10)/10.0));
                    oNew.setProduct(o.getProduct());
                    oNew.setSku(o.getSku());
                    oNew.setId(null);
                    oNew.setSequence(o.getOrder().getOrderItems().size()+1);
                    oNew.setLineTotal(BigDecimal.valueOf(Math.round(oNew.getQuantity()*o.getPrice().doubleValue()*10)/10.0));
                    orderItemRepository.save(o);
                    orderItemRepository.save(oNew);
                    o = oNew;
                }
                orderItemRepository.save(o.purchaseItem(pi));
            }
            sum += i.getQuantity().doubleValue() * i.getPrice().doubleValue();
        }
        BigDecimal subtotal = BigDecimal.valueOf(sum);
        purchase.setSubtotal(subtotal);
        purchase.setTotal(calculateTotal(purchase,subtotal));
        purchase.setDeliveryTotal(dto.getDeliveryTotal());
        purchase.setDiscountTotal(dto.getDiscountTotal());
        purchase.setTaxesTotal(dto.getTaxesTotal());
        purchase.setRef(dto.getRef());
        purchase.setCurrency(dto.getCurrency());
        if(dto.getMerchantId() != null)
            purchase.setMerchant(merchantRepository.getOne(dto.getMerchantId()));

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

    public BigDecimal calculateTotal(Purchase order, BigDecimal subtotal) {
        BigDecimal total = subtotal;
         if(order.getDeliveryTotal() != null)
            total = total.add(order.getDeliveryTotal());
        if(order.getTaxesTotal() != null)
            total = total.add(order.getTaxesTotal());
        if(order.getDiscountTotal()!= null)
            total = total.subtract(order.getDiscountTotal());
        return total;
    }

    public List<PurchaseQueue> findUnshippedPurchases() {
        return purchaseRepository.findUnshipped();
    }

    public PurchaseDTO setStatus(Long id, OrderState state) {
        Purchase order = purchaseRepository.getOne(id);
        order.setOrderState(state);
        order = purchaseRepository.save(order);
        return purchaseMapper.toDto(order);
    }

    public PurchaseDTO close(Long id, String comment) {
        Purchase order = purchaseRepository.getOne(id);
        order.setOrderState(OrderState.CLOSED);
        order = purchaseRepository.save(order);
        return purchaseMapper.toDto(order);
    }

    public PurchaseDTO cancel(Long id, String comment) {
        Purchase order = purchaseRepository.getOne(id);
        order.setOrderState(OrderState.CANCELLED);
        order.setTotal(BigDecimal.ZERO);
        order = purchaseRepository.save(order);
        return purchaseMapper.toDto(order);
    }
}
