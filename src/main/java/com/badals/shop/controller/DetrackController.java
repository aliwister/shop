package com.badals.shop.controller;

import com.badals.shop.domain.Shipment;
import com.badals.shop.domain.ShipmentDoc;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.enumeration.ShipmentStatus;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.ShipmentDocService;
import com.badals.shop.service.ShipmentService;
import com.badals.shop.service.util.S3Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

@Controller
@RequestMapping("/detrack")
public class DetrackController {
   private final Logger log = LoggerFactory.getLogger(DetrackController.class);
   private final ShipmentService shipmentService;
   private final ShipmentDocService shipmentDocService;
   private final OrderService orderService;

   public DetrackController(ShipmentService shipmentService, ShipmentDocService shipmentDocService, OrderService orderService) {
      this.shipmentService = shipmentService;
      this.shipmentDocService = shipmentDocService;
      this.orderService = orderService;
   }

   @RequestMapping(method= RequestMethod.POST)
   public String index(@RequestParam("json") String json) throws IOException, URISyntaxException, ParseException {

      //_emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", json);
      log.info("detrack-------------------------------------------------------------------------------------------");
      ObjectMapper mapper = new ObjectMapper();
      DetrackDelivery confirmation = mapper.readValue(json, DetrackDelivery.class);
      log.info(confirmation.getStatus() + " " + confirmation.get_do() + " " + confirmation.getView_signature_url());
      log.info("detrack-------------------------------------------------------------------------------------------2");
      updateDelivery(confirmation);
      //  _emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", confirmation.getAddress());
      return "";
   }
   public void updateDelivery(DetrackDelivery confirmation) throws IOException {
      // TODO Auto-generated method stub

      log.info("DELIVERY SERVICE-------------------------------------------------------------------------------------------2");
      Long shipmentId = Long.parseLong(confirmation.get_do().split("-")[0]);
      //Integer shipmentId = Integer.parseInt(confirmation.get_do().split("-")[1]);

      if(confirmation.getStatus().equalsIgnoreCase("not delivered")) {
         shipmentService.setStatus(shipmentId, ShipmentStatus.FAILED);
         return;
      }
      shipmentService.setStatus(shipmentId, ShipmentStatus.DELIVERED);

      String fileKey = "pod/"+confirmation.get_do()+".pdf";
      String imageKey = "pod/"+confirmation.get_do()+".jpg";

      this.detrackPhotoUploadToS3(confirmation, fileKey,POD_URL);

      Shipment shipment = shipmentService.findOne(shipmentId).get();
/*      if(shipment.getProgressDone().equals(shipment.getProgressTotal()))
         shipmentService.updateOrder(shipment.getReference(), OrderState.DELIVERED);
      else*/
      orderService.setStatus(shipment.getReference(), OrderState.DELIVERED);

      detrackPhotoUploadToS3(confirmation, fileKey, POD_URL);
      detrackPhotoUploadToS3(confirmation, imageKey, PHOTO1_URL);

      shipmentDocService.save(new ShipmentDoc().fileKey(fileKey).shipment(shipment));
      shipmentDocService.save(new ShipmentDoc().fileKey(imageKey).shipment(shipment));
   }

   final static String PHOTO1_URL = "https://app.detrack.com/api/v1/deliveries/photo_1.json";
   final static String POD_URL = "https://app.detrack.com/api/v1/deliveries/export.pdf";

   public void detrackPhotoUploadToS3(DetrackDelivery confirmation, String fileKey, String url) throws IOException {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      params.add("key", "530e610169c28ce227a9716ba28a386cedc6d2dbefef67eb");

      //String date = "2017-05-02", _do = "43784-7468";
      String json = "{\"date\":\""+ confirmation.getDate() +"\",\"do\":\""+ confirmation.get_do() +"\"}";

      params.add("json", json);
      params.add("url", url);

      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<byte[]> response = restTemplate.postForEntity(url, params, byte[].class);

      if (response.getStatusCode() != HttpStatus.OK) {
         //Log error
      }
      S3Util.getClient().putObject(PutObjectRequest.builder().bucket(S3Util.getBucketName()).key(fileKey).build(), RequestBody.fromInputStream(new ByteArrayInputStream(response.getBody()), response.getBody().length));
   }
}
