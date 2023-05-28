package com.badals.shop.service;

import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.service.dto.PurchaseItemDTO;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class AmazonPurchaseService {
   private String identity = "API8317721671";
   private String sharedSecret = "Nlz6Sced7oH1pCUA09x5P6VAj7BSG0";
   private String payloadIdTail = "@teknify.com.face.admin1";
   //private String timestamp = "2023-02-11T21:00:07+00:00";
   private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
   private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

   String itemXml = "<ItemOut quantity=\"{{quantity}}\" lineNumber=\"{{line}}\">\n" +
           "                <!-- Mandatory -->\n" +
           "                <ItemID>\n" +
           "                    <SupplierPartID>{{sku}}</SupplierPartID>              \n" +
           "                </ItemID>\n" +
           "                <ItemDetail>\n" +
           "                    <UnitPrice>\n" +
           "                        <Money currency=\"USD\">{{price}}</Money>\n" +
           "                        <!-- Mandatory, Unit price of the item -->\n" +
           "                    </UnitPrice>\n" +
           "                    <Description xml:lang=\"en\">{{description}}</Description>\n" +
           "                    <UnitOfMeasure>EA</UnitOfMeasure>\n" +
           "                    <Classification domain=\"\"/>\n" +
           "                </ItemDetail></ItemOut>";

   public InputStream getCxml() {
      return getClass().getResourceAsStream("/amazon-purchase.cxml");

   }
   public String convertStreamToString(InputStream is) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
         sb.append(line + "\n");
      }

      is.close();
      return sb.toString();
   }
   public String buy(PurchaseDTO purchase) throws IOException {
      //File resource = getCxml().getFile();
      String cxml = convertStreamToString(getCxml());

      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_XML);
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());

      String xmlRequestBody = cxml.replaceAll("\\{\\{SharedSecret\\}\\}", sharedSecret)
              .replaceAll("\\{\\{Identity\\}\\}", identity)
              .replaceAll("\\{\\{payloadId\\}\\}", generatePayload(purchase.getId(), purchase.getCreatedDate())+payloadIdTail)
              .replaceAll("\\{\\{timestamp\\}\\}", sdf2.format(timestamp))
              .replaceAll("\\{\\{orderDate\\}\\}", sdf2.format(timestamp))
              .replaceAll("\\{\\{total\\}\\}", purchase.getTotal().toString())
              .replaceAll("\\{\\{po\\}\\}", purchase.getId().toString());

      StringBuilder items = buildItems(purchase);

      HttpEntity<String> request = new HttpEntity<>(xmlRequestBody.replaceAll("\\{\\{items\\}\\}", items.toString()), headers);

      ResponseEntity<String> response = restTemplate.exchange("https://https-ats.amazonsedi.com/f7b9e4ae-0ce6-40df-9377-05ac4ec1a8e6", HttpMethod.POST, request, String.class);
      xmlRequestBody = null;


      String responseBody = response.getBody();
      return responseBody;
   }

   private StringBuilder buildItems(PurchaseDTO p) {
      StringBuilder items = new StringBuilder();
      for (PurchaseItemDTO item: p.getPurchaseItems()) {
         items.append(itemXml
                 .replaceAll("\\{\\{quantity\\}\\}", item.getQuantity().toString())
                 .replaceAll("\\{\\{line\\}\\}", item.getSequence().toString())
                 .replaceAll("\\{\\{sku\\}\\}", item.getSku())
                 .replaceAll("\\{\\{price\\}\\}", item.getPrice().toString())
                 .replaceAll("\\{\\{description\\}\\}", StringEscapeUtils.escapeXml(item.getDescription()))
                 );
      }
      return items;
   }

   private String generatePayload(Long po, Date date) {
      return sdf2.format(date)+"."+po;
   }
}
