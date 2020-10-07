package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.SpeedDialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpeedDial} and its DTO {@link SpeedDialDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface SpeedDialMapper extends EntityMapper<SpeedDialDTO, SpeedDial> {

/*    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.ref", target = "productRef")*/
    SpeedDialDTO toDto(SpeedDial speedDial);

/*    @Mapping(source = "productId", target = "product")*/
    SpeedDial toEntity(SpeedDialDTO speedDialDTO);

    default SpeedDial fromId(Long id) {
        if (id == null) {
            return null;
        }
        SpeedDial speedDial = new SpeedDial();
        speedDial.setId(id);
        return speedDial;
    }
}
