package com.badals.shop.service.mapper;

import com.badals.shop.domain.Hashtag;
import com.badals.shop.domain.ProfileHashtag;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Hashtag} and its DTO {@link HashtagDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfileHashtagMapper extends EntityMapper<ProfileHashtagDTO, ProfileHashtag> {



    default Hashtag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Hashtag hashtag = new Hashtag();
        hashtag.setId(id);
        return hashtag;
    }
}
