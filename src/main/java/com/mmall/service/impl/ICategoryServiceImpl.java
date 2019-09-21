/**
 * projectName: mmall
 * fileName: ICategoryServiceImpl.java
 * packageName: com.mmall.service.impl
 * date: 2019-09-03 16:07
 * copyright(c) HanYu
 */
package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: ICategoryServiceImpl
 * @packageName: com.mmall.service.impl
 * @description:
 * @data: 2019-09-03 16:07
 **/
@Service
public class ICategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: addCategory
     * @description: 添加分类
     * @author: HanYu
     * @param categoryName
     * @param parentId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByError("添加产品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultCount = categoryMapper.insertSelective(category);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("添加产品类成功");
        }
        return ServerResponse.createByError("添加产品类失败");
    }
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
    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByError("更新产品名称参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("更新产品名称成功");
        }
        return ServerResponse.createByError("更新产品名称失败");
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getChildrenParallelCategory
     * @description: 获取平级子分类
     * @author: HanYu
     * @param categoryId 平级子分类的父分类的ID
     * @return: com.mmall.common.ServerResponse<java.util.List<com.mmall.pojo.Category>>
     * @throws:
     */
    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        if (categoryId == null) return ServerResponse.createByError("获取平级子类参数错误");
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
            logger.info("未找到该分类的子类");
            return ServerResponse.createByError("未找到该分类的子类");
        }
        return ServerResponse.createBySuccess(categories);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getChildrenParallelAndDeepCategory
     * @description: 获取其所有子分类的ID
     * @author: HanYu
     * @param categoryId  父分类的ID
     * @return: com.mmall.common.ServerResponse<java.util.List<java.lang.Integer>>
     * @throws:
     */
    @Override
    public ServerResponse<List<Integer>> getChildrenParallelAndDeepCategory(Integer categoryId) {
        if (categoryId == null) return ServerResponse.createByError("获取子类参数错误");
        Set<Category> set = new HashSet<>();
        findCategory(set,categoryId);
        if (CollectionUtils.isEmpty(set)) {
            logger.info("未找到该分类的子类");
            return ServerResponse.createByError("未找到该分类的子类");
        }
        List<Integer> list = new ArrayList<>();
        for (Category category : set) {
            list.add(category.getId());
        }
        return ServerResponse.createBySuccess(list);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: findCategory
     * @description: 递归查找所有子分类
     * @author: HanYu
     * @param set
     * @param parentId
     * @return: void
     * @throws:
     */
    private void findCategory(Set<Category> set, int parentId) {
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(parentId);
        for (Category child : categories) {
            set.add(child);
            findCategory(set,child.getId());
        }
        return;
    }
}