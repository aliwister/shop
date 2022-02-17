package com.badals.shop.repository;

import com.badals.shop.domain.Action;
import com.badals.shop.domain.tenant.S3UploadRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface S3UploadRequestRepository extends JpaRepository<S3UploadRequest, Long> {

}
