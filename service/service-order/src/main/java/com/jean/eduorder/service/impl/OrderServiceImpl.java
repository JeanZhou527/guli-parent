package com.jean.eduorder.service.impl;

import com.jean.commonutils.R;
import com.jean.commonutils.ordervo.CourseWebVoOrder;
import com.jean.commonutils.ordervo.UcenterMemberOrder;
import com.jean.eduorder.client.EduClient;
import com.jean.eduorder.client.UcenterClient;
import com.jean.eduorder.entity.Order;
import com.jean.eduorder.mapper.OrderMapper;
import com.jean.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jean.eduorder.utils.OrderNoUtil;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduClient eduClient;

    //生成订单，并返回订单号
    @Override
    public String createOrders(String courseId, String memberId) {
        //通过远程调用获取到课程信息（根据课程id）
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //通过远程调用获取用户信息（根据用户id）
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfo(memberId);

        //创建Order对象，向Order对象里面设置所需要的数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo()); //订单号

        order.setCourseId(courseId);//课程id
        order.setCourseTitle(courseInfoOrder.getTitle());//课程标题
        order.setCourseCover(courseInfoOrder.getCover());//课程封面
        order.setTeacherName(courseInfoOrder.getTeacherName());//课程的讲师
        order.setTotalFee(courseInfoOrder.getPrice());//课程价格

        order.setMemberId(memberId);//会员id
        order.setMobile(userInfoOrder.getMobile());//会员的手机号
        order.setNickname(userInfoOrder.getNickname());//会员呢称

        order.setStatus(0); //支付状态 0 代表 未支付  1代表已支付
        order.setPayType(1); // 支付方式，1代表微信 2代表支付宝

        baseMapper.insert(order);
        //返回订单号
        return order.getOrderNo();
    }
}
