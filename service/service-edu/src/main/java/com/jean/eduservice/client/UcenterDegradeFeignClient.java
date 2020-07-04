package com.jean.eduservice.client;

import com.jean.commonutils.R;
import org.springframework.stereotype.Component;

@Component
public class UcenterDegradeFeignClient implements UcenterClient{

    @Override
    public R getUcenterMemberInfo(String memberId) {
        System.out.println(R.error().message("没有调到Ucenter微服务，获取用户信息失败"));
        return R.error().message("没有调到Ucenter微服务，获取用户信息失败");
    }
}
