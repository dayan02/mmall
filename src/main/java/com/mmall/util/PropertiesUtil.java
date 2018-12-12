package com.mmall.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by 大燕 on 2018/12/4.
 */

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

private static Properties props;

    static {
        String url = "mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(url),"utf-8"));
            logger.info("加载配置文件成功");
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }

    //通过properties文件的key读取value
    public static  String getProperty(String key){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
return null;
        }
        return value.trim();
    }

    public static  String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
}
