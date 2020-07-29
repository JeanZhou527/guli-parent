package com.jean.eduorder.controller;


import com.jean.commonutils.R;
import com.jean.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    //1、生成微信支付二维码接口
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable ("orderNo") String orderNo){
        //返回一些信息，包含二维码地址，还有其他信息
        Map map=payLogService.createNative(orderNo);

        System.out.println("*********返回二维码的集合："+map);
        return R.ok().data(map);
    }


    //2、根据订单号查询订单的支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStaus(@PathVariable  String orderNo){
        Map<String,String> map=payLogService.queryPayStatus(orderNo);
        System.out.println("***********查询订单状态的集合："+map);

        if(map==null){
            return R.error().message("支付出错了");
        }
        //如果返回的map不为空，通过map，获取到订单状态
        if(map.get("trade_state").equals("SUCCESS")){ //支付成功
            //添加支付记录到支付表中，并更新订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
         return  R.ok().code(25000).message("支付中");
    }

}

