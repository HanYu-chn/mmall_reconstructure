/**
 * projectName: mmall
 * fileName: IOrderServiceImpl.java
 * packageName: com.mmall.service.impl
 * date: 2019-09-17 14:07
 * copyright(c) HanYu
 */
package com.mmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.*;
import com.mmall.pojo.*;
import com.mmall.service.IOrderService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.FTPUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.OrderItemVo;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;
import com.mmall.vo.ShippingVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: IOrderServiceImpl
 * @packageName: com.mmall.service.impl
 * @description:
 * @data: 2019-09-17 14:07
 **/
@Service
public class IOrderServiceImpl implements IOrderService {
    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    private static final Logger logger = LoggerFactory.getLogger(IOrderServiceImpl.class);

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: pay
     * @description: alipay支付订单
     * @author: HanYu
     * @param orderNo
     * @param userId
     * @param path
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse pay(Long orderNo, Integer userId, String path) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null) return ServerResponse.createByError("用户没有该订单");
        Map<String, String> map = new HashMap<>();
        map.put("orderNo", String.valueOf(orderNo));
        //====================================================
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(orderNo);

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("happymmall扫码支付,订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder("订单").append(outTradeNo).append(" 购买商品共").append(totalAmount).append("元").toString();


        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoAndUserId(orderNo, userId);
        for (OrderItem orderItem : orderItemList) {
            GoodsDetail goods1 = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getTotalPrice().doubleValue(), 100).longValue(), orderItem.getQuantity());
            goodsDetailList.add(goods1);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File file = new File(path);
                if (!file.exists()) {
                    file.setWritable(true);
                    file.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                File qrFile = new File(path, qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(qrFile));
                } catch (IOException e) {
                    logger.error("上传二维码异常", e);
                }
                logger.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + qrFileName;
                map.put("qrUrl", qrUrl);
                return ServerResponse.createBySuccess(map);

            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return ServerResponse.createByError("支付宝预下单失败!!!");

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByError("系统异常，预下单状态未知!!!");

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByError("不支持的交易状态，交易返回异常!!!");
        }

    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: alipayCallBack
     * @description: alipay异步回调接口
     * @author: HanYu
     * @param params
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse alipayCallBack(Map<String, String> params) {
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String aliTradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) return ServerResponse.createByError("未找到该订单号,回调忽略");
        if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) return ServerResponse.createBySuccess("支付宝重复回调");
        if (Const.AlipayCallBack.TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatus.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(aliTradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();

    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: queryOrderPayStatus
     * @description: 查询订单支付状态
     * @author: HanYu
     * @param orderNo
     * @param userId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse queryOrderPayStatus(Long orderNo, Integer userId) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null) return ServerResponse.createByError("用户没有该订单");
        if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) return ServerResponse.createBySuccess();
        return ServerResponse.createByError();
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: createOrder
     * @description: 创建订单
     * @author: HanYu
     * @param userId
     * @param shippingId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ServerResponse<List<OrderItem>> serverResponse = getCartOrderItem(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        List<OrderItem> orderItemList = serverResponse.getData();
        if (CollectionUtils.isEmpty(orderItemList)) return ServerResponse.createByError("购物车为空");
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);
        //生成订单
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (order == null) return ServerResponse.createByError("生成订单失败");
        //给订单明细设置订单号
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        //mybatis批量插入
        orderItemMapper.batchInsert(orderItemList);
        //修改库存
        this.reduceProductStoke(orderItemList);
        //清空购物车
        this.cleanCart(cartList);
        //返回给前端数据
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: cancelOrder
     * @description: 取消订单
     * @author: HanYu
     * @param userId
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse cancelOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null) return ServerResponse.createByError("用户没有该订单");
        if (order.getStatus() != Const.OrderStatus.NO_PAY.getCode()) return ServerResponse.createByError("已付款,无法取消该订单");
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatus.CANCELED.getCode());

        int row = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (row > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getOrderProductList
     * @description: 获取订单商品详情
     * @author: HanYu
     * @param userId
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse getOrderProductList(Integer userId ,Long orderNo) {
        OrderProductVo orderProductVo = new OrderProductVo();
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoAndUserId(orderNo, userId);
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return ServerResponse.createBySuccess(orderProductVo);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getDetail
     * @description: 获取订单详情
     * @author: HanYu
     * @param userId
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse getDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo,userId);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoAndUserId(orderNo,userId);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return  ServerResponse.createByError("没有找到该订单");
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getList
     * @description: 获取订单详情集合
     * @author: HanYu
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @Override
    public ServerResponse<PageInfo> getList(Integer userId,Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVo> orderVoList = assembleOrderVoList(orderList,userId);
        PageInfo pageResult = new PageInfo(orderList);
        pageResult.setList(orderVoList);
        return ServerResponse.createBySuccess(pageResult);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getListManage
     * @description: 管理员获取全部订单详情
     * @author: HanYu
     * @param pageSize
     * @param pageNum
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @Override
    public ServerResponse<PageInfo> getListManage(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAll();
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList,null);
        PageInfo pageResult = new PageInfo(orderList);
        pageResult.setList(orderVoList);
        return ServerResponse.createBySuccess(pageResult);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getDetailManage
     * @description: 获取订单详情
     * @author: HanYu
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse<OrderVo> getDetailManage(Long orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return ServerResponse.createByError("订单不存在");
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: sendGoodsManage
     * @description: 订单发货
     * @author: HanYu
     * @param orderNo
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @Override
    public ServerResponse<String> sendGoodsManage(Long orderNo) {
        Order order= orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            if(order.getStatus() == Const.OrderStatus.PAID.getCode()){
                order.setStatus(Const.OrderStatus.SHIPPED.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.createBySuccess("发货成功");
            }
            return ServerResponse.createByError("订单还未支付");
        }
        return ServerResponse.createByError("订单不存在");
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: searchManage
     * @description: TODO 搜寻订单(扩展功能待写)
     * @author: HanYu
     * @param orderNo
     * @param pageSize
     * @param pageNum
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @Override
    public ServerResponse<PageInfo> searchManage(Long orderNo, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);

            PageInfo pageResult = new PageInfo(Lists.newArrayList(order));
            pageResult.setList(Lists.newArrayList(orderVo));
            return ServerResponse.createBySuccess(pageResult);
        }
        return ServerResponse.createByError("订单不存在");
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: assembleOrderVoList
     * @description: 组装OrderList为OrderVoList
     * @author: HanYu
     * @param orderList
     * @param userId
     * @return: java.util.List<com.mmall.vo.OrderVo>
     * @throws:
     */
    private List<OrderVo> assembleOrderVoList(List<Order> orderList,Integer userId){
        List<OrderVo> orderVoList = Lists.newArrayList();
        for(Order order : orderList){
            List<OrderItem>  orderItemList = Lists.newArrayList();
            if(userId == null){
                //管理员查询的时候 不需要传userId
                orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            }else{
                orderItemList = orderItemMapper.selectByOrderNoAndUserId(order.getOrderNo(),userId);
            }
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: assembleOrderVo
     * @description: 组装orderVo,包括订单明细,和收货地址详情
     * @author: HanYu
     * @date: 2019-09-20 13:24
     * @param order
     * @param orderItemList
     * @return: com.mmall.vo.OrderVo
     * @throws:
     */
    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatus.codeOf(order.getStatus()).getStatus());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }

        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));


        orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));


        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    private ShippingVo assembleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: reduceProductStoke
     * @description: 修改库存
     * @author: HanYu
     * @param orderItemList
     * @return: void
     * @throws:
     */
    private void reduceProductStoke(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKey(product);
        }
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: cleanCart
     * @description: 清除购物车记录
     * @author: HanYu
     * @param cartList
     * @return: void
     * @throws:
     */
    private void cleanCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: assembleOrder
     * @description: 生成订单
     * @author: HanYu
     * @param userId
     * @param shippingId
     * @param payment
     * @return: com.mmall.pojo.Order
     * @throws:
     */
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatus.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.PAYMENT_ONLINE.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        int rowCount = orderMapper.insert(order);
        if (rowCount > 0) return order;
        return null;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: generateOrderNo
     * @description: 生成订单号
     * @author: HanYu
     * @param
     * @return: long
     * @throws:
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getOrderTotalPrice
     * @description: 根据订单明细集合,获取订单总价格
     * @author: HanYu
     * @param orderItemList
     * @return: java.math.BigDecimal
     * @throws:
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getCartOrderItem
     * @description: 通过购物车记录集合,获得订单明细集合,此时订单号还没赋值
     * @author: HanYu
     * @param userId
     * @param cartList
     * @return: com.mmall.common.ServerResponse<java.util.List<com.mmall.pojo.OrderItem>>
     * @throws:
     */
    private ServerResponse<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList) {
        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByError("购物车为空");
        }
        //校验购物车中的数据,产品的状态,库存
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList) {
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null || Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return ServerResponse.createByError("产品" + product.getName() + "不是售卖状态");
            }
            if (cart.getQuantity() > product.getStock()) {
                return ServerResponse.createByError("产品" + product.getName() + "库存不足");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }
}