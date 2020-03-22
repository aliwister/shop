package com.badals.shop.repository;

import com.badals.shop.domain.PurchaseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the PurchaseItem entity.
 */
@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    @Query(value = "select distinct purchaseItem from PurchaseItem purchaseItem left join fetch purchaseItem.orderItems",
        countQuery = "select count(distinct purchaseItem) from PurchaseItem purchaseItem")
    Page<PurchaseItem> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct purchaseItem from PurchaseItem purchaseItem left join fetch purchaseItem.orderItems")
    List<PurchaseItem> findAllWithEagerRelationships();

    @Query("select purchaseItem from PurchaseItem purchaseItem left join fetch purchaseItem.orderItems where purchaseItem.id =:id")
    Optional<PurchaseItem> findOneWithEagerRelationships(@Param("id") Long id);

}
