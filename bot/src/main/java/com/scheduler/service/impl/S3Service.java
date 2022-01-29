package com.scheduler.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.scheduler.service.IS3Service;

import java.io.File;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class S3Service implements IS3Service {
    private AmazonS3 client;

    public S3Service(AmazonS3 client) {
        this.client = client;
    }

    @Override
    public void saveFileToS3(File file, String bucketName, String fileName) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        client.putObject(putObjectRequest);
    }
}
