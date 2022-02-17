package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Product;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.CRC32;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductContentService {

    private final Logger log = LoggerFactory.getLogger(ProductContentService.class);

    private final AwsService awsService;

    public ProductContentService(AwsService awsService) {
        this.awsService = awsService;
    }


    protected String uploadToS3(String image, Long currentMerchantId, String currentMerchant, Long tenantId) {
        String t = TenantContext.getCurrentTenant();
        String m = TenantContext.getCurrentMerchant();
        CRC32 checksum = new CRC32();
        checksum.update(image.getBytes());

        String objectKey = "_m/" + m + "/" + checksum.getValue()+image.substring(image.length()-4,image.length());
        try {
            BufferedImage img = ImageIO.read(new URL(image));
            if(img.getHeight() > 350 || img.getWidth() > 350)
                img = resizeAndCrop(img, 300, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img,"png", outputStream);
            PutObjectResponse response = awsService.getS3Client().putObject(PutObjectRequest.builder().bucket(awsService.getBucketName()).key(objectKey).build(), RequestBody.fromBytes(outputStream.toByteArray()));
            return "https://cdn.badals.com/"+ objectKey.substring(3);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    public BufferedImage resizeAndCrop(BufferedImage bufferedImage, Integer width, Integer height) {

        Scalr.Mode mode = (double) width / (double) height >= (double) bufferedImage.getWidth() / (double) bufferedImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH
                : Scalr.Mode.FIT_TO_HEIGHT;

        bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, mode, width, height);

        int x = 0;
        int y = 0;

        if (mode == Scalr.Mode.FIT_TO_WIDTH) {
            y = (bufferedImage.getHeight() - height) / 2;
        } else if (mode == Scalr.Mode.FIT_TO_HEIGHT) {
            x = (bufferedImage.getWidth() - width) / 2;
        }

        bufferedImage = Scalr.crop(bufferedImage, x, y, width, height);

        return bufferedImage;
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}
