package com.scheduler.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.scheduler.service.IS3Service;
import com.scheduler.utils.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class S3Service implements IS3Service {
    private final AmazonS3 client;

    public S3Service(AmazonS3 client) {
        this.client = client;
    }

    @Override
    public void saveFileToS3(File file, String bucketName, String fileName) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        client.putObject(putObjectRequest);
    }

    @Override
    public File getFileFromS3(String bucketName, String fileKey) {
        String path = FileUtils.BASE_FILE_DIRECTORY + fileKey;
        File file = FileUtils.getOrCreateFile(path);
        try (final S3Object s3Object = client.getObject(bucketName, fileKey);
             final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8)) {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.copy(streamReader, fileOutputStream);
            return file;
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            if (e.getErrorCode().equalsIgnoreCase("NoSuchKey")) {
                return null;
            }
            throw new RuntimeException("Could not read file from s3 " + e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get or create file from file system " + e);
        }
    }
}

