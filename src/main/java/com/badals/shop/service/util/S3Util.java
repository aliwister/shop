package com.badals.shop.service.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Util {
   private static S3Client s3;
   static {
      Region region = Region.EU_CENTRAL_1;
      s3 = S3Client.builder().region(region).build();
   }
   public static S3Client getClient() {
      return s3;
   }
}
