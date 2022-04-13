package com.badals.shop.repository;

import com.badals.shop.domain.tenant.Media;
import com.badals.shop.domain.tenant.S3UploadRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

}
