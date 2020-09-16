package com.badals.shop.service;

import com.badals.shop.service.util.S3Util;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class AwsService {

   public URL presignPutUrl( String objectKey, String  contentType) {
      Region region = Region.EU_CENTRAL_1;
         S3Presigner presigner = S3Presigner.builder().region(region).build();

         PresignedPutObjectRequest presignedRequest =
                 presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                         .putObjectRequest(por -> por.bucket(S3Util.getBucketName()).key(objectKey).contentType(contentType)));
         // Upload content to the bucket by using this URL
         return presignedRequest.url();
   }

   public URL presignGetUrl( String objectKey, String contentType) {
      Region region = Region.EU_CENTRAL_1;
      S3Presigner presigner = S3Presigner.builder().region(region).build();

      PresignedGetObjectRequest presignedRequest =
              presigner.presignGetObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                      .getObjectRequest(por -> por.bucket(S3Util.getBucketName()).key(objectKey)));
      // Upload content to the bucket by using this URL
      return presignedRequest.url();
   }
}
