package com.dkkhoa.possystem.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RenameFileUtil {
    public String gerateNewFileName(String name, MultipartFile file, int id) {
        String originalFilename = file.getOriginalFilename();

        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a custom format for date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        // Format the date and time
        String formattedDateTime = currentDateTime.format(formatter);

        // Extract file extension
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // Combine product name, date, and time to create the file name
        String fileName = name +  "_" + id + ".jpg";

        return fileName;
    }
}
