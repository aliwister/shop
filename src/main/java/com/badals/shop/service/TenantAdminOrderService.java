package com.badals.shop.service;

import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.OrderChannel;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.AddressPojo;
import com.badals.shop.repository.projection.AggOrderEntry;
import com.badals.shop.domain.pojo.LineItem;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantOrderItem;
import com.badals.shop.domain.tenant.TenantPayment;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.repository.AddressRepository;
import com.badals.shop.repository.TenantOrderRepository;
import com.badals.shop.repository.TenantPaymentRepository;
import com.badals.shop.repository.search.OrderSearchRepository;

import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.OrderItemDTO;
import com.badals.shop.service.mapper.CheckoutAddressMapper;
import com.badals.shop.service.mapper.TenantOrderMapper;
import com.badals.shop.service.pojo.Message;
import com.badals.shop.service.util.MailService;
import com.badals.shop.web.rest.errors.InvalidPhoneException;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import org.hibernate.envers.AuditReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.badals.shop.service.CartService.createUIUD;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class TenantAdminOrderService {

    private static double ORDER_REF_SIZE = 7;
    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final TenantOrderRepository orderRepository;
    private final TenantPaymentRepository paymentRepository;
    private final OrderSearchRepository orderSearchRepository;

    private final TenantOrderMapper orderMapper;
    private UserService userService;

    private final CustomerService customerService;
    private final MessageSource messageSource;
    private final MailService mailService;
    private final AuditReader auditReader;
    private final CheckoutAddressMapper checkoutAddressMapper;
    private final AddressRepository addressRepository;
    private final TenantCartService cartService;

    public TenantAdminOrderService(TenantOrderRepository orderRepository, TenantPaymentRepository paymentRepository, OrderSearchRepository orderSearchRepository, TenantOrderMapper orderMapper, UserService userService, CustomerService customerService, MessageSource messageSource, MailService mailService, AuditReader auditReader, CheckoutAddressMapper checkoutAddressMapper, AddressRepository addressRepository, TenantCartService cartService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.orderSearchRepository = orderSearchRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.customerService = customerService;
        this.messageSource = messageSource;
        this.mailService = mailService;
        this.auditReader = auditReader;
        this.checkoutAddressMapper = checkoutAddressMapper;
        this.addressRepository = addressRepository;
        this.cartService = cartService;
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

    private String generateRandomNumber() {
        //Random generator = new Random();
        //int num = generator. nextInt(8999999) + 1000000;
        long min = (long) Math.pow(10, ORDER_REF_SIZE - 1);
        Long rand = ThreadLocalRandom.current().nextLong(min, min * 10);
        return String.valueOf(rand);
    }

    public String generateOrderId(int attempt) {
        String ret =  orderRepository.getFirstUnused(generateRandomNumber(),generateRandomNumber(),generateRandomNumber(),generateRandomNumber(),generateRandomNumber(),generateRandomNumber(),generateRandomNumber(),generateRandomNumber(),generateRandomNumber(),generateRandomNumber());
        if (ret == null) {
            log.error("ORDER ID is null");
            if(attempt > 3) {
                ORDER_REF_SIZE++;
            }
            return generateOrderId(++attempt);
        }
        return ret;
    }

    public BigDecimal calculateSubtotal(CheckoutCart checkout) {
        BigDecimal sum = BigDecimal.valueOf(checkout.getItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum());
        return sum;
    }

    public BigDecimal calculateTotal(CheckoutCart checkout) {
        BigDecimal sum = BigDecimal.valueOf(checkout.getItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum());
        //sum = sum.add(checkout.getCarrierRate());
        return sum;
    }

    @Transactional
    public TenantOrder createPosOrder(CheckoutCart cart, String paymentMethod) {
        TenantOrder order = new TenantOrder();
        order.setChannel(OrderChannel.POS);
        order.setCurrency(cart.getCurrency());
        order.setCreatedDate(new Date());
        order.setInvoiceDate(LocalDate.now());
        //order.setCustomerId(cart.get);
        //order.setInvoiceAddress(cart.getInvoiceAddress());
        //order.setDeliveryAddressId(cart.getDeliveryAddressId());
        //order.setDeliveryAddress(cart.getDeliveryAddress());
        order.setEmail(cart.getEmail());
        order.setOrderState(OrderState.DELIVERED);
        
        String orderRef = generateOrderId(1);
        String uiud = createUIUD();

        order.setReference(orderRef);
        //order.setConfirmationKey(cart.getSecureKey()+"."+uiud);
        order.setSubtotal(calculateSubtotal(cart));
        order.setTotal(calculateTotal(cart));
        order.setOrderAdjustments(cart.getOrderAdjustments());
        //order.setCart(cart);
        //order.setCarrier(cart.getCarrier());
        order.setDeliveryTotal(BigDecimal.ZERO);
        order.setPaymentMethod(paymentMethod);

        //order.s

        int i = 1;
        for(LineItem item : cart.getItems()) {
            TenantOrderItem orderItem = new TenantOrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProductName(item.getName());
            orderItem.setQuantity(BigDecimal.valueOf(item.getQuantity()));
            orderItem.sequence(i++);
            orderItem.setImage(item.getImage());
            orderItem.setWeight(item.getWeight());
            orderItem.setUnit(item.getUnit());
            orderItem.setLineTotal(item.getPrice().multiply(orderItem.getQuantity()));
            orderItem.setSku(item.getSku());
            //if(item.getProductId() != null)
            orderItem.setRef(item.getProductId().toString());
            if(item.getCost() != null)
                orderItem.setCost(item.getCost());
            order.addOrderItem(orderItem);

        }


        return order;
    }
    
    
    @Transactional
    public OrderDTO createPosOrder(CheckoutCart cart, String paymentMethod, String paymentAmount, String authCode) {
        TenantOrder order = createPosOrder(cart, paymentMethod);
        saveOrder(order);
        //if(p.prePay) {
            TenantPayment payment = new TenantPayment();
            payment.setAmount(new BigDecimal(paymentAmount));
            payment.setOrder(order);
            //payment.setCreated_date(Instant.now());
            payment.setPaymentMethod(paymentMethod);
            payment.setTransactionId(authCode);
            //payment.setCreated_date(Instant.now());
            payment.setTrackId(cart.getId());
            payment.setCurrency(cart.getCurrency());
            paymentRepository.save(payment);
        //}
        orderRepository.refresh(order);
        return orderMapper.toDto(order);
    }

    private void saveOrder(TenantOrder order) {
        orderRepository.save(order);
        orderRepository.refresh(order);
        OrderDTO orderDTO = orderMapper.toDto(order);
        orderSearchRepository.save(orderDTO);
        //orderDTO.getOrderItems().stream().forEach(x -> orderItemSearchRepository.save(x));
    }

    @Transactional
    public OrderDTO getOrderConfirmation(String reference, String confirmationKey) throws OrderNotFoundException {
        log.info("reference + confirmation key = ", reference, confirmationKey);
        TenantOrder order = orderRepository.findOrderByReferenceAndConfirmationKey(reference, confirmationKey).orElse(null);
        if(order == null) {
            throw new OrderNotFoundException("Order Not Found");
        }
        Customer customer = customerService.findByEmail(order.getEmail());
        //order.setCustomer(customer);


        AddressPojo addressPojo = order.getDeliveryAddressPojo();

        if (addressPojo != null && addressPojo.getSave()) {
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

        order.setConfirmationKey(order.getConfirmationKey()+order.getId());
        OrderDTO dto = save(order);
        sendConfirmationEmail(dto);
        return dto;
    }


    @Transactional
    public OrderResponse getOrders(List<OrderState> orderState, Integer offset, Integer limit, String searchText, Boolean balance) {
/*
        if(searchText != null && searchText.trim().length() > 1 || balance)
            return searchOrders(orderState, offset, limit, searchText, balance);
*/

        Page<TenantOrder> orders = orderRepository.findAllByOrderStateInOrderByCreatedDateDesc(orderState, PageRequest.of((int) offset/limit,limit));
        OrderResponse response = new OrderResponse();
        response.setTotal(orders.getSize());
        response.setItems(orders.stream().map(orderMapper::toDto).collect(Collectors.toList()));
        Integer total = orderRepository.countForState(orderState);
        response.setHasMore((limit+offset) < total);
        return response;

    }

/*    public OrderResponse searchOrders(List<OrderState> orderState, Integer offset, Integer limit, String searchText, Boolean balance) {

        OrderResponse response = new OrderResponse();
        List<OrderDTO> orders = null;
        if(balance) {
            orders = StreamSupport
                    .stream(orderSearchRepository.findAllByOrderStateInAndBalanceNot(orderState, BigDecimal.ZERO, PageRequest.of((int) offset/limit, limit, new Sort(new Sort.Order(Sort.Direction.DESC,"id")))).spliterator(), false).collect(Collectors.toList());

        }
        else
            orders = StreamSupport
                    .stream(orderSearchRepository.search(queryStringQuery(searchText), PageRequest.of((int) offset/limit, limit, new Sort(new Sort.Order(Sort.Direction.DESC,"id")))).spliterator(), false).collect(Collectors.toList());
        response.setItems(orders);
        response.setTotal(orders.size());
        Integer total = orderRepository.countForState(orderState);
        response.setHasMore((limit+offset) < total);
        return response;
    }*/

/*    public void sendPaymentMessage(Long id) {
        Optional<TenantOrder> o = orderRepository.findJoinCustomerJoinAddress(id);
        OrderDTO order = o.map(orderMapper::toDto).orElse(null);
    }*/

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
    private static final String PASSWORD = "Qaswedfr1";
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

    public OrderDTO setStatus(String id, OrderState state) {
        TenantOrder order = orderRepository.findByReference(id).get();
        order.setOrderState(state);

        return save(order);
    }

    public OrderDTO setStatus(Long id, OrderState state) {
        TenantOrder order = orderRepository.getOne(id);
        List<Order> versions = new ArrayList<>();

        order.setOrderState(state);
        List<Number> revisions = auditReader.getRevisions(Order.class, id);
        for (Number rev : revisions) {
            Order v = auditReader.find(Order.class, order, rev);
            versions.add(v);
        }

        return save(order);
    }

    public void sendConfirmationEmail(OrderDTO dto) {
        //OrderDTO order = getOrderWithOrderItems(id).orElse(null);
        //CustomerDTO customer = order.getCustomer();
        mailService.sendOrderCreationMail(dto.getCustomer(), dto);
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
        if(isEditCancel)
            mailService.sendEditCancelMail(dto.getCustomer(), dto, reason);
        else
            mailService.sendEditMail(dto.getCustomer(), dto, reason);
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

    public void sendVoltageEmail(Long orderId, ArrayList<Long> orderItems) {
        TenantOrder order = orderRepository.getOrderWithSomeOrderItems(orderId, orderItems).orElse(null);
        OrderDTO dto = orderMapper.toDto(order);
        CustomerDTO customer = dto.getCustomer();
        mailService.sendVoltageMail(customer, dto);
    }

    public OrderDTO cancel(Long id, String reason) {
        TenantOrder order = orderRepository.getOne(id);

        order.setOrderState(OrderState.CANCELLED);
        order.setTotal(BigDecimal.ZERO);
        OrderDTO dto = save(order);
        mailService.sendCancelMail(dto.getCustomer(), dto, reason);
        return dto;
    }

    private OrderDTO save(TenantOrder order) {
        order = orderRepository.save(order);
        OrderDTO dto = orderMapper.toDto(order);
        //orderSearchRepository.save(dto);
        return dto;
    }

    public void sendConfirmationEmail(Long id) {
        TenantOrder order = orderRepository.getOne(id);
        OrderDTO dto = orderMapper.toDto(order);
        sendConfirmationEmail(dto);
    }

    public OrderDTO close(Long id, String reason) {
        TenantOrder order = orderRepository.getOne(id);
        order.setOrderState(OrderState.CLOSED);
        OrderDTO dto = save(order);
        return dto;
    }

    public List<AggOrderEntry> aggOrderReport() {
        String hour = "%Y-%m-%d %H:00:00";
        String day = "%Y-%m-%d";
        List<AggOrderEntry> report =  orderRepository.aggOrderReport(day,"+4:00");
        return report;
    }
/*    public List<AggregateOrderReport> aggOrderReportNative() {
        List<AggregateOrderReport> report =  orderRepository.aggOrderNativeReport();
        return report;
    }*/

    @Transactional
    public Message voidOrder(Long id) {
        //this.cancel(id, "");
        TenantOrder order = orderRepository.getOne(id);

        order.setOrderState(OrderState.CANCELLED);
        order.setTotal(BigDecimal.ZERO);
        OrderDTO dto = save(order);
        paymentRepository.voidOrderPayments(id);

        return new Message("success");
    }

    public void reIndex(Long from, Long to) {
        List<OrderDTO> dtos = orderRepository.findByIdBetween(from, to).stream().map(orderMapper::toDto).collect(Collectors.toList());
        if(dtos.isEmpty())
            return;
        //dtos.stream().forEach(x -> orderSearchRepository.index(x));
        orderSearchRepository.saveAll(dtos);
        //dtos.forEach(dto -> orderItemSearchRepository.saveAll(dto.getOrderItems()));
    }
}