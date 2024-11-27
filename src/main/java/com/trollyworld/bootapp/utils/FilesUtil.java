package com.trollyworld.bootapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class FilesUtil {
    public static String uploadFile(String destFolder, MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String randomUUID = UUID.randomUUID().toString();
        String generatedFileName = randomUUID.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = destFolder + File.separator + generatedFileName;
        File destPath = new File(destFolder);
        boolean isCreated = false;
        if (!destPath.exists())
            isCreated = destPath.mkdir();
        if (isCreated) log.info("Path Creation Status : {}", isCreated);
        else log.info("Path Already exists!");
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return generatedFileName;
    }
}
