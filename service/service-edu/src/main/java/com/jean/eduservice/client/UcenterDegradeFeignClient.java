package com.jean.eduservice.client;

import com.jean.commonutils.R;
import com.jean.eduservice.entity.UcenterMember;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Component
public class UcenterDegradeFeignClient implements UcenterClient{

    @Override
    public UcenterMember getInfo(String memberId) {
        System.out.println(R.error().message("没有调到Ucenter微服务，memberId获取用户信息失败"));
//        return R.error().message("没有调到Ucenter微服务，memberId获取用户信息失败");
//        return null;
        return new UcenterMember();
    }

//    @Override
//    public R getUcenterMemberInfo(HttpServletRequest request) {
//        System.out.println(R.error().message("没有调到Ucenter微服务，获取用户信息失败"));
//        return R.error().message("没有调到Ucenter微服务，获取用户信息失败");
//    }
}
