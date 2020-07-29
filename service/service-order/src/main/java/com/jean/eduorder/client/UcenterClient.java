package com.jean.eduorder.client;

import com.jean.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
//@FeignClient(value="service-ucenter",fallback = UcenterDegradeFeignClient.class)
@FeignClient(value="service-ucenter")
//@FeignClient(value="service-ucenter")
public interface UcenterClient {

    //5、根据用户memberId，获取用户信息
    @PostMapping("/educenter/member/getUserInfo/{id}")
    public UcenterMemberOrder getUserInfo(@PathVariable("id") String id);

}
