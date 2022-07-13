package com.badals.shop.migrate;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.Payment;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantOrderItem;
import com.badals.shop.domain.tenant.TenantPayment;
import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.repository.*;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
      List<Long> productIds = new ArrayList();
      ScrollableResults scrollableResults = null;
      EntityTransaction txn = null;
      EntityManager em = null;
      try {
         em = entityManager.getEntityManagerFactory().createEntityManager();
         txn  = em.getTransaction();
         txn.begin();

         Session session = em.unwrap( Session.class );

         scrollableResults = session
                 .createQuery( "from Order o join fetch o.payments where o.orderState <> ?3 and o.id between ?1 and ?2" )
                 .setParameter(1, from).setParameter(2, to).setParameter(3, OrderState.AWAITING_PAYMENT)
                 .setCacheMode( CacheMode.IGNORE )
                 .scroll( ScrollMode.FORWARD_ONLY );

         int batchSize = 10;
         int i = 0;
         while(scrollableResults.next()) {
            Order o = (Order) scrollableResults.get( 0 );
            if( o.getOrderState() == OrderState.CANCELLED) {
               if(o.getPayments() == null || o.getPayments().size() == 0)
                  continue;
            }
            if ( i++ > 0 && i % batchSize == 0 ) {
               //flush a batch of inserts and release memory
               em.flush();
               em.clear();
            }
            TenantOrder _to = orderMapper.toDto(o);
            _to.setTenantId("badals");
            List<TenantOrderItem> _items = o.getOrderItems().stream().map(orderItemMapper::toDto).map(x -> x.order(_to)).collect(Collectors.toList());
            em.persist( _to );
            for(TenantOrderItem item : _items) {
               TenantProduct p = item.getProduct();

               if(p != null && !productIds.contains(p.getId())) {
                  p.setTenantId("badals");
                  p.setParentId(null);
                  productIds.add(p.getId());
                  em.merge(p);
                  item.setRef(p.getRef());
               }
               item.setTenantId("badals");
               item.setProduct(null);
               em.persist(item);
            }
            for(Payment p: o.getPayments()) {
               TenantPayment tp = paymentMapper.toDto(p).order(_to);
               tp.setTenantId("badals");
               em.persist(tp);
            }
         }
         txn.commit();
      } catch (RuntimeException e) {
         if ( txn != null && txn.isActive()) txn.rollback();
         throw e;
      } finally {
         if (scrollableResults != null) {
            scrollableResults.close();
         }
         if (em != null) {
            em.close();
         }
      }
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
