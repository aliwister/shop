package com.badals.shop.domain.tenant;

import com.badals.shop.domain.Merchant;
import com.badals.shop.domain.Tenant;
import com.badals.shop.domain.converter.StringListConverter;
import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.enumeration.ProductGroup;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product",  catalog = "profileshop")
@SelectBeforeUpdate(false)
public class TenantProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Getter @Setter @Column(name = "ref")
    public String ref;

    @Getter @Setter
    private String slug;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "parent_id")
    private String parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id", referencedColumnName = "ref", insertable = false, updatable = false)
    private TenantProduct parent;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id",referencedColumnName = "ref")
    private Set<TenantProduct> children = new HashSet<>();;

    @NotNull
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "upc")
    private String upc;

    @Column(name = "unit")
    private String unit;

    @Column(name = "image")
    private String image;

    @Type(type = "json")
    @Column(name = "images", columnDefinition = "string")
    List<Gallery> gallery;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    private Boolean oversize= false;

    public Boolean getOversize() {
        return oversize;
    }

    public void setOversize(Boolean oversize) {
        this.oversize = oversize;
    }

    public Boolean getStub() {
        return stub;
    }

    public void setStub(Boolean stub) {
        this.stub = stub;
    }



    @Column(name = "stub", nullable = false)
    private Boolean stub;

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @Column(name = "in_stock", nullable = false)
    private Boolean inStock;



    public TenantProduct stub(boolean b) {
        stub = b;
        return this;
    }

    public TenantProduct inStock(Boolean b) {
        inStock = b;
        return this;
    }

    @Convert(converter = StringListConverter.class)
    @Column(name = "similar_products")
    private List<String> similarProducts;

    @Type(type = "json")
    @Column(name = "hashtags", columnDefinition = "string")
    private List<String> hashtags;

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "brand")
    private String brand;

    @Column(name = "jhi_group")
    private ProductGroup group;

    //@NotNull
    @Column(name = "last_modified_date", nullable = false, updatable=false, insertable=false)
    private Instant updated;

    //@NotNull
    @Column(name = "created_date", nullable = false, updatable=false, insertable=false)
    private Instant created;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_condition")
    private Condition condition;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private VariationType variationType;

    @Column(name = "is_used")
    private Boolean isUsed;

    @Column(name = "available_for_order")
    private Boolean availableForOrder;

    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @Column(name = "volume_weight", precision = 21, scale = 2)
    private BigDecimal volumeWeight;
