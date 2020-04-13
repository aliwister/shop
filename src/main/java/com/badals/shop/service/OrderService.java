package com.badals.shop.service;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.OrderItem;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.OrderRepository;
import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.OrderItemDTO;
import com.badals.shop.service.mapper.OrderMapper;
import com.badals.shop.web.rest.errors.InvalidPhoneException;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import org.hibernate.envers.AuditReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;
    private UserService userService;

    private final CustomerService customerService;
    private final MessageSource messageSource;
    private final MailService mailService;
    private final AuditReader auditReader;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, UserService userService, CustomerService customerService, MessageSource messageSource, MailService mailService, AuditReader auditReader) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.customerService = customerService;
        this.messageSource = messageSource;
        this.mailService = mailService;
        this.auditReader = auditReader;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
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
        Order order = orderRepository.findOrderByReferenceAndConfirmationKey(reference, confirmationKey).orElse(null);
        if(order == null) {
            throw new OrderNotFoundException("Order Not Found");
        }
        Customer customer = customerService.findByEmail(order.getEmail());
        order.setCustomer(customer);
        order.setConfirmationKey(order.getConfirmationKey()+order.getId());
        order = orderRepository.save(order);
        sendConfirmationEmail(order.getId());
        return orderMapper.toDto(order);
    }

    public List<OrderDTO> getCustomerOrders() {
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        return orderRepository.findOrdersByCustomerOrderByCreatedDateDesc(loginUser).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<OrderDTO> getOrders(List<OrderState> orderState, Integer limit, String searchText) {
        return orderRepository.findAllByOrderStateInOrderByCreatedDateDesc(orderState, PageRequest.of(0,limit)).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void sendPaymnetMessage(Long id) {
        Optional<Order> o = orderRepository.findJoinCustomerJoinAddress(id);
        OrderDTO order = o.map(orderMapper::toDto).orElse(null);
    }

    public void sendRequestPaymentSms(Long id, String mobile) throws Exception {
        Order order = orderRepository.getOne(id);
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

   public Optional<OrderDTO> getOrderWithOrderItems(Long id) {
        return orderRepository.findForOrderDetails(id, String.valueOf(id)).map(orderMapper::toDto);
   }

    public OrderDTO setOrderState(Long orderId, OrderState os) {
        Order order = orderRepository.save(orderRepository.getOne(orderId).orderState(os));
        return orderMapper.toDto(order);
    }

    public OrderDTO setStatus(String id, OrderState state) {
        Order order = orderRepository.findByReference(id).get();
        order.setOrderState(state);
        return orderMapper.toDto(orderRepository.save(order));
    }

    public OrderDTO setStatus(Long id, OrderState state) {
        Order order = orderRepository.getOne(id);
        List<Order> versions = new ArrayList<>();

        order.setOrderState(state);
        List<Number> revisions = auditReader.getRevisions(Order.class, id);
        for (Number rev : revisions) {
            Order v = auditReader.find(Order.class, order, rev);
            versions.add(v);
        }

        return orderMapper.toDto(orderRepository.save(order));
    }

    public void sendConfirmationEmail(Long id) {
        OrderDTO order = getOrderWithOrderItems(id).orElse(null);
        CustomerDTO customer = order.getCustomer();
        mailService.sendOrderCreationMail(customer, order);
    }

   public OrderDTO editOrderItems(Long id, List<OrderItemDTO> orderItems) {
       Order order = orderRepository.findForOrderDetails(id, String.valueOf(id)).get();

       for(OrderItemDTO item : orderItems) {
           order.getOrderItems().stream().filter(x -> x.getSequence() == item.getSequence())
                   .findFirst().get()
                   .quantity(item.getQuantity())
                   .price(item.getPrice())
                   .lineTotal(item.getPrice().doubleValue()*item.getQuantity());
       }
       order.setSubtotal(calculateSubtotal(order));
       order.setTotal(calculateTotal(order));
       order = orderRepository.save(order);
       return orderMapper.toDto(order);
   }
    public BigDecimal calculateSubtotal(Order order) {
        BigDecimal sum = BigDecimal.valueOf(order.getOrderItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum());
        return sum;
    }

    public BigDecimal calculateTotal(Order order) {
        BigDecimal sum = BigDecimal.valueOf(order.getOrderItems().stream().mapToDouble(x -> x.getPrice().doubleValue() * x.getQuantity().doubleValue()).sum());
        if(order.getDeliveryTotal() != null)
            sum = sum.add(order.getDeliveryTotal());
        if(order.getDiscountsTotal() != null)
            sum = sum.subtract(order.getDiscountsTotal());
        return sum;
    }

    public void sendVoltageEmail(Long orderId, ArrayList<Long> orderItems) {
        Order order = orderRepository.getOrderWithSomeOrderItems(orderId, orderItems).orElse(null);
        OrderDTO dto = orderMapper.toDto(order);
        CustomerDTO customer = dto.getCustomer();
        mailService.sendVoltageMail(customer, dto);
    }
}
