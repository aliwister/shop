package com.badals.shop.repository;

import com.badals.shop.domain.Merchant;
import com.badals.shop.service.dto.MerchantDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Merchant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

   Optional<Merchant> findMerchantByDomain(String domain);
}
