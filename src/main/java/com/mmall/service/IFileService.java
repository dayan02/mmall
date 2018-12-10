package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 大燕 on 2018/12/6.
 */
public interface IFileService {
    //文件上传
    String upload(MultipartFile file, String path);
}
