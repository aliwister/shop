package com.badals.shop.repository;

import com.badals.shop.domain.CartRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CartRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRuleRepository extends JpaRepository<CartRule, Long> {

}
