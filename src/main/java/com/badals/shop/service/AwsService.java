package com.badals.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class AwsService {

   private final S3Client s3Client;

   public S3Client getS3Client() {
      return s3Client;
   }
   public String getBucketName() {
      return bucketName;
   }


   @Value("${profileshop.s3bucket}")
   private String bucketName;

   @Value("${profileshop.s3region}")
   private String regionValue;

   public AwsService(S3Client s3Client) {
      this.s3Client = s3Client;
   }



   public URL presignPutUrl( String objectKey, String  contentType) {
      Region region =  Region.of(regionValue);
         S3Presigner presigner = S3Presigner.builder().region(region).build();

         PresignedPutObjectRequest presignedRequest =
                 presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                         .putObjectRequest(por -> por.bucket(bucketName).key(objectKey).contentType(contentType)));
         // Upload content to the bucket by using this URL
         return presignedRequest.url();
   }

   public URL presignGetUrl( String objectKey, String contentType) {
      Region region = Region.of(regionValue);
      S3Presigner presigner = S3Presigner.builder().region(region).build();

      PresignedGetObjectRequest presignedRequest =
              presigner.presignGetObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                      .getObjectRequest(por -> por.bucket(bucketName).key(objectKey)));
      return presignedRequest.url();
   }
}
