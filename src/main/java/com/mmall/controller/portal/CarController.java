package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 大燕 on 2018/12/17.
 */

@Controller
@RequestMapping("/car/")
public class CarController {

    @Autowired
    private ICarService iCarService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session,Integer count ,Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.add(user.getId(),productId,count);
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session,Integer count ,Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.update(user.getId(),productId,count);
    }
}
