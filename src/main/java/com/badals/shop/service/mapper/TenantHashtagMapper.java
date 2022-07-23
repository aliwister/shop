package com.badals.shop.service.mapper;

import com.badals.shop.domain.tenant.TenantHashtag;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link TenantHashtag} and its DTO {@link HashtagDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TenantHashtagMapper extends EntityMapper<ProfileHashtagDTO, TenantHashtag> {



    default TenantHashtag fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantHashtag hashtag = new TenantHashtag();
        hashtag.setId(id);
        return hashtag;
    }
}
