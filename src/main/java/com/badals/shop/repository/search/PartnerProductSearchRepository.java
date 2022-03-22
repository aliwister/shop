package com.badals.shop.repository.search;

import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.PartnerProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PartnerProductSearchRepository extends ElasticsearchRepository<PartnerProduct, Long> {
   List<PartnerProduct> findByTenantIdEqualsAndUpcEquals(String tenant, String upc);
   List<PartnerProduct> findByTenantIdEquals(String tenant);
   PartnerProduct findBySlug(String slug);
}
