package com.badals.shop.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.badals.shop.domain.converter.StringListConverter;
import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.enumeration.ProductGroup;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import com.badals.shop.xtra.IMerchantProduct;
import com.badals.shop.xtra.IProductLang;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SelectBeforeUpdate(false)
public class Product implements Serializable, IMerchantProduct {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private Long ref;

    private String slug;

    //private

    //@OneToMany(fetch = FetchType.LAZY)
    //@JoinColumn(
    //    name = "ref",
    //    referencedColumnName = "parent"
    //)
    //List<Product> children = new ArrayList<Product>();
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Column(name = "parent_id")
    private Long parentId;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id", referencedColumnName = "ref", insertable = false, updatable = false)
    private Product parent;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id",referencedColumnName = "ref")
    private Set<Product> children;



    @ManyToMany
    @JoinTable(
        name = "category_product",
        joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
        )
    private Set<Category> categories = new HashSet<>();

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Product addCategory(Category category) {
        this.categories.add(category);
        category.getProducts().add(this);
        return this;
    }

    public Product removeCategory(Category category) {
        this.categories.remove(category);
        category.getProducts().remove(this);
        return this;
    }


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

    @Convert(converter = StringListConverter.class)
    @Column(name = "similar_products")
    private List<String> similarProducts;

    @Column(name = "url")
    private String url;

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

    @OneToMany(mappedBy = "product", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<ProductLang> productLangs = new HashSet<>();

    public Set<MerchantStock> getMerchantStock() {
        return merchantStock;
    }

    public void setMerchantStock(Set<MerchantStock> merchantStock) {
        this.merchantStock = merchantStock;
    }

    @OneToMany(mappedBy = "product", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<MerchantStock> merchantStock = new HashSet<>();


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

    @Column(name = "price")
    BigDecimal price;

    @Column
    String currency;


    @ManyToOne
    @JoinColumn(name="tenant_id", insertable = false, updatable = false)
    Tenant tenant;

    @Column(name = "tenant_id")
    private Long tenantId;

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getActive() {
        return active;
    }



    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getVariationDimensions() {
        return variationDimensions;
    }

    @Override
    public Product tenantId(Long l) {
        this.tenantId = l;
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

    public Long getRef() {
        return ref;
    }

    public Product ref(Long ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(Long ref) {
        this.ref = ref;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Product slug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public Product sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUpc() {
        return upc;
    }

    public Product upc(String upc) {
        this.upc = upc;
        return this;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getImage() {
        return image;
    }

    public Product image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Gallery> getGallery() {
        return gallery;
    }

    public Product gallery(List<Gallery> gallery) {
        this.gallery = gallery;
        return this;
    }

    public void setGallery(List<Gallery> images) {
        this.gallery = images;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Product releaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean isActive() {
        return active;
    }

    public Product active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<String> getSimilarProducts() {
        return similarProducts;
    }

    public Product similarProducts(List<String> similarProducts) {
        this.similarProducts = similarProducts;
        return this;
    }

    public void setSimilarProducts(List<String> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public String getUrl() {
        return url;
    }

    public Product url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public Product title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public Product brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ProductGroup getGroup() {
        return group;
    }

    public Product group(ProductGroup group) {
        this.group = group;
        return this;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }

    public Instant getUpdated() {
        return updated;
    }

    public Product updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getCreated() {
        return created;
    }

    public Product created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Condition getCondition() {
        return condition;
    }

    public Product condition(Condition condition) {
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

    public Product variationType(VariationType variationType) {
        this.variationType = variationType;
        return this;
    }

    public Boolean isIsUsed() {
        return isUsed;
    }

    public Product isUsed(Boolean isUsed) {
        this.isUsed = isUsed;
        return this;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Boolean isAvailableForOrder() {
        return availableForOrder;
    }

    public Product availableForOrder(Boolean availableForOrder) {
        this.availableForOrder = availableForOrder;
        return this;
    }

    public void setAvailableForOrder(Boolean availableForOrder) {
        this.availableForOrder = availableForOrder;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Product weight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolumeWeight() {
        return volumeWeight;
    }

    public Product volumeWeight(BigDecimal volumeWeight) {
        this.volumeWeight = volumeWeight;
        return this;
    }

    @Override
    public void setPrice(Price price) {
        if (price == null)
            this.price = null;
        else {
            this.setPrice(price.getAmount());
            this.setCurrency(price.getCurrency());
        }
    }

    public void setVolumeWeight(BigDecimal volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

    public Set<ProductLang> getProductLangs() {
        return productLangs;
    }

    public Product productLangs(Set<ProductLang> productLangs) {
        this.productLangs = productLangs;
        return this;
    }

    public Product addProductLang(ProductLang productLang) {
        this.productLangs.add(productLang);
        productLang.setProduct(this);
        return this;
    }

    public Product removeProductLang(ProductLang productLang) {
        this.productLangs.remove(productLang);
        productLang.setProduct(null);
        return this;
    }

    public Product addMerchantStock(MerchantStock merchantStock) {
        this.merchantStock.add(merchantStock);
        merchantStock.setProduct(this);
        return this;
    }


    public void setProductLangs(Set<IProductLang> productLangs) {
        this.productLangs = productLangs.stream().map(a -> (ProductLang) a).collect(Collectors.toSet());
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Product getParent() {
        return parent;
    }

    public void setParent(Product parent) {
        this.parent = parent;
    }

    public Set<Product> getChildren() {
        return children;
    }

    public void setChildren(Set<Product> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", ref=" + getRef() +
            ", parent=" + getParent() +
            ", sku='" + getSku() + "'" +
            ", upc=" + getUpc() +
            ", price=" + getPrice() +
            ", image='" + getImage() + "'" +
            ", images='" + getGallery() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", active='" + isActive() + "'" +
            ", similarProducts='" + getSimilarProducts() + "'" +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", brand='" + getBrand() + "'" +
            ", group='" + getGroup() + "'" +
           // ", updated='" + getUpdated() + "'" +
            //", created='" + getCreated() + "'" +
            ", condition='" + getCondition() + "'" +
            ", isUsed='" + isIsUsed() + "'" +
            ", availableForOrder='" + isAvailableForOrder() + "'" +
            ", weight=" + getWeight() +
            ", volumeWeight=" + getVolumeWeight() +
            "}";
    }
}
