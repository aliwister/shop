package com.badals.shop.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfiguration {
   private static S3Client s3;

   @Value("${aws.s3bucket}")
   private String bucketName;

   @Bean(name="S3Client")
   public S3Client getS3Client() {
      Region region = Region.EU_CENTRAL_1;
      return S3Client.builder().region(region).build();
   }

}
