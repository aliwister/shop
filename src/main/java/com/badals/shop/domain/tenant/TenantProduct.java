package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.Category;
import com.badals.shop.domain.converter.StringListConverter;
import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.enumeration.ProductGroup;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

/**
 * A Product.
 */
@Entity
@Data
@Table(name = "product", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@SelectBeforeUpdate(false)
public class TenantProduct implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Getter
    @Setter
    @Column(name = "ref")
    public String ref;

    @Getter
    @Setter
    private String slug;

    @Column(name = "parent_id")
    private String parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "ref", insertable = false, updatable = false)
    private TenantProduct parent;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id", referencedColumnName = "ref")
    private Set<TenantProduct> children = new HashSet<>();
    ;

    @NotNull
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "upc")
    private String upc;

    @Column(name = "api")
    private String api;

    @Column(name = "pricing_api")
    private String pricingApi;

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
    private Boolean oversize = false;

    @Column(name = "stub", nullable = false)
    private Boolean stub;

    @Column(name = "in_stock", nullable = false)
    private Boolean inStock;

    @Convert(converter = StringListConverter.class)
    @Column(name = "similar_products")
    private List<String> similarProducts;

    @Type(type = "json")
    @Column(name = "hashtags", columnDefinition = "string")
    private List<String> hashtags;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "brand")
    private String brand;

    @Column(name = "url")
    private String url;

    @Column(name = "jhi_group")
    private ProductGroup group;

    //@NotNull
    @Column(name = "last_modified_date", nullable = false, updatable = false, insertable = false)
    private Instant updated;

    //@NotNull
    @Column(name = "created_date", nullable = false, updatable = false, insertable = false)
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

    @OneToMany(mappedBy = "product", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<TenantStock> stock = new HashSet<>();

    @Column(name = "expires")
    private Instant expires;

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
    @Getter
    @Setter
    @Column(name = "description", columnDefinition = "string")
    List<TenantProductLang> langs = new ArrayList<>();

    @Type(type = "json")
    @Getter
    @Setter
    @Column(name = "attributes", columnDefinition = "string")
    List<AttributesLang> attributes;

    @Type(type = "json")
    @Getter
    @Setter
    @Column(name = "delivery_profiles", columnDefinition = "string")
    List<String> deliveryProfiles;

    @Type(type = "json")
    @Column(name = "list_price")
    PriceMap listPrice;

    @Type(type = "json")
    @Column(name = "price")
    PriceMap price;


    @Column
    @Getter
    @Setter
    String rating;


/*    @ManyToOne
    @JoinColumn(name="merchant_id", insertable = false, updatable = false)
    Merchant merchant;*/

    @Getter
    @Setter
    @Column(name = "merchant_id")
    private Long merchantId;

/*    @ManyToOne
    @JoinColumn(name="tenant_id", referencedColumnName = "name", insertable = false, updatable = false)
    Tenant tenant;*/

    @Getter
    @Setter
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "deleted")
    private Boolean deleted;

/*

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }*/

    public TenantProduct merchantId(Long l) {
        this.merchantId = l;
        return this;
    }

    public TenantProduct ref(String ref) {
        this.ref = ref;
        return this;
    }

    public TenantProduct slug(String slug) {
        this.slug = slug;
        return this;
    }

    public TenantProduct sku(String sku) {
        this.sku = sku;
        return this;
    }

    public TenantProduct upc(String upc) {
        this.upc = upc;
        return this;
    }

    public TenantProduct image(String image) {
        this.image = image;
        return this;
    }

    public TenantProduct gallery(List<Gallery> gallery) {
        this.gallery = gallery;
        return this;
    }

    public TenantProduct releaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public TenantProduct active(Boolean active) {
        this.active = active;
        return this;
    }

    public TenantProduct similarProducts(List<String> similarProducts) {
        this.similarProducts = similarProducts;
        return this;
    }

    public TenantProduct title(String title) {
        this.title = title;
        return this;
    }

    public TenantProduct brand(String brand) {
        this.brand = brand;
        return this;
    }

    public TenantProduct group(ProductGroup group) {
        this.group = group;
        return this;
    }

    public TenantProduct updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public TenantProduct created(Instant created) {
        this.created = created;
        return this;
    }

    public TenantProduct condition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public TenantProduct variationType(VariationType variationType) {
        this.variationType = variationType;
        return this;
    }

    public TenantProduct isUsed(Boolean isUsed) {
        this.isUsed = isUsed;
        return this;
    }

    public TenantProduct availableForOrder(Boolean availableForOrder) {
        this.availableForOrder = availableForOrder;
        return this;
    }

    public TenantProduct weight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public TenantProduct volumeWeight(BigDecimal volumeWeight) {
        this.volumeWeight = volumeWeight;
        return this;
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

    public TenantProduct parent(TenantProduct master) {
        this.parent = master;
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

    @ManyToMany
    @JoinTable(
        name = "category_product",
        joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<Category> categories = new HashSet<>();

    public TenantProduct addCategory(Category category) {
        this.categories.add(category);
        category.getProducts().add(this);
        return this;
    }

    public TenantProduct removeCategory(Category category) {
        this.categories.remove(category);
        category.getProducts().remove(this);
        return this;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public TenantProduct stub(boolean b) {
        stub = b;
        return this;
    }

    public TenantProduct inStock(Boolean b) {
        inStock = b;
        return this;
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
        if (volumeWeight != null && volumeWeight.compareTo(weight) == 1) {
            return volumeWeight;
        }
        return weight;
    }

    public void addTag(String tag) {
        if (this.hashtags == null)
            this.hashtags = new ArrayList<>();

        if (this.hashtags.contains(tag))
            return;

        this.hashtags.add(tag);
    }

    public void removeTag(String tag) {
        if (this.hashtags == null)
            return;
        this.hashtags.remove(tag);
    }
}
