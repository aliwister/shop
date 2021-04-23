package com.badals.shop.graph.query;


import com.badals.shop.graph.PaymentResponse;
import com.badals.shop.service.PaymentService;
import com.badals.shop.service.dto.PaymentDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AccountingQuery extends ShopQuery implements GraphQLQueryResolver {

    private final PaymentService paymentService;

    public AccountingQuery(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /*    @PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
        public List<PaymentDTO> payments(Integer limit) {
            return null;
        }*/
    //@PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
    //payments(paymentMethods: [String], offset: Int, limit: Int, searchText: String, dateFrom: Date, dateTo: Date, customerId: id, accountCode: String): PaymentResponse
    public PaymentResponse transactions(List<String> paymentMethods, Integer offset, Integer limit, String maxAmount, Date from, Date to, Long customerId, String accountCode, Boolean unsettledOnly){
        return paymentService.findForTable(paymentMethods,
                offset,
                limit,
                maxAmount,
                from,
                to,
                customerId,
                accountCode,
                unsettledOnly);
    }
    @PreAuthorize("hasRole('ROLE_FINANCE')")
    public PaymentDTO transaction(Long id) {
        return null;
    }
}
