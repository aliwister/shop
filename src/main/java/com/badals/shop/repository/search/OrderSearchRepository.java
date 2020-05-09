package com.badals.shop.repository.search;

import com.badals.shop.service.dto.OrderDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderSearchRepository extends ElasticsearchRepository<OrderDTO, Long> {
   //AddProductDTO findBySlug(String slug);
}
