package com.jean.eduorder.service;

import com.jean.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
public interface OrderService extends IService<Order> {

    //创建订单，并返回订单号
    String createOrders(String courseId, String memberIdByJwtToken);
}
