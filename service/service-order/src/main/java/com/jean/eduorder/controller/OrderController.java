package com.jean.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.internal.$Gson$Preconditions;
import com.jean.commonutils.JwtUtils;
import com.jean.commonutils.R;
import com.jean.eduorder.entity.Order;
import com.jean.eduorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    //1、生成订单的方法 ，根据课程id生成订单
    @PostMapping("/createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId=JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {

//            System.out.println(R.error().getCode());
//            System.out.println(R.error().message("请登录"));

            return R.error().data("message","请登录");
        }
        //创建订单，并返回订单号
        String orderNo=orderService.createOrders(courseId,memberId);
        return  R.ok().data("orderId",orderNo);
    }


    //2、根据订单id（订单号），查询订单信息
    @GetMapping("/getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<Order>();
        wrapper.eq("order_no",orderId);   //是订单号
        Order order = orderService.getOne(wrapper);
        return  R.ok().data("items",order);
    }

    //3、根据课程id和用户id，查询订单表中订单状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId,@PathVariable("memberId") String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<Order>();
        wrapper.eq("course_id",courseId);   //是课程id
        wrapper.eq("member_id",memberId);   //是用户id
        wrapper.eq("status",1);   //订单状态 1表示已支付
        int count = orderService.count(wrapper);
        if(count>0){ //如果>0 表示已经支付
            return true;
        }else {
            return false;
        }

    }



}

