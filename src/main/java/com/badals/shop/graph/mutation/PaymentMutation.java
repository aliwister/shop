package com.badals.shop.graph.mutation;

import com.badals.shop.service.TenantPaymentService;
import com.badals.shop.service.pojo.Message;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class PaymentMutation implements GraphQLMutationResolver {

    private final TenantPaymentService paymentService;

    public PaymentMutation(TenantPaymentService paymentService) {
        this.paymentService = paymentService;
    }


    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message voidPayment(Long id) {
        try {
            this.paymentService.voidPayment(id);
        } catch (IllegalStateException e) {
            return new Message("Failure");
        }

        return new Message("Success");
    }

}

