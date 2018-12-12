package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by 大燕 on 2018/12/6.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {


private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //获取扩展名 abc.jpg |abc.a.cv.jpg
        String fileExetensionFileName = fileName.substring(fileName.lastIndexOf(".")+1);
//上传文件名称,使用UUID不会重复
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExetensionFileName;
logger.info("开始上传文件，上传文件名:{},上传的路径:{},新的文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
            }
        File targetFile = new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);
        //文件以及上传成功，以及传到upload文件夹下
            logger.info("文件上传成功");
            //  将文件上传到ftp服务器上
            logger.info("文件开始上传到ftp服务器");
            FTPUtil.upLoadFile(Lists.newArrayList(targetFile));

            // 上传成功后，删除上传到upload文件夹下的图片
            targetFile.delete();
            logger.info("文件上传事件结束");

        } catch (IOException e) {
//           logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();

    }

//    public static void main(String[] args) {
//        String str = "avm.jpg";
//        System.out.println(str.substring(str.lastIndexOf(".")+1));
//    }
}
