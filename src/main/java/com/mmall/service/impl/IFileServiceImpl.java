/**
 * projectName: mmall
 * fileName: IFileServiceImpl.java
 * packageName: com.mmall.service.impl
 * date: 2019-09-06 16:09
 * copyright(c) HanYu
 */
package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: IFileServiceImpl
 * @packageName: com.mmall.service.impl
 * @description:
 * @data: 2019-09-06 16:09
 **/
@Service
public class IFileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);
    /*---------------------------------------分割线-----------------------------------------**/
    @Override
    public String uplode(MultipartFile file,String path){
        String filename = file.getOriginalFilename();
        String fileExtensionName = filename.substring(filename.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString();
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", filename, path, uploadFileName);
        File fileDirectory = new File(path);
        if (!fileDirectory.exists()) {
            fileDirectory.setWritable(true);
            fileDirectory.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常",e);
            return null;
        }
        return targetFile.getName();
    }
}