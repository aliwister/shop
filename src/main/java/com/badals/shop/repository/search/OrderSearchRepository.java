package com.badals.shop.repository.search;

import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.service.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.math.BigDecimal;
import java.util.List;

public interface OrderSearchRepository extends ElasticsearchRepository<OrderDTO, Long> {
   //AddProductDTO findBySlug(String slug);
   Page<OrderDTO> findAllByCustomerContainsAndTenantIdOrderById(String search, String tenantId, Pageable p);
   Page<OrderDTO> findAllByOrderStateInAndBalanceGreaterThanEqualAndTenantIdOrderByInvoiceDateAsc(List<OrderState> orderStates, Double balance, String tenantId, Pageable p);
   Page<OrderDTO> findAllByOrderStateInAndBalanceGreaterThanEqualAndTenantIdOrderByInvoiceDateDesc(List<OrderState> orderStates, Double balance, String tenantId, Pageable p);

   @Query("{ \"bool\": {\"must\": [ {\"query_string\": { \"query\": \"?0\" } }],\"filter\": [ {\"match\": { \"tenantId\": \"?3\"} },{\"terms\": { \"orderState.keyword\": ?1 }}, {\"range\": { \"balance\": {\"gt\": ?2 } } }] }}")
   Page<OrderDTO> searchByKeyword(String keyword, String states, Double minBalance, String tenantId, Pageable p);

}
