package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.RewardLangDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RewardLang} and its DTO {@link RewardLangDTO}.
 */
@Mapper(componentModel = "spring", uses = {RewardMapper.class})
public interface RewardLangMapper extends EntityMapper<RewardLangDTO, RewardLang> {

    @Mapping(source = "reward.id", target = "rewardId")
    RewardLangDTO toDto(RewardLang rewardLang);

    @Mapping(source = "rewardId", target = "reward")
    RewardLang toEntity(RewardLangDTO rewardLangDTO);

    default RewardLang fromId(Long id) {
        if (id == null) {
            return null;
        }
        RewardLang rewardLang = new RewardLang();
        rewardLang.setId(id);
        return rewardLang;
    }
}
