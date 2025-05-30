package com.badals.shop.service.dto;
import lombok.Data;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Payment} entity.
 */
@Data
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private String paymentMethod;

    private String authCode;

    private String cardNumber;

    @NotNull
    private BigDecimal amount;

    private String transactionId;

    @NotNull
    private Date createdDate;

    private Boolean voided;

    private Long orderId;

    private String orderReference;

    private String account;

    private String bankAccountNumber;
    private String         bankName;
    private String bankOwnerName;

    private Date processedDate;
    private Date settlementDate;

    private String invoiceNum;

    private String customer;
    private String cartId;
}
