package com.badals.shop.service;

import com.badals.shop.domain.checkout.helper.Message;
import com.checkout.APIClient;
import com.checkout.api.services.charge.response.Refund;
import com.checkout.api.services.shared.Response;
import com.checkout.helpers.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.checkout.api.services.charge.request.ChargeRefund;

import java.util.Arrays;

@Service
@Transactional
public class CheckoutComService {

   @Value("${badals.sk}")
   private String _sk;

   private final Logger log = LoggerFactory.getLogger(CheckoutComService.class);

   public synchronized Message refund(String chargeId, String amount, String reference, String description) throws Exception {
      ChargeRefund refundPayload =new ChargeRefund();
      refundPayload.value = amount;
      refundPayload.trackId = reference;
      refundPayload.description =  description;

      try {
         // Create APIClient instance with your secret key
         APIClient ckoAPIClient= new APIClient(_sk, Environment.LIVE);
         // Submit your request and receive an apiResponse
         Response<Refund> apiResponse = ckoAPIClient.chargeService.refundRequest(chargeId,refundPayload);

         if(!apiResponse.hasError){
            // Access the response object retrieved from the api
            Refund refund = apiResponse.model;

         } else {
            // Api has returned an error object. You can access the details in the error property of the apiResponse.
            // apiResponse.error
            log.error(apiResponse.error.message);
            log.error(Arrays.toString(apiResponse.error.errors.toArray()));
         }
      } catch (Exception e) {}
      return null;
   }
}
