package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 大燕 on 2018/11/26.
 */

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iuserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加等级category
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId" ,defaultValue = "0") Integer parentId){


    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
    }
        if (iuserService.checkUserRole(user).isSuccess()){
            //是管理员
            //增加处理分类的逻辑
return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return  ServerResponse.createByErrorMessage("无权限处理，需管理员权限");
        }

}

    /**
     * 更新categoryName的接口
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody

    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
           return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iuserService.checkUserRole(user).isSuccess()){
//更新操作
return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMessage("无权限处理，需管理员权限");
        }
    }

    /**
     * 获取当前category等级的子等级及其平行等级
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public  ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iuserService.checkUserRole(user).isSuccess()) {
            //当前categoryId下边结点的等category信息，即子结点及子节点平级结点信息,不递归
            //如果等级结点categoryId不传入，默认是0,根节点
           return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限处理，需管理员权限");
        }
    }


    /**
     * 获取当前categoryId并且及其子节点的id，递归查询出所有的
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iuserService.checkUserRole(user).isSuccess()) {
            //查询当前节点id及其递归子节点id
return iCategoryService.getChildrenParallelCategory(categoryId);

        } else {
            return ServerResponse.createByErrorMessage("无权限处理，需管理员权限");
        }
    }
}
