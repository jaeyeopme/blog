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

    private final String blog;

    public AmazonService(AmazonS3 amazonS3,
                         @Value("${cloud.aws.s3.blog}") String blog) {
        this.amazonS3 = amazonS3;
        this.blog = blog;
    }

    public String saveImage(MultipartFile newImage) {
        String newImageName = String.format("images/%s-%s", UUID.randomUUID(), newImage.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(newImage.getContentType());
        metadata.setContentLength(newImage.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(blog, newImageName, newImage.getInputStream(), metadata));
        } catch (Exception e) {
            throw new AmazonS3Exception("Image save error.");
        }

        return amazonS3.getUrl(blog, newImageName).toString();
    }

    public void deleteImage(String oldImageUrl) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(blog, oldImageUrl));
        } catch (Exception e) {
            throw new AmazonS3Exception("Image delete error.");
        }
    }

    public String changeImage(MultipartFile newImage, String oldImageUrl) {
        deleteImage(oldImageUrl);
        return saveImage(newImage);
    }

}
