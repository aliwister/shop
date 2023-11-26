package com.badals.shop.repository;

import com.badals.shop.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByCustomerId(Long customerId);

}
