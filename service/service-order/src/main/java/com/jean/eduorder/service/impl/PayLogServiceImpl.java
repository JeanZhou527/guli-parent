package com.jean.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.jean.eduorder.entity.Order;
import com.jean.eduorder.entity.PayLog;
import com.jean.eduorder.mapper.PayLogMapper;
import com.jean.eduorder.service.OrderService;
import com.jean.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jean.eduorder.utils.HttpClient;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //生成微信支付二维码接口
    @Override
    public Map createNative(String orderNo) {
        try {
            //1、根据订单号获取订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);

            Map m = new HashMap();
            //2、设置生成二维码需要的一些支付参数
            m.put("appid", "wx74862e0dfcf69954");//微信id
            m.put("mch_id", "1558950191");//商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//生成一个随机的字符串
            m.put("body", order.getCourseTitle());//课程标题或名称
            m.put("out_trade_no", orderNo);//订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");//订单价格
            m.put("spbill_create_ip", "127.0.0.1");//需要支付的IP地址
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");//支付完的一个回调地址
            m.put("trade_type", "NATIVE");//支付的类型，现在是根据价格生成二维码

            //3、发送httpclient请求，传递xml格式的参数，请求地址是微信支付提供的固定地址
            //3、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置xml格式的参数    根据商户key对m进行加密，再转成xml格式的数据
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true); //支持htttps请求
            //执行请求发送
            client.post();

            //4、得到发送请求返回的结果，返回第三方的数据
            String xml = client.getContent();
            //把xml格式数据转换成map集合，把map集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //5、封装返回结果集
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));//返回二维码的操作码
            map.put("code_url", resultMap.get("code_url")); //二维码的地址

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);

            return map;

        } catch (Exception e) {
            throw new GuliException(20001,"生成二维码失败");
//            e.printStackTrace();
//            return new HashMap<>();
        }
    }

    //根据订单号，查询订单的支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");//微信id
            m.put("mch_id", "1558950191");//商户id
            m.put("out_trade_no", orderNo);//订单号
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置发送请求  发送httpclient
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、得到请求返回的内容，并转成map
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //4、再返回map
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //向支付表中添加支付记录，同是更新订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");//得到订单号
        //根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);
        // 1、更新订单表order中的订单状态
        if(order.getStatus().intValue() == 1) return;
        order.setStatus(1); // 1 代表已支付
        orderService.updateById(order);

        //向支付表中添加支付记录
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());//支付完成时间
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));//订单流水号
        payLog.setAttr(JSONObject.toJSONString(map)); //其他数据转成json进行保存
        baseMapper.insert(payLog);//插入到支付表
    }


}
