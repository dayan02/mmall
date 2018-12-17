package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by 大燕 on 2018/11/14.
 */

public class Const {
    //静态数

public  static  final  String CURRENT_USER  = "currentUser";

    public static  final  String EMAIL = "email";
    public static final  String USERNAME = "username";

//与前端有个约定写法,下划线前部分代表按照什么排序，后部分代表升序还是降序
    public interface ProdyctListOrderBy{
    //set的contains方法的时间复杂度是O(1),但是List的是O(n)
    Set<String> PRICE_ASC_DESC  = Sets.newHashSet("price_desc","price_asc");
}

    public interface  cart{
        int CHECKED = 1;//购物车选中状态
        int UNCHECKED = 0;//购物车未选中状态
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

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
