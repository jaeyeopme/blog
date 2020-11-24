package com.springboot.blog.common;

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
            if (newPhoto.isEmpty()) {
                return null;
            }
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(newPhoto.getContentType());
            metadata.setContentLength(newPhoto.getSize());

            String photoName = String.format("images/%s-%s", UUID.randomUUID(), newPhoto.getOriginalFilename());
            amazonS3.putObject(new PutObjectRequest(bucket, photoName, newPhoto.getInputStream(), metadata));
            return String.format("%s%s", url, photoName);
        } catch (Exception e) {
            throw new AmazonS3Exception("사진 저장에 실패했습니다.");
        }
    }

    public void deletePhoto(String photoName) {
        try {
            if (photoName.isEmpty()) {
                return;
            }
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, photoName));
        } catch (Exception e) {
            throw new AmazonS3Exception("사진 삭제에 실패했습니다.");
        }
    }
}
