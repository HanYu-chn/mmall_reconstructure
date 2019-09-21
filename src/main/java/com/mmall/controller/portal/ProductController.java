/**
 * projectName: mmall
 * fileName: ProductController.java
 * packageName: com.mmall.controller.portal
 * date: 2019-09-08 18:21
 * copyright(c) HanYu
 */
package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: ProductController
 * @packageName: com.mmall.controller.portal
 * @description:
 * @data: 2019-09-08 18:21
 **/
@RequestMapping("/product/")
@Controller
public class ProductController {
    @Autowired
    private IProductService iProductService;
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getDetail
     * @description: 获取产品详情
     * @author: HanYu
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.ProductDetailVo>
     * @throws:
     */
    @RequestMapping(value = "getDetail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getList
     * @description: 根据查询条件获取产品集合
     * @author: HanYu
     * @date: 2019-09-19 13:49
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @RequestMapping(value = "getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.getProductList(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}