/*
    @OneToMany(mappedBy = "product", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<ProductLang> productLangs = new HashSet<>();*/

    public Set<TenantStock> getStock() {
        return stock;
    }

    public void setStock(Set<TenantStock> stock) {
        this.stock = stock;
    }

    @OneToMany(mappedBy = "product", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<TenantStock> stock = new HashSet<>();

    @Column(name = "expires")
    private Instant expires;

    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Variations
    @Type(type = "json")
    @Column(name = "variation_dimensions", columnDefinition = "string")
    List<String> variationDimensions;

    @Type(type = "json")
    @Column(name = "variation_options", columnDefinition = "string")
    List<VariationOption> variationOptions;

    @Type(type = "json")
    @Column(name = "variations", columnDefinition = "string")
    List<Variation> variations;

    @Type(type = "json")
    @Column(name = "variation_attributes", columnDefinition = "string")
    List<Attribute> variationAttributes;

    @Type(type = "json")
    @Getter @Setter @Column(name = "description", columnDefinition = "string")
    List<TenantProductLang> langs;


    @Type(type = "json")
    @Column(name = "list_price")
    Price listPrice;


    @Column @Getter @Setter
    String rating;


    @ManyToOne
    @JoinColumn(name="merchant_id", insertable = false, updatable = false)
    Merchant merchant;

    @Getter @Setter @Column(name = "merchant_id")
    private Long merchantId;

    @ManyToOne
    @JoinColumn(name="tenant_id", insertable = false, updatable = false)
    Tenant tenant;

    @Getter @Setter @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "deleted")
    private Boolean deleted;



    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }


    public Boolean getActive() {
        return active;
    }



    public Price getPrice() {
        return listPrice;
    }

    public void setPrice(Price price) {
        this.listPrice = price;
    }


    public List<String> getVariationDimensions() {
        return variationDimensions;
    }

    public TenantProduct merchantId(Long l) {
        this.merchantId = l;
        return this;
    }

    public void setVariationDimensions(List<String> variationDimensions) {
        this.variationDimensions = variationDimensions;
    }

    public List<VariationOption> getVariationOptions() {
        return variationOptions;
    }

    public void setVariationOptions(List<VariationOption> variationOptions) {
        this.variationOptions = variationOptions;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    public void setVariations(List<Variation> variations) {
        this.variations = variations;
    }

    public List<Attribute> getVariationAttributes() {
        return variationAttributes;
    }

    public void setVariationAttributes(List<Attribute> variationAttributes) {
        this.variationAttributes = variationAttributes;
    }



    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public TenantProduct ref(String ref) {
        this.ref = ref;
        return this;
    }



    public TenantProduct slug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public TenantProduct sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUpc() {
        return upc;
    }

    public TenantProduct upc(String upc) {
        this.upc = upc;
        return this;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getImage() {
        return image;
    }

    public TenantProduct image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Gallery> getGallery() {
        return gallery;
    }

    public TenantProduct gallery(List<Gallery> gallery) {
        this.gallery = gallery;
        return this;
    }

    public void setGallery(List<Gallery> images) {
        this.gallery = images;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public TenantProduct releaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean isActive() {
        return active;
    }

    public TenantProduct active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<String> getSimilarProducts() {
        return similarProducts;
    }

    public TenantProduct similarProducts(List<String> similarProducts) {
        this.similarProducts = similarProducts;
        return this;
    }

    public void setSimilarProducts(List<String> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public String getTitle() {
        return title;
    }

    public TenantProduct title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public TenantProduct brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ProductGroup getGroup() {
        return group;
    }

    public TenantProduct group(ProductGroup group) {
        this.group = group;
        return this;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }

    public Instant getUpdated() {
        return updated;
    }

    public TenantProduct updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getCreated() {
        return created;
    }

    public TenantProduct created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Condition getCondition() {
        return condition;
    }

    public TenantProduct condition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public VariationType getVariationType() {
        return variationType;
    }

    public void setVariationType(VariationType variationType) {
        this.variationType = variationType;
    }

    public TenantProduct variationType(VariationType variationType) {
        this.variationType = variationType;
        return this;
    }

    public Boolean isIsUsed() {
        return isUsed;
    }

    public TenantProduct isUsed(Boolean isUsed) {
        this.isUsed = isUsed;
        return this;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Boolean isAvailableForOrder() {
        return availableForOrder;
    }

    public TenantProduct availableForOrder(Boolean availableForOrder) {
        this.availableForOrder = availableForOrder;
        return this;
    }

    public void setAvailableForOrder(Boolean availableForOrder) {
        this.availableForOrder = availableForOrder;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public TenantProduct weight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolumeWeight() {
        return volumeWeight;
    }

    public TenantProduct volumeWeight(BigDecimal volumeWeight) {
        this.volumeWeight = volumeWeight;
        return this;
    }

    public void setVolumeWeight(BigDecimal volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

/*    public Set<ProductLang> getProductLangs() {
        return productLangs;
    }

    public ProfileProduct productLangs(Set<ProductLang> productLangs) {
        this.productLangs = productLangs;
        return this;
    }*/

    public TenantProduct addStock(TenantStock stock) {
        this.stock.add(stock);
        stock.setProduct(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public TenantProduct getParent() {
        return parent;
    }

    public void setParent(TenantProduct parent) {
        this.parent = parent;
    }

    public TenantProduct parent(TenantProduct master) {
        this.parent = parent;
        return this;
    }

    public Set<TenantProduct> getChildren() {
        return children;
    }

    public void setChildren(Set<TenantProduct> children) {
        this.children = children;
    }

    public void addChild(TenantProduct child) {
        child.setVariationType(VariationType.CHILD);
        child.setMerchantId(this.merchantId);
        this.children.add(child);
        child.setParentId(this.ref);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantProduct)) {
            return false;
        }
        return id != null && id.equals(((TenantProduct) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", ref=" + ref +
                ", slug='" + slug + '\'' +
                ", parentId=" + parentId +
                '}';
    }


    public TenantProduct variationAttributes(List<Attribute> value) {
        this.setVariationAttributes(value);
        return this;
    }

    public void removeChild(TenantProduct child) {
        this.children.remove(child);
    }

    public BigDecimal getComputedWeight() {
        if(volumeWeight != null && volumeWeight.compareTo(weight) == 1) {
            return volumeWeight;
        }
        return weight;
    }
}
