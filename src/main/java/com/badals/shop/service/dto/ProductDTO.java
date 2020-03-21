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
import com.badals.shop.domain.pojo.*;
import com.badals.shop.service.pojo.Meta;
import lombok.Data;

/**
 * A DTO for the {@link com.badals.shop.domain.Product} entity.
 */

@Data
public class ProductDTO implements Serializable {

    private Long id;

    private Long ref;

    private Long parent;

    @NotNull
    private String sku;

    private String upc;

    @NotNull
    private String price;

    private String salePrice;

    private float discountInPercent = 0;

    @NotNull
    private String currency;

    private String image;

    @Lob
    private List<Gallery> gallery;

    private LocalDate releaseDate;

    @NotNull
    private Boolean active;

    @Lob
    private List<String> similarProducts;

    private String url;

    @NotNull
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

    private Boolean availableForOrder;

    private BigDecimal weight;

    private BigDecimal volumeWeight;

    private ProductType type = ProductType.CHILDREN_BOOKS;

    private String author = "Fatma Anwar";

    private String unit = "pcs";

   private Meta meta = new Meta();

   private String availability;
   private int hours;

    // Variations
    List<String> variationDimensions;
    List<VariationOption> variationOptions;
    List<Variation> variations;
    List<Attribute> variationAttributes;

    List<MerchantStockDTO> merchantStock;

    String browseNode;

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
