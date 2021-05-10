package com.badals.shop.graph.mutation;

import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.service.PaymentService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class PaymentMutation implements GraphQLMutationResolver {

    private final PaymentService paymentService;

    public PaymentMutation(PaymentService paymentService) {
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

