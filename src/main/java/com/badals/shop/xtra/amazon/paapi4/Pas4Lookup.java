/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License.
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package com.badals.shop.xtra.amazon.paapi4;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.SignedRequestsHelper;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Pas4Lookup {

   //@Autowired
   public SignedRequestsHelper helper;

   //private XStream xstream;

   public Pas4Lookup() {

   }

   public Pas4Lookup(SignedRequestsHelper helper) {
      this();
      this.helper = helper;
   }

   public PasItemNode lookup(String asin) {
      String requestUrl = null;

      Map<String, String> params = new HashMap<String, String>();
      params.put("Service", "AWSECommerceService");
      params.put("Version", "2010-06-01");
      params.put("Operation", "ItemLookup");
      params.put("ItemId", asin);
      params.put("ResponseGroup", "BrowseNodes,VariationMatrix,VariationOffers,VariationImages,OfferListings,ItemAttributes,Images,Similarities,EditorialReview,Reviews");
      params.put("AssociateTag", "deseneoma-20");

      requestUrl = helper.sign(params);
      System.out.println("Signed Request is \"" + requestUrl + "\"");
      return fetch(requestUrl);
   }

   /*
    * Utility function to fetch the response from the service and extract the title
    * from the XML.
    */
   private PasItemNode fetch(String requestUrl) {
      //PasItemNode doc = null;
      try {
         //File file = new File("C:\\work\\shop\\pas4wVariations.xml");
         //URL req = file.toURI().toURL(); //
         URL req = new URL(requestUrl);

         //xstream.setClassLoader(Thread.currentThread().getContextClassLoader());// does not require XPP3 library starting with Java 6
         //doc = (ItemLookupResponse) xstream.fromXML(req.openStream());
         //return doc;


         boolean isParent = true;
         boolean inItem = true;
         boolean isItemDim = false;
         boolean isPkgDim = false;

         boolean isVariation = false;
         boolean isPrice = false;
         boolean isListPrice = false;
         boolean isMerchant = false;
         boolean isImgSet = false;
         boolean isLargeImage = false;
         boolean isSimilarity = false;
         boolean isEditorial = false;

         XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
         XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new BufferedInputStream(req.openStream()));
         PasItemNode parent = null;
         PasItemNode item = null;
         Attribute attribute = null;
         while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {

               //System.out.println(reader.next());
               switch(reader.getLocalName()) {
                  case "Variations":
                     isParent = false;
                     break;
                  case "Item":
                     item = new PasItemNode(isParent);
                     if(isParent)
                        parent = item;
                     break;
                  case "ASIN":
                     reader.next();
                     if(isSimilarity)
                        item.getSimilarities().add(reader.getText());
                     else
                        item.setAsin(reader.getText());
                     break;
                  case "ParentASIN":
                     reader.next();
                     if(reader.hasText())
                        item.setParentAsin(reader.getText());
                     break;
                  case "Brand":
                     reader.next();
                     if(reader.hasText())
                        item.setBrand(reader.getText());
                     break;
                  case "EAN":
                     reader.next();
                     if(reader.hasText())
                        item.setEan(reader.getText());
                     break;
                  case "PartNumber":
                     reader.next();
                     if(reader.hasText())
                        item.setPartNumber(reader.getText());
                     break;
                  case "ItemDimensions":
                     isItemDim = true;
                     break;
                  case "PackageDimensions":
                     isItemDim = true;
                     break;
                  case "Weight":
                     reader.next();
                     if (isItemDim)
                        item.setItemWeight(reader.getText());
                     else if(isPkgDim)
                        item.setPackageWeight(reader.getText());
                     break;
                  case "Manufacturer":
                     reader.next();
                     if(reader.hasText())
                        item.setManufacturer(reader.getText());
                     break;
                  case "ProductGroup":
                     reader.next();
                     if(reader.hasText())
                        item.setProductGroup(reader.getText());
                     break;
                  case "ProductTypeName":
                     reader.next();
                     if(reader.hasText())
                        item.setProductType(reader.getText());
                     break;
                  case "Title":
                     reader.next();
                     if(reader.hasText())
                        item.setTitle(reader.getText());
                     break;
                  case "MaximumHours":
                     reader.next();
                     if(reader.hasText())
                        item.setMaxHours(reader.getText());
                     break;
                  case "IsEligibleForSuperSaverShipping":
                     reader.next();
                     if(reader.hasText())
                        item.setSuperSaver(reader.getText().equals("1"));
                     break;
                  case "IsEligibleForPrime":
                     reader.next();
                     if(reader.hasText())
                        item.setPrime(reader.getText().equals("1"));
                     break;
                  case "VariationAttribute":
                     isVariation = true;
                     break;
                  case "Merchant":
                     isMerchant = true;
                     break;
                  case "ListPrice":
                     isListPrice = true;
                     break;
                  case "OfferListing":
                     isPrice = true;
                     break;
                  case "Name":
                     reader.next();
                     if(isVariation) {
                        attribute = new Attribute();
                        attribute.setName(reader.getText());
                     }
                     else if(isMerchant)
                        item.setMerchant(reader.getText());
                     break;
                  case "Value":
                     if(isVariation) {
                        reader.next();
                        attribute.setValue(reader.getText());
                        item.getVariationAttributes().add(attribute);
                     }
                     break;
                  case "Amount":
                     reader.next();
                     if(isListPrice)
                        item.setListPrice(new BigDecimal(reader.getText()));
                     else if(isPrice)
                        item.setCost(new BigDecimal(reader.getText()));
                     break;
                  case "ImageSet":
                     isImgSet = true;
                     break;
                  case "LargeImage":
                     isLargeImage = true;
                     break;
                  case "URL":
                     reader.next();
                     if(isImgSet && isLargeImage)
                        item.getGallery().add(reader.getText());
                     else if(isLargeImage)
                        item.setImage(reader.getText());
                     break;
                  case "DetailPageURL":
                     reader.next();
                     item.setUrl(reader.getText());
                     break;
                  case "SimilarProduct":
                     isSimilarity = true;
                     break;
                  case "EditorialReview":
                     isEditorial = true;
                     break;
                  case "Content":
                     reader.next();
                     if(isEditorial)
                        item.setEditorial(reader.getText());
                     break;
               }
            }
            if(reader.getEventType() == XMLStreamReader.END_ELEMENT) {
               switch(reader.getLocalName()) {
                  case "Variations":
                     isParent = true;
                     item = parent;
                     break;
                  case "ASIN":
                     break;
                  case "ItemDimensions":
                     isItemDim = false;
                     break;
                  case "PackageDimensions":
                     isPkgDim = false;
                     break;
                  case "Item":
                     if(!isParent)
                        parent.getChildren().add(item);
                     break;
                  case "VariationAttribute":
                     isVariation = false;
                     break;
                  case "Merchant":
                     isMerchant = false;
                     break;
                  case "ImageSet":
                     isImgSet = false;
                     break;
                  case "LargeImage":
                     isLargeImage = false;
                     break;
                  case "ListPrice":
                     isListPrice = false;
                     break;
                  case "OfferListing":
                     isPrice = false;
                     break;
                  case "SimilarProduct":
                     isSimilarity = false;
                     break;
                  case "EditorialReview":
                     isEditorial = false;
                     break;
               }
            }
         }
         return parent;

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static void main(String args[]) throws IOException, XMLStreamException {
      File file = new File("C:\\work\\shop\\pas4wVariations.xml");
      URL req = file.toURI().toURL();

/*
      Map<String, Class<?>> aliases = new HashMap<>();
      aliases.put("Item", ItemNode.class);
      aliases.put("ImageSet", ImageSetNode.class);
      aliases.put("VariationDimension", String.class);
      //xstream.addImplicitCollection(VariationsNode.class, "items", "Item", ItemNode.class);
      aliases.put("EditorialReview", EditorialReviewNode.class);
      aliases.put("SimilarProduct", SimilarProductNode.class);
      aliases.put("VariationAttribute", VariationAttributeNode.class);
      //XStream xstream = new XStream(new StaxDriver());
      //XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
      //xStreamMarshaller.setAliases(aliases);
*/


      boolean isParent = true;
      boolean inItem = true;
      boolean isItemDim = false;
      boolean isPkgDim = false;

      boolean isVariation = false;
      boolean isPrice = false;
      boolean isListPrice = false;
      boolean isMerchant = false;
      boolean isImgSet = false;
      boolean isLargeImage = false;


      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(req.openStream());
      PasItemNode parent = null;
      PasItemNode item = null;
      Attribute attribute = null;
      while (reader.hasNext()) {
         reader.next();
         if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {

            //System.out.println(reader.next());
            switch(reader.getLocalName()) {
               case "Variations":
                  isParent = false;
                  break;
               case "Item":
                  item = new PasItemNode(isParent);
                  if(isParent)
                     parent = item;
                  break;
               case "ASIN":
                  reader.next();
                  if(reader.hasText())
                     item.setAsin(reader.getText());
                  break;
               case "Brand":
                  reader.next();
                  if(reader.hasText())
                     item.setBrand(reader.getText());
                  break;
               case "EAN":
                  reader.next();
                  if(reader.hasText())
                     item.setEan(reader.getText());
                  break;
               case "PartNumber":
                  reader.next();
                  if(reader.hasText())
                     item.setPartNumber(reader.getText());
                  break;
               case "ItemDimensions":
                  isItemDim = true;
                  break;
               case "PackageDimensions":
                  isItemDim = true;
                  break;
               case "Weight":
                  reader.next();
                  if (isItemDim)
                     item.setItemWeight(reader.getText());
                  else if(isPkgDim)
                     item.setPackageWeight(reader.getText());
                  break;
               case "Manufacturer":
                  reader.next();
                  if(reader.hasText())
                     item.setManufacturer(reader.getText());
                  break;
               case "ProductGroup":
                  reader.next();
                  if(reader.hasText())
                     item.setProductGroup(reader.getText());
                  break;
               case "ProductTypeName":
                  reader.next();
                  if(reader.hasText())
                     item.setProductType(reader.getText());
                  break;
               case "Title":
                  reader.next();
                  if(reader.hasText())
                     item.setTitle(reader.getText());
                  break;
               case "MaximumHours":
                  reader.next();
                  if(reader.hasText())
                     item.setMaxHours(reader.getText());
                  break;
               case "IsEligibleForSuperSaverShipping":
                  reader.next();
                  if(reader.hasText())
                     item.setSuperSaver(reader.getText().equals("1"));
                  break;
               case "IsEligibleForPrime":
                  reader.next();
                  if(reader.hasText())
                     item.setPrime(reader.getText().equals("1"));
                  break;
               case "VariationAttribute":
                  isVariation = true;
                  break;
               case "Merchant":
                  isMerchant = true;
                  break;
               case "ListPrice":
                  isListPrice = true;
                  break;
               case "OfferListing":
                  isPrice = true;
                  break;
               case "Name":
                  reader.next();
                  if(isVariation) {
                     attribute = new Attribute();
                     attribute.setName(reader.getText());
                  }
                  else if(isMerchant)
                     item.setMerchant(reader.getText());
                  break;
               case "Value":
                  if(isVariation) {
                     reader.next();
                     attribute.setValue(reader.getText());
                     item.getVariationAttributes().add(attribute);
                  }
                  break;
               case "Amount":
                  reader.next();
                  if(isListPrice)
                     item.setListPrice(new BigDecimal(reader.getText()));
                  else if(isPrice)
                     item.setCost(new BigDecimal(reader.getText()));
                  break;
               case "ImageSet":
                  isImgSet = true;
                  break;
               case "LargeImage":
                  isLargeImage = true;
                  break;
               case "URL":
                  reader.next();
                  if(isImgSet && isLargeImage)
                     item.getGallery().add(reader.getText());
                  else if(isLargeImage)
                     item.setImage(reader.getText());
                  break;
               case "DetailPageURL":
                  reader.next();
                  item.setUrl(reader.getText());
                  break;
            }
         }
         if(reader.getEventType() == XMLStreamReader.END_ELEMENT) {
            switch(reader.getLocalName()) {
               case "Variations":
                  isParent = true;
                  item = parent;
                  break;
               case "ASIN":
                  break;
               case "ItemDimensions":
                  isItemDim = false;
                  break;
               case "PackageDimensions":
                  isPkgDim = false;
                  break;
               case "Item":
                  if(!isParent)
                     parent.getChildren().add(item);
                  break;
               case "VariationAttribute":
                  isVariation = false;
                  break;
               case "Merchant":
                  isMerchant = false;
                  break;
               case "ImageSet":
                  isImgSet = false;
                  break;
               case "LargeImage":
                  isLargeImage = false;
                  break;
               case "ListPrice":
                  isListPrice = false;
                  break;
               case "OfferListing":
                  isPrice = false;
                  break;
            }
         }
      }

      System.out.println(parent.getAsin());
      parent.getChildren().forEach(
              x -> System.out.println(x)
      );



      /*
      XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(req.openStream());
      xmlFileReader.setUnmarshaller(xStreamMarshaller);
      SynchronizedItemStreamReader< ItemNode> synchronizedItemStreamReader = new SynchronizedItemStreamReader<>();
      synchronizedItemStreamReader.setDelegate(xmlFileReader);
*/
   }
}
