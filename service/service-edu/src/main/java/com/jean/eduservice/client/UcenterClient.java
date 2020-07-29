package com.jean.eduservice.client;


import com.jean.eduservice.entity.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(value="service-ucenter",fallback = UcenterDegradeFeignClient.class)
//@FeignClient(value="service-ucenter")
public interface UcenterClient {

    //根据memberId获取用户信息
    @PostMapping("/educenter/member/getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable("id") String id);

//    @GetMapping("/educenter/member/getMemberInfo")
//   public R getUcenterMemberInfo(HttpServletRequest request);


}
