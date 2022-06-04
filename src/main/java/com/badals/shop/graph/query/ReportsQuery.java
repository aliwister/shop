package com.badals.shop.graph.query;

import com.badals.shop.repository.projection.AggOrderEntry;
import com.badals.shop.service.*;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportsQuery extends BaseQuery implements GraphQLQueryResolver {

   private final TenantAdminProductService productService;
   private final TenantAdminOrderService orderService;
   private static final Logger log = LoggerFactory.getLogger(ReportsQuery.class);

   public ReportsQuery(TenantAdminProductService productService, TenantAdminOrderService orderService) {
      this.productService = productService;
      this.orderService = orderService;
   }

   public List<AggOrderEntry> aggOrderReport() {
      List<AggOrderEntry> report =  orderService.aggOrderReport();
      return report;
   }
}

