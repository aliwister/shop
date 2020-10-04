package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.HashtagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hashtag} and its DTO {@link HashtagDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HashtagMapper extends EntityMapper<HashtagDTO, Hashtag> {



    default Hashtag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Hashtag hashtag = new Hashtag();
        hashtag.setId(id);
        return hashtag;
    }
}
