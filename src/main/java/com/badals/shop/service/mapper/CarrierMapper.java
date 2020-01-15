package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.CarrierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Carrier} and its DTO {@link CarrierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CarrierMapper extends EntityMapper<CarrierDTO, Carrier> {



    default Carrier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Carrier carrier = new Carrier();
        carrier.setId(id);
        return carrier;
    }
}
