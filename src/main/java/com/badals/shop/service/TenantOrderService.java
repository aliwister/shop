package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.DiscountSource;
import com.badals.shop.domain.pojo.AddressPojo;

import com.badals.shop.domain.pojo.CheckoutOrderAdjustment;
import com.badals.shop.domain.pojo.OrderAdjustment;
import com.badals.shop.domain.tenant.Checkout;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantOrderItem;
import com.badals.shop.domain.tenant.TenantReward;
import com.badals.shop.repository.*;
import com.badals.shop.security.jwt.ProfileUser;
import com.badals.shop.service.mapper.CheckoutAddressMapper;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.OrderItemDTO;
import com.badals.shop.service.mapper.CustomerMapper;
import com.badals.shop.service.mapper.TenantOrderMapper;
import com.badals.shop.service.util.MailService;
import com.badals.shop.web.rest.errors.InvalidPhoneException;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.google.gson.Gson;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.hibernate.envers.AuditReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link TenantOrder}.
 */
@Service
@Transactional
public class TenantOrderService {

    private final Logger log = LoggerFactory.getLogger(TenantOrderService.class);
    private final TenantOrderRepository orderRepository;
    private final TenantOrderMapper orderMapper;
    private UserService userService;
    private final CustomerService customerService;
    private final MessageSource messageSource;
    private final MailService mailService;
    private final AuditReader auditReader;
    private final CheckoutAddressMapper checkoutAddressMapper;
    private final CustomerMapper customerMapper;
    private final AddressRepository addressRepository;
    private final TenantCartService cartService;
    private final CheckoutRepository checkoutRepository;
    private final TenantRewardRepository rewardRepository;
    private final PointRepository pointRepository;
    private final PointUsageHistoryRepository pointUsageHistoryRepository;
    private final PointCustomerRepository pointCustomerRepository;

