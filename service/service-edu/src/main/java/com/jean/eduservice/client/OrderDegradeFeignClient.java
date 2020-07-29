package com.jean.eduservice.client;

import com.jean.commonutils.R;

import org.springframework.stereotype.Component;

@Component
public class OrderDegradeFeignClient implements OrderClient{
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        System.out.println(R.error().message("没有调到Order微服务，查询课程信息失败"));
//        return R.error().message("没有调到Ucenter微服务，memberId获取用户信息失败");
//        return null;
//        throw new GuliException(20001,"调用order微服务时，出现错误");
        return false;
    }
}
