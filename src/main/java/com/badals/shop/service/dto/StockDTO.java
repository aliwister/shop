package com.badals.shop.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Stock} entity.
 */
public class StockDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private Integer availability;

    @NotNull
    private Boolean allow_backorder;

    private Integer backorder_availability;

    private String link;

    private String location;

    private String store;

    private String cost;

    private String price;


    private Long merchantId;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Boolean isAllow_backorder() {
        return allow_backorder;
    }

    public void setAllow_backorder(Boolean allow_backorder) {
        this.allow_backorder = allow_backorder;
    }

    public Integer getBackorder_availability() {
        return backorder_availability;
    }

    public void setBackorder_availability(Integer backorder_availability) {
        this.backorder_availability = backorder_availability;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (stockDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MerchantStockDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", availability=" + getAvailability() +
            ", allow_backorder='" + isAllow_backorder() + "'" +
            ", backorder_availability=" + getBackorder_availability() +
            ", link='" + getLink() + "'" +
            ", location='" + getLocation() + "'" +
            ", store='" + getStore() + "'" +
            ", cost='" + getCost() + "'" +
            ", price='" + getPrice() + "'" +
            ", merchant=" + getMerchantId() +
            ", product=" + getProductId() +
            "}";
    }
}
