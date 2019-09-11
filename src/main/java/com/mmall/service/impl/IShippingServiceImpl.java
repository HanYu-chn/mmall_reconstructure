/**
 * projectName: mmall
 * fileName: IShippingServiceImpl.java
 * packageName: com.mmall.service.impl
 * date: 2019-09-11 14:34
 * copyright(c) HanYu
 */
package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: IShippingServiceImpl
 * @packageName: com.mmall.service.impl
 * @description:
 * @data: 2019-09-11 14:34
 **/
@Service
public class IShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;
    /*---------------------------------------分割线-----------------------------------------**/

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount == 0) return ServerResponse.createByError("添加地址失败");
        Map map = new HashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createBySuccess("添加地址成功",map);
    }
    /*---------------------------------------分割线-----------------------------------------**/

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByUserIdShippingId(shipping);
        if(rowCount == 0) return ServerResponse.createByError("更新地址失败");
        return ServerResponse.createBySuccess("更新地址成功");
    }
    /*---------------------------------------分割线-----------------------------------------**/

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
        int rowCount = shippingMapper.deleteByUserIdShippingId(userId,shippingId);
        if(rowCount > 0) return ServerResponse.createBySuccess("删除地址成功");
        return ServerResponse.createByError("删除地址失败");
    }
    /*---------------------------------------分割线-----------------------------------------**/

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdShippingId(userId, shippingId);
        if(shipping == null) return ServerResponse.createByError("该地址不存在");
        return ServerResponse.createBySuccess("查找成功",shipping);
    }
    /*---------------------------------------分割线-----------------------------------------**/

    @Override
    public ServerResponse<PageInfo> getList(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}