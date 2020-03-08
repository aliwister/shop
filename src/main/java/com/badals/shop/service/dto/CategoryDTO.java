package com.badals.shop.service.dto;
import java.io.Serializable;
import java.util.*;

import com.badals.shop.domain.enumeration.ProductGroup;
import lombok.Data;

/**
 * A DTO for the {@link com.badals.shop.domain.Category} entity.
 */
@Data
public class CategoryDTO implements Serializable {

    private Long id;

    private String title;

    private String icon;

    private String slug;

    private ProductGroup group;

    private Long parentId;

    private List<CategoryDTO> children = new ArrayList<>();

    //private Set<ProductDTO> products = new HashSet<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (categoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), categoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", icon='" + getIcon() + "'" +
            ", slug='" + getSlug() + "'" +
            ", group='" + getGroup() + "'" +
            ", parent=" + getParentId() +
            "}";
    }
}
