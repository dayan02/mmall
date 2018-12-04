package com.mmall.service.Impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetaiVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 大燕 on 2018/12/3.
 */
@Service("iProductService")
public class ProductService implements IProductService {

@Autowired
    private ProductMapper productMapper;

@Autowired
    private CategoryMapper categoryMapper;
public ServerResponse savaOrUpdateProduct(Product product){
    if (product != null){
    //子图不为空，就把子图第一个作为主图显示
        if (StringUtils.isNotBlank(product.getSubImages())){
            String[]  subImageArray  = product.getSubImages().split(".");
        if (subImageArray.length>0){
            product.setSubImages(subImageArray[0]);
        }
    }
        if (product.getId()!=null){
           int resultRow =  productMapper.updateByPrimaryKey(product);
            if (resultRow>0){
                return ServerResponse.createBySuccessMessage("更新产品成功");
            }
            return ServerResponse.createByErrorMessage("更新产品失败");
        }else{
            int resultRow =  productMapper.insert(product);
            if (resultRow>0){
                return ServerResponse.createBySuccessMessage("新增产品成功");
            }
            return ServerResponse.createByErrorMessage("新增产品失败");
        }
}else {
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }
}

public ServerResponse<String> setSaleSatus(Integer productId,Integer status){
    if (productId == null||status == null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }
    Product product = new Product();
    product.setId(productId);
    product.setStatus(status);
    int resultRow = productMapper.updateByPrimaryKeySelective(product);
    if (resultRow>0){
        return ServerResponse.createBySuccessMessage("修改商品状态成功");
    }
    return  ServerResponse.createByErrorMessage("修改商品状态失败");
}

//获取产品信息
    public ServerResponse<ProductDetaiVo> manageProductDetail(Integer productId){
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product =  productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }

      ProductDetaiVo productDetaiVo = this.assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetaiVo);

    }


    private ProductDetaiVo assembleProductDetailVo(Product product){
ProductDetaiVo productDetaiVo = new ProductDetaiVo();
        productDetaiVo.setId(product.getId());
        productDetaiVo.setName(product.getName());
        productDetaiVo.setStatus(product.getStatus());
        productDetaiVo.setCategoryId(product.getCategoryId());
        productDetaiVo.setDetail(product.getDetail());
        productDetaiVo.setStock(product.getStock());
        productDetaiVo.setPrice(product.getPrice());
        productDetaiVo.setSubTitle(product.getSubtitle());
        productDetaiVo.setMainImage(product.getMainImage());
        productDetaiVo.setSubImage(product.getSubImages());

        //imageHost、pareentCategoryId、createTime、updateTime
        //从配置文件获取
        productDetaiVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
       //查找或设置父类别结点
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            productDetaiVo.setParentCategoryId(0);
        }else{
           productDetaiVo.setParentCategoryId(category.getParentId());
        }

        productDetaiVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetaiVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetaiVo;
}
}
