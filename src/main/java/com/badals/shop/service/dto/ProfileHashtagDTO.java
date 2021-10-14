package com.badals.shop.service.dto;
import com.badals.shop.domain.pojo.I18String;
import com.badals.shop.graph.ProductResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Hashtag} entity.
 */
@Data
public class ProfileHashtagDTO implements Serializable {

    private Long id;

    private List<I18String> langs;

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

        ProfileHashtagDTO hashtagDTO = (ProfileHashtagDTO) o;
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

            "}";
    }
}
