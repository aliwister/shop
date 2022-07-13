package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A OrderPayment.
 */
@Entity
@Data
@Table(catalog="profileshop", name = "payment")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantPayment extends Auditable implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "auth_code")
    private String authCode;

    @Column(name = "card_number")
    private String cardNumber;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_owner_name")
    private String bankOwnerName;


    @Column(name = "currency")
    private String currency;

    //ALTER TABLE shop.payment ADD track_id BIGINT NULL;
    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "void")
    private Boolean voided = false;

    @ManyToOne
    @JsonIgnoreProperties("orderPayments")
    private TenantOrder order;

    public TenantPayment paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }


    public TenantPayment authCode(String authCode) {
        this.authCode = authCode;
        return this;
    }
    public TenantPayment cardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public TenantPayment amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TenantPayment transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }


    public TenantPayment order(TenantOrder order) {
        this.order = order;
        return this;
    }

    @Column(name="tenant_id")
    private String tenantId;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantPayment)) {
            return false;
        }
        return id != null && id.equals(((TenantPayment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrderPayment{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", authCode='" + getAuthCode() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            ", amount=" + getAmount() +
            ", transactionId='" + getTransactionId() + "'" +
            ", created_date='" + getCreatedDate() + "'" +
            "}";
    }
}
