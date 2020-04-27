package com.midas2018mobile5.serverapp.domain.cafe;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeImage {
    @Column(name = "imgUrl", length = 1024)
    private String value;

    @Transient
    private String imgName;

    @Builder
    public CafeImage(MultipartFile file) throws IOException {
        if (file != null) {
            this.value = saveImage(file);
            imgName = file.getOriginalFilename();
        }
    }

    public String getEndPoint() {
        return "/api/cafe/img/";
    }

    public String getImgName() {
        return imgName;
    }

    private static final String staticPath = "src/main/resources/static";

    public String saveImage(MultipartFile file) throws IOException {
        final String currentPath = System.getProperty("user.dir");
        final String UPLOAD_PATH = currentPath + File.separator + staticPath;

        String fileName = LocalDateTime.now() + "_" + file.getOriginalFilename().substring(0, 3);
        String savePath = UPLOAD_PATH + File.separator + fileName;

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(savePath)));
        stream.write(file.getBytes());
        stream.close();

        return getEndPoint() + fileName;
    }

    public void deleteImage() {
        if (this.value != null) {
            File target = new File(staticPath + File.separator + imgName);
            target.deleteOnExit();
        }
    }
}
