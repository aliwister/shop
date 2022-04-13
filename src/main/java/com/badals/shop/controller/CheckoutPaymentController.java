package com.badals.shop.controller;

import com.badals.shop.domain.Action;
import com.badals.shop.repository.ActionRepository;
import com.badals.shop.repository.CheckoutCartRepository;
import com.badals.shop.repository.PaymentRepository;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.ShipmentDocService;
import com.badals.shop.service.ShipmentService;
import com.checkout.api.services.charge.response.Capture;
import com.checkout.api.services.charge.response.Charge;
import com.checkout.api.services.charge.response.Refund;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;

@Controller
@RequestMapping("/checkout1432u102398019238092183")
public class CheckoutPaymentController {
   private final Logger log = LoggerFactory.getLogger(CheckoutPaymentController.class);
   private final ActionRepository actionRepository;
   private final PaymentRepository paymentRepository;

   public CheckoutPaymentController(ActionRepository actionRepository, PaymentRepository paymentRepository) {
      this.actionRepository = actionRepository;
      this.paymentRepository = paymentRepository;
   }

   @PostMapping("/capture")
   public String capture(@RequestBody CaptureResponse json) throws IOException, URISyntaxException, ParseException {
      log.info("checkout.com CHARGE-------------------------------------------------------------------------------------------");
      String cartId = json.message.trackId;
      String captureId = json.message.id;
      //String amount = json.message.

      //paymentRepository.updateCaptureId(cartId, captureId, amount);
      addAuditEvent(cartId, "payment", "capture", json.message);

      log.info("checkout.com CHARGE-------------------------------------------------------------------------------------------2");
      //  _emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", confirmation.getAddress());
      return "";
   }

   private void addAuditEvent(String cartId, String className, String methodName, com.checkout.api.services.charge.response.Void state) {
      Gson gson = new Gson();
      String json = gson.toJson(state);
      Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
      Action action = new Action();
      action.setAction(methodName);
      action.setObject(className);
      action.setState(json);
      action.setCreatedDate(timestamp);
      action.setObjectId(cartId);
      actionRepository.save(action);
   }

   @PostMapping("/charge")
   public String charge(@RequestBody CaptureResponse json) throws IOException, URISyntaxException, ParseException {
      log.info("checkout.com-------------------------------------------------------------------------------------------");

      log.info("checkout.com-------------------------------------------------------------------------------------------2");
      //updatePayment(json.message);
      //  _emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", confirmation.getAddress());
      return "";
   }
}

enum EVENT {
   AUTHORIZED("charge.succeeded"),
   CAPTURED("charge.captured"),
   FAILED("charge.failed");
   public final String value;
   private EVENT(String value) {
      this.value = value;
   }
}

class CaptureResponse {
   String eventType;
   Capture message;
}
class ChargeResponse {
   String eventType;
   Charge message;
}
class RefundResponse {
   String eventType;
   Refund message;
}
class VoidResponse {
   String eventType;
   com.checkout.api.services.charge.response.Void message;
}