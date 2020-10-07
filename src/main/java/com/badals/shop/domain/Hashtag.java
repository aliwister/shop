package com.badals.shop.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Hashtag.
 */
@Entity
@Table(name = "hashtag")
public class Hashtag extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "en")
    private String en;

    @Column(name = "ar")
    private String ar;

    private String icon;

    private Integer position;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEn() {
        return en;
    }

    public Hashtag en(String en) {
        this.en = en;
        return this;
    }
    public Hashtag icon(String icon) {
        this.icon = icon;
        return this;
    }
    public Hashtag position(Integer position) {
        this.position = position;
        return this;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getAr() {
        return ar;
    }

    public Hashtag ar(String ar) {
        this.ar = ar;
        return this;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hashtag)) {
            return false;
        }
        return id != null && id.equals(((Hashtag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Hashtag{" +
            "id=" + getId() +
            ", en='" + getEn() + "'" +
            ", ar='" + getAr() + "'" +
            "}";
    }
}
