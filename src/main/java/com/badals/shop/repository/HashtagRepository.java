package com.badals.shop.repository;

import com.badals.shop.domain.Hashtag;
import com.badals.shop.domain.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hashtag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

   @Query("select p from Hashtag p order by p.position ASC")
   Page<Hashtag> findForList(Pageable page);
}
