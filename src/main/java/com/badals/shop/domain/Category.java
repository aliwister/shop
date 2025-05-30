package com.badals.shop.domain;
import com.badals.shop.domain.tenant.TenantProduct;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.badals.shop.domain.enumeration.ProductGroup;

/**
 * A Category.
 */
@Entity
@Table(catalog="shop", name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    public Category() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "icon")
    private String icon;

    @Column(name = "slug")
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_group")
    private ProductGroup group;

    @ManyToOne
    @JsonIgnoreProperties("children")
    private Category parent;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id",referencedColumnName = "id")
    private Collection<Category> children;

    public Category(Long id) {
        this.id = id;
    }

    public Collection<Category> getChildren() {
        return children;
    }

    public void setChildren(Collection<Category> children) {
        this.children = children;
    }

    @ManyToMany(mappedBy = "categories")
    private Set<TenantProduct> products;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Category title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public Category icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSlug() {
        return slug;
    }

    public Category slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ProductGroup getGroup() {
        return group;
    }

    public Category group(ProductGroup group) {
        this.group = group;
        return this;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }

    public Category getParent() {
        return parent;
    }

    public Category parent(Category category) {
        this.parent = category;
        return this;
    }

    public void setParent(Category category) {
        this.parent = category;
    }

    public Set<TenantProduct> getProducts() {
        return products;
    }

    public Category products(Set<TenantProduct> products) {
        this.products = products;
        return this;
    }

    public Category addProduct(TenantProduct product) {
        this.products.add(product);
        //product.getCategories().add(this);
        return this;
    }

    public Category removeProduct(TenantProduct product) {
        this.products.remove(product);
        //product.getCategories().remove(this);
        return this;
    }

    public void setProducts(Set<TenantProduct> products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", icon='" + getIcon() + "'" +
            ", slug='" + getSlug() + "'" +
            ", group='" + getGroup() + "'" +
            "}";
    }

    @ManyToMany(mappedBy = "categories")
    private Collection<TenantProduct> products2;

    public Collection<TenantProduct> getProducts2() {
        return products2;
    }

    public void setProducts2(Collection<TenantProduct> products2) {
        this.products2 = products2;
    }
}
