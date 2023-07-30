package com.badals.shop.service.dto;

import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.enumeration.ProductGroup;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.service.pojo.Meta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IndexProductDTO implements Serializable {

    private Boolean inStock;

    private String id;
    private String tenantId;
    private Long ref;

    private Long parent;

    @NotNull
    private String sku;

    private String upc;

    private String stock;
    //List<StockDTO> stock;

    /*
    @NotNull
*/
    private String price;
    private String api;

    private String listPrice;

    private String salePrice;

    private float discountInPercent = 0;

    /*    @NotNull*/
    private String currency;

    private String image;

    @Lob
    @JsonIgnore
    private List<Gallery> gallery;

    private LocalDate releaseDate;

    /*
    @NotNull
*/
    private Boolean active;

    @Lob
    private List<String> similarProducts;

    private String url;

    //@org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String title;

    private String description;

    //private Set<CategoryDTO> categories;

    private String brand;

    @JsonIgnore
    private ProductGroup group;

    private String slug;

    //@NotNull
    //private Instant updated;

    //@NotNull
    //private Instant created;
    @JsonIgnore
    private Condition condition;

    private Boolean isUsed;

    private Boolean isPrime;

    private Boolean availableForOrder;

    private BigDecimal weight;

    private BigDecimal volumeWeight;

    private String author;

    private String unit = "pcs";

    @JsonIgnore
    private Meta meta = new Meta();

    private String availability;
    private int hours;

    private List<String> hashtags;
    private String dial;

    // Variations
    List<String> variationDimensions;

    @JsonIgnore
    List<VariationOption> variationOptions;

    @JsonIgnore
    List<Variation> variations;

    @JsonIgnore
    List<Attribute> variationAttributes;

    @JsonIgnore
    VariationType variationType;

    Long merchantId;

    String browseNode;

    List<String> features;

    String rating;

    Boolean stub;

    String _locale;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndexProductDTO productDTO = (IndexProductDTO) o;
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
        return (
            "ProductDTO{" +
            "id=" +
            getId() +
            ", ref=" +
            getRef() +
            ", parent=" +
            getParent() +
            ", sku='" +
            getSku() +
            "'" +
            ", upc=" +
            getUpc() +
            ", price=" +
            getPrice() +
            ", image='" +
            getImage() +
            "'" +
            //", images='" + getImages() + "'" +
            ", releaseDate='" +
            getReleaseDate() +
            "'" +
            ", active='" +
            getActive() +
            "'" +
            ", similarProducts='" +
            getSimilarProducts() +
            "'" +
            ", url='" +
            getUrl() +
            "'" +
            ", title='" +
            getTitle() +
            "'" +
            ", brand='" +
            getBrand() +
            "'" +
            ", group='" +
            getGroup() +
            "'" +
            ", condition='" +
            getCondition() +
            "'" +
            ", isUsed='" +
            getIsUsed() +
            "'" +
            ", availableForOrder='" +
            getAvailableForOrder() +
            "'" +
            ", weight=" +
            getWeight() +
            ", volumeWeight=" +
            getVolumeWeight() +
            "}"
        );
    }

    public IndexProductDTO ref(long ref) {
        setRef(ref);
        return this;
    }

    public IndexProductDTO sku(String sku) {
        setSku(sku);
        return this;
    }

    public IndexProductDTO tenantId(String tenantId) {
        setTenantId(tenantId);
        return this;
    }
}
