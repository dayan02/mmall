package com.mmall.common;

/**枚举
 * 状态以及信息显示
 * Created by 大燕 on 2018/11/14.
 */

public enum ResponseCode {
    SUCCESS(0,"success"),
    ERROR(1,"error"),
    NEED_LOGIN(10,"need login"),
    ILLEGAL_ARGUMENT(2,"illegal_argument");

    private  final int code;
    private  final String desc;

    ResponseCode (int code,String desc){
        this.code = code;
        this.desc = desc;
    }


    public int getCode(){

        return  code;
    }
public  String getDesc(){
    return  desc;
}
}
