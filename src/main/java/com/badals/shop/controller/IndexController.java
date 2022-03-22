package com.badals.shop.controller;

import com.badals.shop.service.TenantAdminOrderService;
import com.badals.shop.service.TenantAdminProductService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Controller
@RequestMapping("/api")
public class IndexController {

   private final Logger log = LoggerFactory.getLogger(IndexController.class);
   private final TenantAdminProductService productService;
   private final TenantAdminOrderService orderService;
   private static final String ORDER_ENTITY_NAME = "jhi_order";


   public IndexController(TenantAdminProductService productService, TenantAdminOrderService orderService) {
      this.productService = productService;
      this.orderService = orderService;
   }


   @PostMapping("/orders/reindex")
   public ResponseEntity<String> reindex(@RequestParam("_from") Long from, @RequestParam("_to") Long to) throws URISyntaxException {
      log.debug("REST request to update Order : {} ", from);
      if (from == null || to == null) {
         throw new BadRequestAlertException("Invalid id", ORDER_ENTITY_NAME, "idnull");
      }
      orderService.reIndex(from, to);
      return ResponseEntity.ok()
              .headers(HeaderUtil.createEntityUpdateAlert("shop", true, ORDER_ENTITY_NAME, from+"-"+to))
              .body("Fuck yeah");
   }
}
