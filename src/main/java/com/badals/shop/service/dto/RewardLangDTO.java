package com.badals.shop.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.RewardLang} entity.
 */
public class RewardLangDTO implements Serializable {

    private Long id;

    @NotNull
    private String lang;

    private String name;

    private String description;


    private Long rewardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RewardLangDTO rewardLangDTO = (RewardLangDTO) o;
        if (rewardLangDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rewardLangDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RewardLangDTO{" +
            "id=" + getId() +
            ", lang='" + getLang() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", reward=" + getRewardId() +
            "}";
    }
}
