package com.midas2018mobile5.serverapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
@RequestMapping("/api/upload")
public class UploadController {
    private final String UPLOAD_PATH;

    @Autowired
    public UploadController(String currentPath) {
        this.UPLOAD_PATH = currentPath + File.separator + "src/main/resources/static";
    }

    @RequestMapping(value = "image", method = RequestMethod.POST)
    public ResponseEntity<?> imageUpload(@RequestParam("filePath") MultipartFile uploadFile) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.KOREA);
            Date currentTime = new Date();

            String fileName = format.format(currentTime) + "_"
                    + uploadFile.getOriginalFilename().substring(0, 3);
            String filePath = UPLOAD_PATH + File.separator + fileName;

            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(uploadFile.getBytes());
            stream.close();

            return new ResponseEntity<>("img/" + fileName,HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
