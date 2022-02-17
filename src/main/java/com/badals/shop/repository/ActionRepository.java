package com.badals.shop.repository;

import com.badals.shop.domain.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

   List<Action> findAllByObjectIdAndObject(String objectId, String object);
}
