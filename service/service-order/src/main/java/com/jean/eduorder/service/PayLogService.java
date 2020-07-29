package com.jean.eduorder.service;

import com.jean.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
public interface PayLogService extends IService<PayLog> {

    //生成微信支付二维码接口
    Map createNative(String orderNo);

    //根据订单号，查询订单的支付状态
    Map<String, String> queryPayStatus(String orderNo);

    //向支付表中添加支付记录，同是更新订单状态
    void updateOrderStatus(Map<String, String> map);
}
