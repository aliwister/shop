package com.badals.shop.xtra.amazon;

import java.math.BigDecimal;

public class PasUtility {
   private static final double USD2OMR = .386;
   private static final double LB2KG = 0.453592;
   private static final double OMRPERKG = 3.5;

   public static BigDecimal calculatePrice(BigDecimal cost, BigDecimal weight, double localShipping, double margin, double risk, double fixed, boolean isPrime, boolean isFulfilledByAmazon) {
      double dWeight = weight.doubleValue()*LB2KG;
      if (dWeight < .0001) {
         //throw new Exception("Unable to caculate the price [weight=0]");
         fixed = 10000000;
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

      double c_add = (double) (margin + risk) * dCost *.01 + localShipping;
      double w_add = (double) OMRPERKG * dWeight ;

      //if($isDirect) 		$w_add = 0 ;
      //if(!isInsurance) 	$insurance = 0;
      double dPrice = (dCost + c_add + insurance  ) * USD2OMR + fixed + w_add;
      dPrice = Math.round(dPrice*10.0)/10.0;

      BigDecimal price = BigDecimal.valueOf(dPrice);
      return price;
   }
}
