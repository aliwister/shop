package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.RewardDTO;

import org.mapstruct.*;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Mapper for the entity {@link Reward} and its DTO {@link RewardDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface RewardMapper extends EntityMapper<RewardDTO, Reward> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.ref", target = "productRef")
    RewardDTO toDto(Reward reward);

    @Mapping(source = "productId", target = "product")
    @Mapping(target = "rewardLangs", ignore = true)
    @Mapping(target = "removeRewardLang", ignore = true)
    Reward toEntity(RewardDTO rewardDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget RewardDTO target, Reward source) {
        RewardLang lang = source.getRewardLangs().stream().filter(e -> e.getLang().equals(LocaleContextHolder.getLocale().toString())).findFirst().orElse(source.getRewardLangs().stream().findFirst().orElse(null));

        if(lang != null) {
            target.setName(lang.getName());
            target.setDescription(lang.getDescription());
        }
    }

    default Reward fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reward reward = new Reward();
        reward.setId(id);
        return reward;
    }
}