    public TenantOrderService(TenantOrderRepository orderRepository, TenantOrderMapper orderMapper, UserService userService, CustomerService customerService, MessageSource messageSource, MailService mailService, AuditReader auditReader, CheckoutAddressMapper checkoutAddressMapper, CustomerMapper customerMapper, AddressRepository addressRepository, TenantCartService cartService, CheckoutRepository checkoutRepository, TenantRewardRepository rewardRepository, PointRepository pointRepository, PointUsageHistoryRepository pointUsageHistoryRepository, PointCustomerRepository pointCustomerRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.customerService = customerService;
        this.messageSource = messageSource;
        this.mailService = mailService;
        this.auditReader = auditReader;
        this.checkoutAddressMapper = checkoutAddressMapper;
        this.customerMapper = customerMapper;
        this.addressRepository = addressRepository;
        this.cartService = cartService;
        this.checkoutRepository = checkoutRepository;
        this.rewardRepository = rewardRepository;
        this.pointRepository = pointRepository;
        this.pointUsageHistoryRepository = pointUsageHistoryRepository;
        this.pointCustomerRepository = pointCustomerRepository;
    }
    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        TenantOrder order = orderMapper.toEntity(orderDTO);
        return save(order);
    }
    /**
     * Get all the orders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        log.debug("Request to get all Orders");
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id)
                .map(orderMapper::toDto);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDTO getOrderConfirmation(String reference, String confirmationKey) throws OrderNotFoundException {
        log.info("reference + confirmation key = ", reference, confirmationKey);
        TenantOrder order = orderRepository.findOrderByReferenceAndConfirmationKey(reference, confirmationKey).orElse(null);
        if(order == null) {
            throw new OrderNotFoundException("Order Not Found");
        }
        if(order.getEmailSent()) {
            return orderMapper.toDto(order);
        }
        Customer customer = customerService.findByEmail(order.getEmail());
        if(customer != null)
            order.setCustomer(customer);

//        check rewards for the order, validate points
        Checkout checkout =  checkoutRepository.findBySecureKey(order.getCheckoutSecureKey()).orElse(null);
        AddressPojo addressPojo = order.getDeliveryAddressPojo();

        if (addressPojo != null && addressPojo.getSave() != null && addressPojo.getSave()) {
            Address address = checkoutAddressMapper.addressPojoToAddress(addressPojo);
            address.setCustomer(customer);
            address.setActive(true);
            address.setDeleted(false);
            address.setIdCountry(164L);
            address = addressRepository.save(address);
            order.setDeliveryAddressPojo(null);
            order.setDeliveryAddress(address);

        }

        String secureKey = confirmationKey.split("\\.")[0];
        cartService.closeCart(secureKey);
        order.setEmailSent(true);
        OrderDTO dto = save(order);
        // add to history

        dto.setCustomer(customerMapper.toDto(customer));
        sendConfirmationEmail(dto);
        return dto;
    }
    public OrderResponse getOrders(List<OrderState> orderState, Integer offset, Integer limit, String searchText, Boolean balance, Boolean isAsc, BigDecimal minBal) {
        if(searchText.length() > 2 || balance) {
//            return searchOrders(orderState, offset, limit, searchText, balance, isAsc, minBal);
        }

        Page<TenantOrder> orders = orderRepository.findAllByOrderStateInOrderByCreatedDateDesc(orderState, PageRequest.of((int) offset/limit,limit));
        OrderResponse response = new OrderResponse();
        response.setTotal(orders.getSize());
        response.setItems(orders.stream().map(orderMapper::toDto).collect(Collectors.toList()));
        Integer total = orderRepository.countForState(orderState);
        response.setHasMore((limit+offset) < total);
        return response;

    }

    public OrderResponse getOrders(List<OrderState> orderState, Integer offset, Integer limit) {
        ProfileUser profileUser = (ProfileUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<TenantOrder> orders = orderRepository.findAllByOrderStateInAndEmailIsOrderByCreatedDateDesc(orderState,profileUser.getUsername(), PageRequest.of((int) offset/limit,limit));
        OrderResponse response = new OrderResponse();
        response.setTotal(orders.getSize());
        response.setItems(orders.stream().map(orderMapper::toDto).collect(Collectors.toList()));
        Integer total = orderRepository.countForState(orderState);
        response.setHasMore((limit+offset) < total);
        return response;

    }

//    public OrderResponse searchOrders(List<OrderState> orderState, Integer offset, Integer limit, String searchText, Boolean balance, Boolean isAsc, BigDecimal minBal) {
//        OrderResponse response = new OrderResponse();
//        Double minBalance = minBal.doubleValue();
//        List<OrderDTO> orders = null;
//        Page<OrderDTO> page = null;
//        if(!balance) minBalance = -100000.0;
//        if (searchText != null && searchText.length() > 0) {
//            String states = new Gson().toJson(orderState);
//            page= orderSearchRepository.searchByKeyword(searchText, states, minBalance, TenantContext.getCurrentProfile(), PageRequest.of((int) offset/limit, limit, new Sort(new Sort.Order(Sort.Direction.DESC,"id"))));
//
//            orders = StreamSupport
//                    .stream(page.spliterator(), false).collect(Collectors.toList());
//
//        }
//        else if(balance) {
//            if(isAsc) {
//                page = orderSearchRepository.findAllByOrderStateInAndBalanceGreaterThanEqualAndTenantIdOrderByInvoiceDateAsc(orderState, minBalance, TenantContext.getCurrentProfile(), PageRequest.of((int) offset / limit, limit, new Sort(new Sort.Order(Sort.Direction.ASC, "id"))));
//                orders = StreamSupport
//                        .stream(page.spliterator(), false).collect(Collectors.toList());
//            }
//            else {
//                page = orderSearchRepository.findAllByOrderStateInAndBalanceGreaterThanEqualAndTenantIdOrderByInvoiceDateDesc(orderState, minBalance, TenantContext.getCurrentProfile(), PageRequest.of((int) offset / limit, limit, new Sort(new Sort.Order(Sort.Direction.DESC, "id"))));
//                orders = StreamSupport
//                        .stream(page.spliterator(), false).collect(Collectors.toList());
//            }
//        }
//        else
//            orders = StreamSupport
//                    .stream(orderSearchRepository.search(queryStringQuery(searchText), PageRequest.of((int) offset/limit, limit, new Sort(new Sort.Order(Sort.Direction.DESC,"id")))).spliterator(), false).collect(Collectors.toList());
//
//        response.setItems(orders);
//        response.setTotal(orders.size());
//        response.setHasMore((limit+offset) < page.getTotalElements());
//        return response;
//    }

    public Optional<OrderDTO> getOrderWithOrderItems(Long id) {
        return orderRepository.findForOrderDetails(id, String.valueOf(id)).map(orderMapper::toDto);
    }
    public OrderDTO setOrderState(Long orderId, OrderState os) {
        TenantOrder order = orderRepository.getOne(orderId);
        if(os.equals(OrderState.PAYMENT_ACCEPTED) && !order.getOrderState().equals(OrderState.PAYMENT_ACCEPTED))
            order.setInvoiceDate(LocalDate.now());
        order.setOrderState(os);
        if(!os.equals(OrderState.CANCELLED)) {
            order.setSubtotal(calculateSubtotal(order));
            order.setTotal(calculateTotal(order));
        }
        return save(order);
    }
    public void sendConfirmationEmail(OrderDTO dto) {
        CustomerDTO customer = dto.getCustomer();
        UserBase userBase = null;
        if (customer == null) {
            userBase = new UserBase(dto.getEmail(), "", dto.getEmail());
        }
        else
            userBase = customerMapper.toUserBase(customer);
        mailService.sendOrderCreationMail(userBase, dto);
    }
    public BigDecimal calculateSubtotal(TenantOrder order) {
        BigDecimal sum = BigDecimal.valueOf(order.getOrderItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
        return sum;
    }

    public BigDecimal calculateTotal(TenantOrder order) {
        BigDecimal sum = BigDecimal.valueOf(order.getOrderItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
        if(order.getDeliveryTotal() != null)
            sum = sum.add(order.getDeliveryTotal());
        if(order.getDiscountsTotal() != null)
            sum = sum.subtract(order.getDiscountsTotal());
        return sum;
    }
    private OrderDTO save(TenantOrder order) {
        order = orderRepository.save(order);
        OrderDTO dto = orderMapper.toDto(order);
//        orderSearchRepository.save(dto);
        return dto;
    }

    public void sendConfirmationEmail(Long id) {
        TenantOrder order = orderRepository.getOne(id);
        OrderDTO dto = orderMapper.toDto(order);
        sendConfirmationEmail(dto);
    }

    public OrderDTO setStatus(String id, OrderState state) {
        TenantOrder order = orderRepository.findByReference(id).get();
        order.setOrderState(state);

        return save(order);
    }

    public void sendVoltageEmail(Long orderId, List<Long> orderItems) {
        TenantOrder order = orderRepository.getOrderWithSomeOrderItems(orderId, orderItems).orElse(null);
        OrderDTO dto = orderMapper.toDto(order);
        CustomerDTO customer = dto.getCustomer();
        UserBase userBase = null;
        if (customer == null) {
            userBase = new UserBase(order.getEmail(), "", order.getEmail());
        }
        else
            userBase = customerMapper.toUserBase(customer);
        mailService.sendVoltageMail(userBase, dto);
    }
    @Transactional
    public OrderDTO updateCarrier(Long id, String carrier, BigDecimal value) {
        TenantOrder order = orderRepository.getOne(id);
        order.setCarrier(carrier);
        order.setDeliveryTotal(value);
        if (order.getOrderAdjustments() == null)
            order.setOrderAdjustments(new ArrayList<>());

        OrderAdjustment adjustment = order.getOrderAdjustments().stream().filter(adj -> adj.shippingCheck()).findFirst().orElse(null);
        if (adjustment == null) {
            adjustment = new OrderAdjustment();
            order.getOrderAdjustments().add(adjustment);
        }
        adjustment.setShippingValueAndType(value, carrier);
        OrderDTO dto = save(order);
        return dto;
    }
    public OrderDTO cancel(Long id, String reason) {
        TenantOrder order = orderRepository.getOne(id);

        order.setOrderState(OrderState.CANCELLED);
        order.setTotal(BigDecimal.ZERO);

        OrderDTO dto = save(order);
        CustomerDTO customer = dto.getCustomer();
        UserBase userBase = null;
        if (customer == null) {
            userBase = new UserBase(order.getEmail(), "", order.getEmail());
        }
        else
            userBase = customerMapper.toUserBase(customer);

        mailService.sendCancelMail(userBase, dto, reason);
        return dto;
    }
    public OrderDTO editOrderItems(Long id, List<OrderItemDTO> orderItems, String reason) {
        TenantOrder order = orderRepository.findForOrderDetails(id, String.valueOf(id)).get();
        boolean isEditCancel = false;
        for(OrderItemDTO item : orderItems) {
            TenantOrderItem before = order.getOrderItems().stream().filter(x -> x.getSequence() == item.getSequence())
                    .findFirst().get();

            if(item.getQuantity().compareTo(before.getQuantity()) < 0)
                isEditCancel = true;

            before.quantity(item.getQuantity())
                    .price(item.getPrice())
                    .lineTotal(item.getPrice().multiply(item.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        order.setSubtotal(calculateSubtotal(order));
        order.setTotal(calculateTotal(order));
        OrderDTO dto = save(order);
        CustomerDTO customer = dto.getCustomer();
        UserBase userBase = null;
        if (customer == null) {
            userBase = new UserBase(order.getEmail(), "", order.getEmail());
        }
        else
            userBase = customerMapper.toUserBase(customer);
        if(isEditCancel)
            mailService.sendEditCancelMail(userBase, dto, reason);
        else
            mailService.sendEditMail(userBase, dto, reason);
        return dto;
    }

    public OrderDTO addDiscount(Long id, BigDecimal amount, String couponName) {
        TenantOrder order = orderRepository.getOne(id);
        order.setDiscountsTotal(amount);
        //order.setCouponName(couponName);
        order.setSubtotal(calculateSubtotal(order));
        order.setTotal(calculateTotal(order));
        return save(order);
    }

    public OrderDTO close(Long id, String reason) {
        TenantOrder order = orderRepository.getOne(id);
        order.setOrderState(OrderState.CLOSED);
        OrderDTO dto = save(order);
        return dto;
    }
    public void sendRequestPaymentSms(Long id, String mobile) throws Exception {
        TenantOrder order = orderRepository.getOne(id);
        mobile = order.getDeliveryAddress().getMobile();
        if(mobile == null)
            throw new InvalidPhoneException("Mobile not provided");
        mobile = mobile.trim();
        mobile = mobile.substring(mobile.length()-8, mobile.length());
        final String[] params = new String[]{order.getReference(), String.valueOf(order.getTotal())};
        Locale locale = Locale.forLanguageTag("en");//user.getLangKey());
        String message = messageSource.getMessage("sms.payment.request", params, locale);
        sendSms(message, "968"+mobile, false);
    }

    private static final String USERNAME = "badals";
    private static final String PASSWORD = "***REMOVED***";
    public void sendSms(String message, String phone, boolean isUnicode) throws Exception {
        String data = "";
        data += "User=" + URLEncoder.encode(USERNAME, "ISO-8859-1");
        data += "&passwd=" + URLEncoder.encode(PASSWORD, "ISO-8859-1");
        if (isUnicode)
            data += "&message=" + hexify(message)+"&mtype=OL";
        else
            data += "&message=" + URLEncoder.encode(message, "ISO-8859-1");
        data += "&DR=Y";
        data += "&mobilenumber="+formatPhoneNumber(phone);

        // Send data
        URL url = new URL("http://api.smscountry.com/SMSCwebservice_bulk.aspx");

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            // Print the response output...
            System.out.println(line);
        }
        wr.close();
        rd.close();

    }
    static String hexify(String str) {
        String ret="";
        for (int i = 0; i < str.length(); ++i) {
            String hex = Integer.toHexString(str.charAt(i)).toUpperCase();
            ret += ("0000"+hex).substring(hex.length());
        }
        return ret;
    }
    public static String formatPhoneNumber(String phone) throws Exception {
        String ret = phone.trim().replaceFirst("^0+(?!$)", "");
        if (ret.length() < 8) {
            throw new Exception ("SMS Fail. Invalid Phone #");
        }
        if (phone.length() < 11) {
            ret = "968"+ret;
        }
        return ret;
    }

    private Integer calculateRewardPoints(Checkout checkout){
        List<CheckoutOrderAdjustment> checkoutOrderAdjustments = checkout.getCheckoutAdjustments();
        return checkoutOrderAdjustments.stream().mapToInt((x)  -> x.getDiscountSource() == DiscountSource.REWARD ? (rewardRepository.findByRewardType(x.getSourceRef()).getPoints()) : 0).sum();
    }

    private Integer getPointsForCustomer(Long customerId){
        List<Point> earnedPoints = pointRepository.findAllByCustomerId(customerId);
        List<PointUsageHistory> usedPoints = pointUsageHistoryRepository.findAllByCustomerId(customerId);
        Integer earned = earnedPoints.stream().mapToInt(Point::getAmount).sum();
        Integer used = usedPoints.stream().mapToInt(PointUsageHistory::getPoints).sum();
        return earned - used;
    }
}
