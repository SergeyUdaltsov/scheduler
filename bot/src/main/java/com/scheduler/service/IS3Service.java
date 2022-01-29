package com.scheduler.service;

import java.io.File;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public interface IS3Service {

    void saveFileToS3(File file, String bucketName, String fileName);
}
