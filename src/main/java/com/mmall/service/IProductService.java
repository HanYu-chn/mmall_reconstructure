/**
 * projectName: mmall
 * fileName: IProductService.java
 * packageName: com.mmall.service
 * date: 2019-09-05 13:43
 * copyright(c) HanYu
 */
package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: IProductService
 * @packageName: com.mmall.service
 * @description:
 * @data: 2019-09-05 13:43
 **/
public interface IProductService {
    /**
     * @title: getProductDetail
     * @description: 获取产品详情,包装为ProductDetailVo
     * @author: HanYu
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.ProductDetailVo>
     * @throws:
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
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
    ServerResponse<PageInfo> getProductList(String keyword,Integer categoryId,Integer pageNum, Integer pageSize,String orderBy);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: saveOrUpdateProduct
     * @description: 新增或更新产品
     * @author: HanYu
     * @param product
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse saveOrUpdateProduct(Product product);
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
    ServerResponse setSaleStatus(Integer productId, Integer status);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: manageGetProductDetail
     * @description: 后台获取产品详情,包装为ProductDetailVo
     * @author: HanYu
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.ProductDetailVo>
     * @throws:
     */
    ServerResponse<ProductDetailVo> manageGetProductDetail(Integer productId);
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
    ServerResponse<PageInfo> manageGetProductList(Integer pageNum, Integer pageSize);
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
    ServerResponse<PageInfo> manageSearchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);


}