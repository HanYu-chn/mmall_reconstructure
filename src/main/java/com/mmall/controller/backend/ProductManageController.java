/**
 * projectName: mmall
 * fileName: ProductManageController.java
 * packageName: com.mmall.controller.backend
 * date: 2019-09-05 12:57
 * copyright(c) HanYu
 */
package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: ProductManageController
 * @packageName: com.mmall.controller.backend
 * @description: 产品后台管理
 * @data: 2019-09-05 12:57
 **/
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: productSave
     * @description: 新增或更新产品
     * @author: HanYu
     * @param session
     * @param product
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "productSave.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: setSaleStatus
     * @description: 后台设置产品上下架状态
     * @author: HanYu
     * @param session
     * @param productId
     * @param productStatus
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "setSaleStatus.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer productStatus) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, productStatus);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getDetail
     * @description: 后台获取产品详情,包装为ProductDetailVo
     * @author: HanYu
     * @param session
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.ProductDetailVo>
     * @throws:
     */
    @RequestMapping(value = "getDetail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.manageGetProductDetail(productId);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getList
     * @description: 后台获取产品集合
     * @author: HanYu
     * @param session
     * @param pageNum
     * @param pageSize
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @RequestMapping(value = "getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.manageGetProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: productSearch
     * @description: 产品搜索
     * @author: HanYu
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @RequestMapping(value = "productSearch.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> productSearch(HttpSession session, String productName, Integer productId,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.manageSearchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: uplodeFile
     * @description: 图片上传
     * @author: HanYu
     * @param session
     * @param file
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "uplodeFile.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse uplodeFile(HttpSession session, @RequestParam("uploadFile") MultipartFile file) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = session.getServletContext().getRealPath("upload");
            String fileName = iFileService.uplode(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName;
            Map map = Maps.newHashMap();
            map.put("fileName", fileName);
            map.put("url", url);
            return ServerResponse.createBySuccess(map);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: richTextImgUpload
     * @description: 富文本上传图片
     * @author: HanYu
     * @param session
     * @param file
     * @param response
     * @return: java.util.Map
     * @throws:
     */
    @RequestMapping(value = "richTextImgUpload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richTextImgUpload(HttpSession session,@RequestParam("uploadFile") MultipartFile file,HttpServletResponse response) {
        Map resultMap = new HashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = session.getServletContext().getRealPath("upload");
            String fileName = iFileService.uplode(file, path);
            if (StringUtils.isBlank(fileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传文件失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传文件成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作,需要管理员权限");
            return resultMap;
        }
    }
}