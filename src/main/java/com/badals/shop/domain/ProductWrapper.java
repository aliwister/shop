package com.badals.shop.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Product.
 */
@Table(schema="shop",name="products")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
//Alp
@SQLInsert(sql="INSERT INTO products (body, is_parent, parent, rewrite, sku, updated, view_count) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE body = body, parent = parent, rewrite = rewrite, sku = sku, view_count = view_count + 1, is_parent = is_parent" )
@Entity
public class ProductWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "sku")
    private String sku;

    @Column(name = "rewrite")
    private String rewrite;

    @Column(name = "parent")
    private Long parent;

    @Type(type = "json")
    @Column(columnDefinition = "string")
    private Product body;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "is_parent")



    public Product getBody() {
        return body;
    }

    public void setBody(Product body) {
        this.body = body;
    }


    private Boolean is_parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUpdated() {
        return updated;
    }

    public ProductWrapper updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getSku() {
        return sku;
    }

    public ProductWrapper sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRewrite() {
        return rewrite;
    }

    public ProductWrapper rewrite(String rewrite) {
        this.rewrite = rewrite;
        return this;
    }

    public void setRewrite(String rewrite) {
        this.rewrite = rewrite;
    }

    public Long getParent() {
        return parent;
    }

    public ProductWrapper parent(Long parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }


    public Integer getViewCount() {
        return viewCount;
    }

    public ProductWrapper viewCount(Integer view_count) {
        this.viewCount = view_count;
        return this;
    }

    public void setViewCount(Integer view_count) {
        this.viewCount = view_count;
    }

    public Boolean isIs_parent() {
        return is_parent;
    }

    public ProductWrapper is_parent(Boolean is_parent) {
        this.is_parent = is_parent;
        return this;
    }

    public void setIs_parent(Boolean is_parent) {
        this.is_parent = is_parent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove



    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", updated='" + getUpdated() + "'" +
            ", sku='" + getSku() + "'" +
            ", rewrite='" + getRewrite() + "'" +
            ", parent=" + getParent() +
            ", body='" + getBody() + "'" +
            ", view_count=" + getViewCount() +
            ", is_parent='" + isIs_parent() + "'" +
            "}";
    }
}
