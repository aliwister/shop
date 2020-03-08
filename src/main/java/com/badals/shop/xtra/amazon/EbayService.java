package com.badals.shop.xtra.amazon;

import ebay.apis.shopping.eblbasecomponents.GetSingleItemRequestType;
import ebay.apis.shopping.eblbasecomponents.ShoppingInterface;
import ebay.apis.shopping.eblbasecomponents.SimpleItemType;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.service.model.MessageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.apache.cxf.message.Message.PROTOCOL_HEADERS;
import static org.apache.cxf.phase.Phase.PRE_PROTOCOL;

public class EbayService {

   public static void main (String args[]) {
      EbayService service = new EbayService();
      //ShoppingInterface api = service.buildShoppingApiService();
      SimpleItemType item = service.getEbayItem("254197023475");
      System.out.println(item);
   }


   public SimpleItemType getEbayItem(String itemId) {
      GetSingleItemRequestType request = new GetSingleItemRequestType();
      request.setItemID(itemId);
      return buildShoppingApiService().getSingleItem(request).getItem();
   }

   private ShoppingInterface buildShoppingApiService() {
      JaxWsProxyFactoryBean serviceFactory = new JaxWsProxyFactoryBean();
      serviceFactory.setServiceClass(ShoppingInterface.class);
      serviceFactory.setAddress("http://open.api.ebay.com/shopping");
      Object port = serviceFactory.create();

      ClientProxy.getClient(port).getOutInterceptors().add(new AbstractSoapInterceptor(PRE_PROTOCOL) {
         @Override
         public void handleMessage(SoapMessage message) throws Fault {
            Map httpHeaders = (Map) message.get(PROTOCOL_HEADERS);
            MessageInfo soapMessageInfo = (MessageInfo) message.get(MessageInfo.class.getName());
            httpHeaders.put(
                    "X-EBAY-API-CALL-NAME",
                    singletonList(soapMessageInfo.getOperation().getName().getLocalPart())
            );
         }
      });

      Map<String, List<String>> headers = new HashMap<>();
      headers.put("X-EBAY-API-APP-ID", singletonList("BadalTra-5d42-45de-b09b-586ff14be57f"));
      headers.put("X-EBAY-API-REQUEST-ENCODING", singletonList("SOAP"));
      headers.put("X-EBAY-API-VERSION", singletonList("989"));
      ClientProxy.getClient(port).getRequestContext().put(PROTOCOL_HEADERS, headers);

      return (ShoppingInterface) port;
   }

}
