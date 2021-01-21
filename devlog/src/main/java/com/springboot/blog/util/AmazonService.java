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

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String url;

    public AmazonService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String putPhoto(MultipartFile newPhoto) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(newPhoto.getContentType());
            metadata.setContentLength(newPhoto.getSize());

            String newPhotoName = String.format("images/%s-%s", UUID.randomUUID(), newPhoto.getOriginalFilename());
            amazonS3.putObject(new PutObjectRequest(bucket, newPhotoName, newPhoto.getInputStream(), metadata));

            return String.format("%s%s", url, newPhotoName);
        } catch (Exception e) {
            throw new AmazonS3Exception("Photo save error");
        }
    }

    public void deletePhoto(String photoName) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, photoName));
        } catch (Exception e) {
            throw new AmazonS3Exception("Photo remove error.");
        }
    }

}
