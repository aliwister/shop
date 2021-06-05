package com.badals.shop.repository;

import com.badals.shop.domain.Recycle;
import com.badals.shop.domain.Slug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SlugRepository extends JpaRepository<Slug, Long> {
   @Query(value="select t.id " +
           "from  (select :s0 as id " +
           "union select :s1 " +
           ") as t  " +
           "where not exists ( " +
           "select 1 from  " +
           "slug s " +
           "where s.id = t.id " +
           ")  " +
           "limit 1 ", nativeQuery = true)
   Long getFirstUnused(
           @Param("s0") Long option0,
           @Param("s1") Long option1);
}
