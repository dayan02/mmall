package com.mmall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
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
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 大燕 on 2018/12/3.
 */
@Service("iProductService")
public class ProductService implements IProductService {

@Autowired
    private ProductMapper productMapper;

@Autowired
    private CategoryMapper categoryMapper;

    //保存或新增商品

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

    //更改商品信息状态
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


    //分页处理列表查询
    public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
        //1、设置初始页；2、sql语句处理；3、pagehelper收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem: productList) {
            ProductListVo productListVo = this.assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }


    private ProductListVo assembleProductListVo(Product product){
ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setStatus(product.getStatus());
        productListVo.setName(product.getName());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
   return productListVo;
    }



    //商品查询
    public ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){

        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
}
        List<Product> productList = productMapper.selectProdutByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem:productList) {
           ProductListVo productListVo = this.assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productList);
        return ServerResponse.createBySuccess(pageResult);
}

//前台商品信息查询
    public ServerResponse<ProductDetaiVo> getProductDetail(Integer productId){

        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product =  productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
    return ServerResponse.createByErrorMessage("产品已下架或者删除");
}
        ProductDetaiVo productDetaiVo = this.assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetaiVo);

    }


//    前端商品查询(分页)
//    public ServerResponse<PageInfo>
}
