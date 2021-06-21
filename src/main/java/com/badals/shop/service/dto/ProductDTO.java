package com.badals.shop.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.Lob;

import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.enumeration.ProductGroup;
//import com.badals.shop.domain.enumeration.ProductType;
import com.badals.shop.domain.enumeration.ProductType;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import com.badals.shop.service.pojo.Meta;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A DTO for the {@link com.badals.shop.domain.Product} entity.
 */

@Data
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductDTO implements Serializable {

   private Boolean inStock;

    private Long id;

    private Long ref;

    private Long parent;

    @NotNull
    private String sku;

    private String upc;

/*
    @NotNull
*/
    private String price;

    private String salePrice;

    private float discountInPercent = 0;

/*    @NotNull*/
    private String currency;

    private String image;

    @Lob
    private List<Gallery> gallery;

    private LocalDate releaseDate;

/*
    @NotNull
*/
    private Boolean active;

    @Lob
    private List<String> similarProducts;

    private String url;

    @NotNull
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String title;

    private String description;

    private Set<CategoryDTO> categories;


    private String brand;

    private ProductGroup group;

    private String slug;

    //@NotNull
    //private Instant updated;

    //@NotNull
    //private Instant created;

    private Condition condition;

    private Boolean isUsed;

    private Boolean isPrime;

    private Boolean availableForOrder;

    private BigDecimal weight;

    private BigDecimal volumeWeight;

    private String author = "Fatma Anwar";

    private String unit = "pcs";

   private Meta meta = new Meta();

   private String availability;
   private int hours;

   private List<String> hashtags;
   private String dial;

    // Variations
    List<String> variationDimensions;
    List<VariationOption> variationOptions;
    List<Variation> variations;
    List<Attribute> variationAttributes;

    List<MerchantStockDTO> merchantStock;

    VariationType variationType;

    Long merchantId;

    String browseNode;

    List<String> features;

    String rating;

    String _locale;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", ref=" + getRef() +
            ", parent=" + getParent() +
            ", sku='" + getSku() + "'" +
            ", upc=" + getUpc() +
            ", price=" + getPrice() +
            ", image='" + getImage() + "'" +
            //", images='" + getImages() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", active='" + getActive() + "'" +
            ", similarProducts='" + getSimilarProducts() + "'" +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", brand='" + getBrand() + "'" +
            ", group='" + getGroup() + "'" +
            ", condition='" + getCondition() + "'" +
            ", isUsed='" + getIsUsed() + "'" +
            ", availableForOrder='" + getAvailableForOrder() + "'" +
            ", weight=" + getWeight() +
            ", volumeWeight=" + getVolumeWeight() +
            "}";
    }
}
