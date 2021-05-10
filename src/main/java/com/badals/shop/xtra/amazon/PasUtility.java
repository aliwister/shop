package com.badals.shop.xtra.amazon;

import com.badals.shop.domain.ProductOverride;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasUtility {
   private static final double USD2OMR = .386;
   private static final double LB2KG = 0.453592;
   private static final double OMRPERKG = 3.4;
   public static final double MINWEIGHT = 0.001;

   public static BigDecimal calculatePrice(BigDecimal cost, BigDecimal weight, double localShipping, double margin, double risk, double fixed, boolean isPrime, boolean isFulfilledByAmazon, String shippingCountry) throws PricingException {
      double dWeight = weight.doubleValue()*LB2KG;
      if (dWeight < MINWEIGHT) {
         throw new PricingException("Unable to caculate the price [weight=0]");
      }
      if (dWeight < .05)
         dWeight += .4;

      dWeight = Math.max(dWeight, .3);
      double dCost = cost.doubleValue();
      double insurance = Math.log(dCost/dWeight)* Math.log10(dCost/dWeight) * Math.max(Math.log10(Math.sqrt(dCost)),1);
      insurance = .1*Math.max(insurance, 0);
      insurance = Math.min(insurance, 10);
      insurance = Math.min(insurance, 2+dCost);
      insurance = Math.max(insurance, .02*dCost);

      if(!isPrime)
         insurance += 1+.15*dWeight;

      if(isPrime & dCost < 11 & dWeight < .5)
         insurance = -1;

      if(shippingCountry != null && !shippingCountry.equalsIgnoreCase("us"))
         risk ++;

      double c_add = (double) (margin + risk) * dCost *.01 + localShipping;
      double w_add = (double) OMRPERKG * dWeight ;

      //if($isDirect) 		$w_add = 0 ;
      //if(!isInsurance) 	$insurance = 0;
      double dPrice = (dCost + c_add + insurance  ) * USD2OMR + fixed + w_add;
      dPrice = Math.round(dPrice*9.9)/10.0;

      BigDecimal price = BigDecimal.valueOf(dPrice);
      return price;
   }

   public static int parseAvailability (PasItemNode item) {
      int availability = 5*24;
      if(!item.getAvailabilityType().equals("Now"))
         availability += 72;

      if(!item.isPrime())
         availability += 72;

      if(item.getAvailabilityMessage() == null)
         return availability;
      String msg = item.getAvailabilityMessage().toLowerCase();
      if(msg.equals("in stock")) return availability;

      int maxNum = parseMaxNumber(msg);
      if(msg.contains("day"))
         availability += maxNum*24;
      else if(msg.contains("week"))
         availability += maxNum*24*7;
      else if(msg.contains("month"))
         availability += maxNum*24*30;
      else {
         LocalDate date = parseDate(item.getAvailabilityMessage());
         if(date != null)
            availability += Math.abs(Period.between(date, LocalDate.now()).getDays())*24;
      }

      if(!item.getShippingCountry().equalsIgnoreCase("us"))
         availability *= 1.8;

      if(item.getShippingCountry().equalsIgnoreCase("cn"))
         availability *= 1.4;

      return availability;
   }

   private static int parseMaxNumber(String msg) {
      String regex = "\\d+";
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(msg);
      int MAX = 0;
      while(m.find()) {
         int num = Integer.parseInt(m.group());
         if(num > MAX)
            MAX = num;
      }
      return MAX;
   }

   private static LocalDate parseDate(String msg) {
      String regex = "\\d+";
      Pattern p = Pattern.compile("\\w+\\s\\d+(st)?(nd)?(rd)?(th)?,\\s+\\d+");
      Matcher m = p.matcher(msg);
      try {
         LocalDate localDate = null;
         while (m.find()) {
            m.find();
            String date = m.group();
            localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MMM dd, yyyy"));
         }
         return localDate;
      }
      catch (Exception e) {
         return null;
      }
   }

   public static BigDecimal calculateWeight(BigDecimal weight, ProductOverride override) {
      if (override == null) return weight;
      if(!override.isLazy()  ||  (override.isLazy() && (weight == null || weight.doubleValue() < MINWEIGHT)))
         return new BigDecimal(override.getOverride());
      return weight;
   }
}
