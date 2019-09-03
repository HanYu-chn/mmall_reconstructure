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
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(String categoryName, Integer categoryId);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<Set<Category>> getChildrenParallelAndDeepCategory(Integer categoryId);
}