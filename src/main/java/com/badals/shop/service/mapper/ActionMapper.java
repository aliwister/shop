package com.badals.shop.service.mapper;

import com.badals.shop.domain.Action;
import com.badals.shop.domain.Carrier;
import com.badals.shop.service.dto.ActionDTO;
import com.badals.shop.service.dto.CarrierDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Carrier} and its DTO {@link CarrierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActionMapper extends EntityMapper<ActionDTO, Action> {



    default Action fromId(Long id) {
        if (id == null) {
            return null;
        }
        Action action = new Action();
        action.setId(id);
        return action;
    }
}
