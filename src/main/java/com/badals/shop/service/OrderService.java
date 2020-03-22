package com.badals.shop.service;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.OrderRepository;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.mapper.OrderMapper;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, UserService userService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.customerService = customerService;
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
        order.setConfirmationKey(null);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public List<OrderDTO> getCustomerOrders() {
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        return orderRepository.findOrdersByCustomerOrderByCreatedDateDesc(loginUser).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<OrderDTO> getOrders(List<OrderState> orderState, Integer limit, String searchText) {
        return orderRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(0,100)).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void sendPaymnetMessage(Long id) {
        Optional<Order> o = orderRepository.findJoinCustomerJoinAddress(id);
        OrderDTO order = o.map(orderMapper::toDto).orElse(null);

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
}
