package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetaiVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 大燕 on 2018/12/13.
 */

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    //前端商品信息查询
    public ServerResponse<ProductDetaiVo> detail(Integer productId){
//查询商品信息跟后台查询一致，不过前台查询的话需要先判断该商品是否在线，住service里面进行业务实现
        return iProductService.getProductDetail(productId);

    }

//前端用户搜索

//public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword,
//                                     @RequestParam(value = "categoryId",required = false)Integer categoryId,
//                                     @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
//                                     @RequestParam(value = "pageSize",defaultValue = "10")int pageSize
//                                     ){
//
//}
}
