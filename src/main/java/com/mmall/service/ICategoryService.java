/**
 * projectName: mmall
 * fileName: ICategoryService.java
 * packageName: com.mmall.service.impl
 * date: 2019-09-03 16:06
 * copyright(c) HanYu
 */
package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: ICategoryService
 * @packageName: com.mmall.service.impl
 * @description:
 * @data: 2019-09-03 16:06
 **/
public interface ICategoryService {
    /**
     * @title: addCategory
     * @description: 添加分类
     * @author: HanYu
     * @param categoryName
     * @param parentId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse addCategory(String categoryName, Integer parentId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: updateCategoryName
     * @description: 更新分类名称
     * @author: HanYu
     * @param categoryName
     * @param categoryId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse updateCategoryName(String categoryName, Integer categoryId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getChildrenParallelCategory
     * @description: 获取平级子分类
     * @author: HanYu
     * @param categoryId 平级子分类的父分类的ID
     * @return: com.mmall.common.ServerResponse<java.util.List<com.mmall.pojo.Category>>
     * @throws:
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getChildrenParallelAndDeepCategory
     * @description: 获取其所有子分类的ID
     * @author: HanYu
     * @date: 2019-09-19 13:25
     * @param categoryId  父分类的ID
     * @return: com.mmall.common.ServerResponse<java.util.List<java.lang.Integer>>
     * @throws:
     */
    ServerResponse<List<Integer>> getChildrenParallelAndDeepCategory(Integer categoryId);
}