package com.badals.shop.controller;

import com.badals.shop.domain.Shipment;
import com.badals.shop.domain.ShipmentDoc;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.enumeration.ShipmentStatus;
import com.badals.shop.service.AwsService;
import com.badals.shop.service.ShipmentDocService;
import com.badals.shop.service.ShipmentService;
import com.badals.shop.service.TenantOrderService;
import com.badals.shop.service.pojo.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

@Controller
@RequestMapping("/myus/adfadf32423423423")
public class MyusController {
   private final Logger log = LoggerFactory.getLogger(MyusController.class);
   private final ShipmentService shipmentService;
   private final ShipmentDocService shipmentDocService;
   private final TenantOrderService orderService;
   private final AwsService awsService;

   public MyusController(ShipmentService shipmentService, ShipmentDocService shipmentDocService, TenantOrderService orderService, AwsService awsService) {
      this.shipmentService = shipmentService;
      this.shipmentDocService = shipmentDocService;
      this.orderService = orderService;
      this.awsService = awsService;
   }

   @RequestMapping(path="/pkg", method= RequestMethod.POST)
   public ResponseEntity<Message> pkg(@RequestBody JsonNode json) throws IOException, URISyntaxException, ParseException {

      //_emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", json);
/*      log.info("detrack-------------------------------------------------------------------------------------------");
      ObjectMapper mapper = new ObjectMapper();
      DetrackDelivery confirmation = mapper.readValue(json, DetrackDelivery.class);
      log.info(confirmation.getStatus() + " " + confirmation.get_do() + " " + confirmation.getView_signature_url());
      log.info("detrack-------------------------------------------------------------------------------------------2");
      updateDelivery(confirmation);*/
      //  _emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", confirmation.getAddress());
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(json,headers);
      ResponseEntity<byte[]> response = restTemplate.postForEntity("https://webhook.site/53dd5d04-8835-4ce5-8c47-a19d0bd95048", entity, byte[].class);
      return new ResponseEntity<Message>(new Message("Success", "200"), HttpStatus.OK);//.status(HttpStatus.FOUND).location(URI.create("https://webhook.site/0603401a-4e8f-42be-b3df-7f2bc3c9335b")).build();

   }
   @RequestMapping(path="/shipment", method= RequestMethod.POST)
   public ResponseEntity<Message> shipment(@RequestBody JsonNode json) throws IOException, URISyntaxException, ParseException {
/*
      //_emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", json);
      log.info("detrack-------------------------------------------------------------------------------------------");
      ObjectMapper mapper = new ObjectMapper();
      DetrackDelivery confirmation = mapper.readValue(json, DetrackDelivery.class);
      log.info(confirmation.getStatus() + " " + confirmation.get_do() + " " + confirmation.getView_signature_url());
      log.info("detrack-------------------------------------------------------------------------------------------2");
      updateDelivery(confirmation);
      //  _emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", confirmation.getAddress());*/
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(json,headers);
      ResponseEntity<byte[]> response = restTemplate.postForEntity("https://webhook.site/530bf620-a8ef-48b0-a423-a83c2a98739d", entity, byte[].class);
      return new ResponseEntity<Message>(new Message("Success", "200"), HttpStatus.OK);//.status(HttpStatus.FOUND).location(URI.create("https://webhook.site/0603401a-4e8f-42be-b3df-7f2bc3c9335b")).build();
   }
}