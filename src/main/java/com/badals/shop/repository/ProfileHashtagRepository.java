package com.badals.shop.repository;

import com.badals.shop.domain.Hashtag;
import com.badals.shop.domain.ProfileHashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Hashtag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileHashtagRepository extends JpaRepository<ProfileHashtag, Long> {

   @Query("from ProfileHashtag p where p.tenant.id = ?1")
   List<ProfileHashtag> findForList(Long tenantId);

}
