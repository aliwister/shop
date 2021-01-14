package com.badals.shop.repository;

import com.badals.shop.domain.Action;
import com.badals.shop.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

   List<Action> findAllByObjectId(String valueOf);
}
