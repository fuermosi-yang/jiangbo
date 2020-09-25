package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    @Autowired
    private FastFileStorageClient storageClient;


    public String upload(MultipartFile file)   {

        String originalFilename = file.getOriginalFilename();

        String newFileName = System.currentTimeMillis()+originalFilename.substring(originalFilename.lastIndexOf("."));


//        file.transferTo(new File("d:\\image\\" + newFileName));
        String ext = StringUtils.substringAfterLast(newFileName, ".");


        StorePath   storePath = null;
        try {
            storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 生成url地址，返回
        return "http://image.leyou.com/" + storePath.getFullPath();


    }
}