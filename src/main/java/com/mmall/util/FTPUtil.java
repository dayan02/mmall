package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by 大燕 on 2018/12/6.
 */

public class FTPUtil {

    private static  final  Logger logger = LoggerFactory.getLogger(FTPUtil.class);

//获取ftp用户密码配置，在mmall.properties里面
   private static  String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static  String ftpIUser = PropertiesUtil.getProperty("ftp.user");
    private static  String ftpIPsw = PropertiesUtil.getProperty("ftp.pass");
    private String ip;
    private int port;
    private String user;
    private String psw;
    private FTPClient ftpClient;

    public FTPUtil(String ip,int port,String user,String psw){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.psw = psw;
    }

//开放出去的静态方法
    public static boolean upLoadFile(List<File> fileList) throws IOException{
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpIUser,ftpIPsw);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.upLoadFile("img",fileList);
        logger.info("开始连接ftp服务器，结束连接，上传结果:{}");
        return result;
    }

private  boolean upLoadFile(String remotePath,List<File> fileList) throws IOException {
    boolean upLoad = true;
    FileInputStream files = null ;
    //连接ftp服务器
   if (connectServer(this.ip,this.port,this.user,this.psw)){
       try {
           //更换目录
           ftpClient.changeWorkingDirectory(remotePath);
           ftpClient.setBufferSize(1024);
           ftpClient.setControlEncoding("utf-8");
           ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//文件类型二进制，防止乱码问题
           ftpClient.enterLocalPassiveMode();//打开本地的被动模式
           for (File fileItem: fileList) {
               files = new FileInputStream(fileItem);
               //通过InputStream流调用ftpClient进行文件的存储
               ftpClient.storeFile(fileItem.getName(),files);
           }
       } catch (IOException e) {
           logger.error("上传文件异常",e);
           upLoad = false;
           e.printStackTrace();
       }finally {
           //释放连接
           //流关闭
           files.close();
           ftpClient.disconnect();
       }
   }
    return upLoad;
}

    //判断ftp服务器的连接
    private  boolean connectServer(String ip,int port,String user,String psw){
       boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            //连接
            ftpClient.connect(ip);
            //登录
          isSuccess  =  ftpClient.login(ftpIUser,ftpIPsw);
            //确认登录成功才进行文件上传
        } catch (IOException e) {
            logger.error("连接ftp服务器异常",e);
            e.printStackTrace();
        }
        return isSuccess;
    }



    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


}
