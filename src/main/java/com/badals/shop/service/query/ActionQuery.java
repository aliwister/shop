package com.badals.shop.service.query;


import com.badals.shop.domain.pojo.PaymentResponse;
import com.badals.shop.service.ActionService;
import com.badals.shop.service.PaymentService;
import com.badals.shop.service.dto.ActionDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ActionQuery extends ShopQuery implements GraphQLQueryResolver {

    private final ActionService actionService;

    public ActionQuery(ActionService actionService) {
        this.actionService = actionService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ActionDTO> auditActivity(Long id, String type) {
        return actionService.auditActivity(id, type);
    }
}
