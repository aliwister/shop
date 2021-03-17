package com.badals.shop.repository.search;

import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.service.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.math.BigDecimal;
import java.util.List;

public interface OrderSearchRepository extends ElasticsearchRepository<OrderDTO, Long> {
   //AddProductDTO findBySlug(String slug);

   List<OrderDTO> findAllByCustomerContainsOrderById(String search, Pageable p);

   List<OrderDTO> findAllByOrderStateInAndBalanceNot(List<OrderState> orderStates, BigDecimal balance, Pageable p);
}
