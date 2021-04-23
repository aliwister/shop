package com.badals.shop.controller;

import com.badals.shop.domain.Action;
import com.badals.shop.repository.ActionRepository;
import com.badals.shop.repository.PaymentRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;

@Controller
@RequestMapping("/cybersource3402873490")
public class CybersourcePaymentController {
   private final Logger log = LoggerFactory.getLogger(CybersourcePaymentController.class);
   private final ActionRepository actionRepository;
   private final PaymentRepository paymentRepository;

   public CybersourcePaymentController(ActionRepository actionRepository, PaymentRepository paymentRepository) {
      this.actionRepository = actionRepository;
      this.paymentRepository = paymentRepository;
   }

   @PostMapping("/process")
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

