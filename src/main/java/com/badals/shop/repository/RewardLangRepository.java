package com.badals.shop.repository;

import com.badals.shop.domain.RewardLang;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RewardLang entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RewardLangRepository extends JpaRepository<RewardLang, Long> {

}
