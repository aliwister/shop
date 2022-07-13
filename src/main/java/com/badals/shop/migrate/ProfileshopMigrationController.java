package com.badals.shop.migrate;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantOrderItem;
import com.badals.shop.domain.tenant.TenantPayment;
import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.repository.*;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProfileshopMigrationController {
   private final Logger log = LoggerFactory.getLogger(ProfileshopMigrationController.class);

   private final String ORDER_MIGRATE_URL = "/migrate/orders";
   private final OrderRepository orderRepository;
   private final OrderItemRepository orderItemRepository;
   private final PaymentRepository paymentRepository;
   private final TenantOrderRepository tenantOrderRepository;
   private final TenantOrderItemRepository tenantOrderItemRepository;
   private final TenantPaymentRepository tenantPaymentRepository;
   private final OrderMigrationMapper orderMapper;
   private final OrderItemMigrationMapper orderItemMapper;
   private final PaymentMigrationMapper paymentMapper;

   private final String PRODUCT_MIGRATE_URL = "/migrate/products";
   private final ProductRepository productRepository;
   private final TenantProductRepository tenantProductRepository;

   public ProfileshopMigrationController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, PaymentRepository paymentRepository, TenantOrderRepository tenantOrderRepository, TenantOrderItemRepository tenantOrderItemRepository, TenantPaymentRepository tenantPaymentRepository, OrderMigrationMapper orderMapper, OrderItemMigrationMapper orderItemMapper, PaymentMigrationMapper paymentMapper, ProductRepository productRepository, TenantProductRepository tenantProductRepository) {
      this.orderRepository = orderRepository;
      this.orderItemRepository = orderItemRepository;
      this.paymentRepository = paymentRepository;
      this.tenantOrderRepository = tenantOrderRepository;
      this.tenantOrderItemRepository = tenantOrderItemRepository;
      this.tenantPaymentRepository = tenantPaymentRepository;
      this.orderMapper = orderMapper;
      this.orderItemMapper = orderItemMapper;
      this.paymentMapper = paymentMapper;
      this.productRepository = productRepository;
      this.tenantProductRepository = tenantProductRepository;
   }

   @PersistenceContext
   private EntityManager entityManager;

   @Transactional
   @PostMapping(ORDER_MIGRATE_URL)
   public ResponseEntity<String> migrateOrders(@RequestParam("_from") Long from, @RequestParam("_to") Long to) throws URISyntaxException {
      log.debug("REST request to update Order : {} ", from);
      if (from == null || to == null) {
         throw new BadRequestAlertException("Invalid id", ORDER_MIGRATE_URL, "idnull");
      }
      List<Order> fromList = orderRepository.findByIdForMigration(from, to, OrderState.AWAITING_PAYMENT).stream().collect(Collectors.toList());
      //List<TenantOrder> toList = fromList.stream().map(orderMapper::toDto).collect(Collectors.toList());
      TenantContext.setCurrentProfile("badals");
      Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
      filter.setParameter("tenantId", "badals");
      filter.validate();

      List<TenantOrder> tos = new ArrayList();
      List<TenantOrderItem> orderItems = new ArrayList();
      List<TenantProduct> products = new ArrayList();
      List<TenantPayment> payments = new ArrayList();

      for(Order o: fromList) {
         if( o.getOrderState() == OrderState.CANCELLED) {
            if(o.getPayments() == null || o.getPayments().size() == 0)
               continue;
         }
         TenantOrder _to = orderMapper.toDto(o);
         tos.add(_to );
         List<TenantOrderItem> _items = o.getOrderItems().stream().map(orderItemMapper::toDto).map(x -> x.order(_to)).collect(Collectors.toList());
         orderItems.addAll(_items);
         products.addAll(_items.stream().map(TenantOrderItem::getProduct).collect(Collectors.toList()));
         payments.addAll(o.getPayments().stream().map(paymentMapper::toDto).map(x -> x.order(_to)).collect(Collectors.toList()));

      }

      tenantProductRepository.saveAll(products);
      tenantProductRepository.flush();

      tenantOrderRepository.saveAll(tos);
      tenantOrderRepository.flush();

      tenantOrderItemRepository.saveAll(orderItems);
      tenantOrderItemRepository.flush();

      tenantPaymentRepository.saveAll(payments);
      tenantPaymentRepository.flush();

      return ResponseEntity.ok()
              .headers(HeaderUtil.createEntityUpdateAlert("shop", true, ORDER_MIGRATE_URL, from+"-"+to))
              .body("Right on");
   }

   @PostMapping(PRODUCT_MIGRATE_URL)
   public ResponseEntity<String> reindex(@RequestParam("_from") Long from, @RequestParam("_to") Long to) throws URISyntaxException {
      log.debug("REST request to update Order : {} ", from);
      if (from == null || to == null) {
         throw new BadRequestAlertException("Invalid id", PRODUCT_MIGRATE_URL, "idnull");
      }
      return ResponseEntity.ok()
              .headers(HeaderUtil.createEntityUpdateAlert("shop", true, PRODUCT_MIGRATE_URL, from+"-"+to))
              .body("Fuck yeah");
   }

}
