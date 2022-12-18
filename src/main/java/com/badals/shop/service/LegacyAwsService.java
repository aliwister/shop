package com.badals.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class LegacyAwsService {

   private final S3Client s3Client;

   public S3Client getS3Client() {
      return s3Client;
   }
   public String getBucketName() {
      return bucketName;
   }


   @Value("${badals.s3bucket}")
   private String bucketName;

   @Value("${badals.accessKey1}")
   private String accessKey1;

   @Value("${badals.secretKey1}")
   private String secretKey1;



   public LegacyAwsService(S3Client s3Client) {
      this.s3Client = s3Client;
   }



   public URL presignPutUrl( String objectKey, String  contentType) {
      Region region = Region.EU_CENTRAL_1;
      S3Presigner presigner = S3Presigner.builder().credentialsProvider(StaticCredentialsProvider.create(new AwsCredentials() {
         @Override
         public String accessKeyId() {
            return accessKey1;
         }

         @Override
         public String secretAccessKey() {
            return secretKey1;
         }
      })).region(region).build();

      PresignedPutObjectRequest presignedRequest =
              presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                      .putObjectRequest(por -> por.bucket(bucketName).key(objectKey).contentType(contentType)));
      // Upload content to the bucket by using this URL
      return presignedRequest.url();
   }

   public URL presignGetUrl( String objectKey, String contentType) {
      Region region = Region.EU_CENTRAL_1;
      S3Presigner presigner = S3Presigner.builder().credentialsProvider(StaticCredentialsProvider.create(new AwsCredentials() {
         @Override
         public String accessKeyId() {
            return accessKey1;
         }

         @Override
         public String secretAccessKey() {
            return secretKey1;
         }
      })).region(region).build();

      PresignedGetObjectRequest presignedRequest =
              presigner.presignGetObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                      .getObjectRequest(por -> por.bucket(bucketName).key(objectKey)));
      return presignedRequest.url();
   }
}