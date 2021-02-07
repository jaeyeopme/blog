package com.springboot.blog.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class AmazonService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.blog}")
    private String blog;

    @Value("${cloud.aws.s3.url}")
    private String url;

    public AmazonService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String putImage(MultipartFile image, String imageName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            amazonS3.putObject(new PutObjectRequest(blog, imageName, image.getInputStream(), metadata));

            return String.format("%s%s", url, imageName);
        } catch (Exception e) {
            throw new AmazonS3Exception("Image save error.");
        }
    }

    public void deleteImage(String imageName) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(blog, imageName));
        } catch (Exception e) {
            throw new AmazonS3Exception("Image delete error.");
        }
    }

}
