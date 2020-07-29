package com.jean.educenter.service;

import com.jean.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jean.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录
    String login(UcenterMember member);

    //注册
    void register(RegisterVo registerVo);

    //根据openid查询
    UcenterMember getOpenIdMember(String openid);

    //查询某一天的注册人数
    Integer countRegister(String day);
}
