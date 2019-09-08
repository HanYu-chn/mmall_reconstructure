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
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageGetProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo> getProductListByKeyword(String keyword,Integer categoryId,Integer pageNum, Integer pageSize,String orderBy);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
}