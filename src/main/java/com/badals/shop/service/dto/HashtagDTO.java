package com.badals.shop.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Hashtag} entity.
 */
public class HashtagDTO implements Serializable {

    private Long id;

    private String en;

    private String ar;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

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
