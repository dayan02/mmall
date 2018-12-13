package com.mmall.common;

/**
 * Created by 大燕 on 2018/11/14.
 */

public class Const {

public  static  final  String CURRENT_USER  = "currentUser";

public static  final  String EMAIL = "email";
    public static final  String USERNAME = "username";



    public  interface Role{
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN  = 1; //管理员
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在线");

        private  int code;
        private  String value;


         ProductStatusEnum(int code,String value){
          this.code = code;
            this.value = value;
        }
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
