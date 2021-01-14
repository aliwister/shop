package com.badals.shop.service.dto;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Carrier} entity.
 */
@Data
public class ActionDTO implements Serializable {

    private Long id;

    private String action;
    private String object;
    private String objectId;
    private String state;
    private String comment;

    private Date createdDate;
    private String createdBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActionDTO actionDTO = (ActionDTO) o;
        if (actionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
