package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantPayment;
import com.badals.shop.graph.PaymentResponse;
import com.badals.shop.repository.TenantPaymentRepository;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.service.mapper.TenantPaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantPaymentService {
   private final Logger log = LoggerFactory.getLogger(TenantPaymentService.class);
   private final TenantPaymentRepository paymentRepository;
   private final TenantPaymentMapper paymentMapper;

   public TenantPaymentService(TenantPaymentRepository paymentRepository, TenantPaymentMapper paymentMapper) {
      this.paymentRepository = paymentRepository;
      this.paymentMapper = paymentMapper;
   }

   @Transactional(readOnly = true)
   public Optional<PaymentDTO> findOne(Long id) {
      log.debug("Request to get Order : {}", id);
      return paymentRepository.findById(id)
              .map(paymentMapper::toDto);
   }


   public PaymentDTO addPayment(Long orderId, BigDecimal amount, String paymentMethod, String authCode, String currency) {
      TenantPayment p = new TenantPayment();
      p.amount(amount).order(new TenantOrder(orderId)).paymentMethod(paymentMethod).authCode(authCode).currency(currency);
      p = paymentRepository.save(p);
      return paymentMapper.toDto(p);
   }

   public List<PaymentDTO> findForOrder(Long orderId) {
      return paymentRepository.findAllByOrderId(orderId).stream().map(paymentMapper::toDto).collect(Collectors.toList());
   }

   public PaymentDTO addRefund(Long orderId, BigDecimal amount, String authCode, String bankName, String bankAccountNumber, String bankOwnerName, Long ref, String paymentMethod, String currency) {
      TenantPayment p = new TenantPayment();
      p.amount(amount).order(new TenantOrder(orderId)).paymentMethod(paymentMethod).authCode(authCode).bankAccountNumber(bankAccountNumber).bankName(bankName).bankOwnerName(bankOwnerName).ref(ref).currency(currency);
      p = paymentRepository.save(p);
      return paymentMapper.toDto(p);
   }

   public PaymentResponse findForTable(List<String> paymentMethods, Integer offset, Integer limit, String maxAmount, Date from, Date to, Long customerId, String accountCode, Boolean unsettledOnly) {
      Page<TenantPayment> payments = paymentRepository.findForTable(paymentMethods, from, to, customerId, accountCode, maxAmount, unsettledOnly, PageRequest.of((int) offset/limit,limit));
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

   @Modifying
   @Transactional
   public void voidPayment(Long id) {
      TenantPayment p = paymentRepository.getOne(id);
      if(p.getProcessedDate() != null || p.getSettlementDate() != null)
         throw new IllegalStateException("Cannot void a processed TenantPayment");
      p.setVoided(true);
      paymentRepository.save(p);
   }
}
