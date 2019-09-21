/**
 * projectName: mmall
 * fileName: IProductServiceImpl.java
 * packageName: com.mmall.service.impl
 * date: 2019-09-05 13:43
 * copyright(c) HanYu
 */
package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductGeneralVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: IProductServiceImpl
 * @packageName: com.mmall.service.impl
 * @description:
 * @data: 2019-09-05 13:43
 **/
@Service
public class IProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: saveOrUpdateProduct
     * @description: 新增或更新产品
     * @author: HanYu
     * @param product
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] images = product.getSubImages().split(",");
                if (images.length > 0) {
                    product.setMainImage(images[0]);
                }
            }
            if (product.getId() != null) {
                int resultCount = productMapper.updateByPrimaryKey(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                } else {
                    return ServerResponse.createByError("更新产品失败");
                }
            } else {
                int resultCount = productMapper.insert(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                } else {
                    return ServerResponse.createByError("新增产品失败");
                }
            }
        }
        return ServerResponse.createByError("新增或更新产品参数不正确");
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: setSaleStatus
     * @description: 设置产品上下架状态
     * @author: HanYu
     * @param productId
     * @param status
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改产品状态成功");
        } else {
            return ServerResponse.createByError("修改产品状态失败");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: manageGetProductDetail
     * @description: 后台获取产品详情,包装为ProductDetailVo
     * @author: HanYu
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.ProductDetailVo>
     * @throws:
     */
    @Override
    public ServerResponse<ProductDetailVo> manageGetProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByError("产品不存在");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: manageGetProductList
     * @description: 后台获取产品集合
     * @author: HanYu
     * @param pageNum
     * @param pageSize
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @Override
    public ServerResponse<PageInfo> manageGetProductList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectList();
        List<ProductGeneralVo> productGeneralVoList = new ArrayList<>();
        for (Product product : products) {
            ProductGeneralVo productGeneralVo = assembleProductGeneralVo(product);
            productGeneralVoList.add(productGeneralVo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productGeneralVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @title: getProductList
     * @description: 根据查询条件获取产品集合
     * @author: HanYu
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @Override
    public ServerResponse<PageInfo> getProductList(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //返回空结果集
                List<ProductGeneralVo> productGeneralVoList = new ArrayList<>();
                PageHelper.startPage(pageNum, pageSize);
                PageInfo pageInfo = new PageInfo(productGeneralVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getChildrenParallelAndDeepCategory(categoryId).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        List<Product> products = productMapper.selectListByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword,
                categoryIdList.size() == 0 ? null : categoryIdList);
        List<ProductGeneralVo> productGeneralVoList = new ArrayList<>();
        for (Product product : products) {
            productGeneralVoList.add(assembleProductGeneralVo(product));
        }
        //排序处理
        PageHelper.startPage(pageNum, pageSize);
        if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            String[] strings = orderBy.split("_");
            PageHelper.orderBy(strings[0] + " " + strings[1]);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productGeneralVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: manageSearchProduct
     * @description: 产品搜索
     * @author: HanYu
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @Override
    public ServerResponse<PageInfo> manageSearchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.selectListByNameAndId(productName, productId);
        List<ProductGeneralVo> productGeneralVoList = new ArrayList<>();
        for (Product product : products) {
            ProductGeneralVo productGeneralVo = assembleProductGeneralVo(product);
            productGeneralVoList.add(productGeneralVo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productGeneralVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param productId
     * @title: getProductDetail
     * @description: 获取产品详情, 包装为ProductDetailVo
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.ProductDetailVo>
     * @throws:
     */
    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByError("产品已下架或不存在");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByError("产品已下架或不存在");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param product
     * @title: assembleProductDetailVo
     * @description: 组装ProductVo
     * @author: HanYu
     * @return: com.mmall.vo.ProductDetailVo
     * @throws:
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        //parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        //createTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        //updateTime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param product
     * @title: assembleProductGeneralVo
     * @description: 组装ProductGeneralVo
     * @author: HanYu
     * @return: com.mmall.vo.ProductGeneralVo
     * @throws:
     */
    private ProductGeneralVo assembleProductGeneralVo(Product product) {
        ProductGeneralVo productListVo = new ProductGeneralVo();
        productListVo.setId(product.getId());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        //imageHost
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return productListVo;
    }
}