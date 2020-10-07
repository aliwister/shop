package com.badals.shop.service.dto;
import com.badals.shop.domain.pojo.ProductResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Hashtag} entity.
 */
@Data
public class HashtagDTO implements Serializable {

    private Long id;

    private String en;

    private String ar;

    private String icon;

    private Integer position;

    private ProductResponse products;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HashtagDTO hashtagDTO = (HashtagDTO) o;
        if (hashtagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hashtagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HashtagDTO{" +
            "id=" + getId() +
            ", en='" + getEn() + "'" +
            ", ar='" + getAr() + "'" +
            "}";
    }
}
