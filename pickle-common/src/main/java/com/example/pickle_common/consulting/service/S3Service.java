package com.example.pickle_common.consulting.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.real_common.global.exception.error.NotFoundImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String saveFile(MultipartFile multipartFile, String name) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucketName, name, multipartFile.getInputStream(), metadata);
        } catch(IOException e) {
            throw new NotFoundImageException("파일이 없습니다.");
        }

        return amazonS3.getUrl(bucketName, name).toString();
    }

}
