package com.badals.shop.domain;

import com.badals.shop.domain.pojo.I18String;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * A Hashtag.
 */
@Entity
@Data
@Table(name = "hashtag", catalog = "profileshop")
public class ProfileHashtag extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "json")
    @Column(name = "lang", columnDefinition = "string")
    private List<I18String> langs;

    private String icon;

    private Integer position;

    @ManyToOne
    @JoinColumn(name="tenant_id", insertable = false, updatable = false)
    Tenant tenant;

    public ProfileHashtag icon(String icon) {
        this.icon = icon;
        return this;
    }
    public ProfileHashtag position(Integer position) {
        this.position = position;
        return this;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileHashtag)) {
            return false;
        }
        return id != null && id.equals(((ProfileHashtag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Hashtag{" +
            "id=" + getId() +

            "}";
    }
}
