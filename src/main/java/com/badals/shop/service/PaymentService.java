package com.badals.shop.service;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.Payment;
import com.badals.shop.domain.pojo.OrderResponse;
import com.badals.shop.domain.pojo.PaymentResponse;
import com.badals.shop.repository.PaymentRepository;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.service.mapper.PaymentMapper;
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
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll().stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id)
            .map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

   public PaymentDTO addPayment(Long orderId, BigDecimal amount, String paymentMethod, String authCode) {
        Payment p = new Payment();
        p.amount(amount).order(new Order(orderId)).paymentMethod(paymentMethod).authCode(authCode);
        p = paymentRepository.save(p);
        return paymentMapper.toDto(p);
   }

   public List<PaymentDTO> findForOrder(Long orderId) {
       return paymentRepository.findAllByOrderId(orderId).stream().map(paymentMapper::toDto).collect(Collectors.toList());
   }

   public PaymentDTO addRefund(Long orderId, BigDecimal amount, String authCode, String bankName, String bankAccountNumber, String bankOwnerName, Long ref, String paymentMethod) {
      Payment p = new Payment();
      p.amount(amount).order(new Order(orderId)).paymentMethod(paymentMethod).authCode(authCode).bankAccountNumber(bankAccountNumber).bankName(bankName).bankOwnerName(bankOwnerName).ref(ref);
      p = paymentRepository.save(p);
      return paymentMapper.toDto(p);
   }

   public PaymentResponse findForTable(List<String> paymentMethods, Integer offset, Integer limit, String maxAmount, Date from, Date to, Long customerId, String accountCode, Boolean unsettledOnly) {
      Page<Payment> payments = paymentRepository.findForTable(paymentMethods, from, to, customerId, accountCode, maxAmount, unsettledOnly, PageRequest.of((int) offset/limit,limit));
      PaymentResponse response = new PaymentResponse();
      response.setItems(payments.getContent().stream().map(paymentMapper::toDto).collect(Collectors.toList()));
      Long total = payments.getTotalElements();
      response.setTotal(total.intValue());
      response.setHasMore((limit+offset) < total);
      return response;
   }

   public void setSettlementDate(ArrayList<Long> payments, Date date) {
       paymentRepository.setSettlementDate(payments, date);
   }

   public void setProcessedDate(ArrayList<Long> payments, Date date) {
       paymentRepository.setProcessedDate(payments, date);
   }

   public void setAccountingCode(ArrayList<Long> payments, String code) {
       paymentRepository.setAccountingCode(payments, code);
   }
}
