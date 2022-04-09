package org.sj.capston.debug.debugbackend.util;

import org.sj.capston.debug.debugbackend.entity.BoardImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageStore {

    @Value("${my-app.image.dir}")
    public String imageDir;

    public String getFullPath(String filename) {
        return imageDir + filename;
    }

    public BoardImage storeImage(MultipartFile multipartImage) throws IOException {
        String originalImageName = multipartImage.getOriginalFilename();
        String storedImageName = createStoreFileName(originalImageName);

        multipartImage.transferTo(new File(getFullPath(storedImageName)));

        return BoardImage.builder()
                .originName(originalImageName)
                .storedName(storedImageName)
                .build();
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();

        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
