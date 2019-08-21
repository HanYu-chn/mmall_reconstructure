/**
 * projectName: mmall
 * fileName: IUserService.java
 * packageName: com.mmall.service
 * date: 2019-08-18 15:45
 * copyright(c) HanYu
 */
package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: IUserService
 * @packageName: com.mmall.service
 * @description: 用户服务接口
 * @data: 2019-08-18 15:45
 **/
public interface IUserService {
    ServerResponse<User> login(String username, String passowrd);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String value, String type);
}

