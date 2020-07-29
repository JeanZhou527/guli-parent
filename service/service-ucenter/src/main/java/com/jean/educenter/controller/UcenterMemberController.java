package com.jean.educenter.controller;


import com.jean.commonutils.JwtUtils;
import com.jean.commonutils.R;
import com.jean.commonutils.ordervo.UcenterMemberOrder;
import com.jean.educenter.entity.UcenterMember;
import com.jean.educenter.entity.vo.RegisterVo;
import com.jean.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
@RestController
//@CrossOrigin
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //用户登录
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember member){
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token=memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册用户
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo)
    {
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token，获取用户信息
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request)
    {   //调用jwt工具类中的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        //根据用户id，查询数据库，获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据token字符串中的memberId获取用户信息
    @PostMapping("/getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = memberService.getById(id);
        UcenterMember memeber = new UcenterMember();
        BeanUtils.copyProperties(ucenterMember,memeber);
//        return R.ok().data("memberInfo",memeber);
        return memeber;
    }

    //5、根据用户id，获取用户信息
    @PostMapping("/getUserInfo/{id}")
    public UcenterMemberOrder getUserInfo(@PathVariable String id) {

        //根据用户id获取用户信息
        UcenterMember ucenterMember = memberService.getById(id);
        UcenterMemberOrder memeber = new UcenterMemberOrder();
        //把UcenterMember赋值给 UcenterMemberOrder
        BeanUtils.copyProperties(ucenterMember,memeber);
        return memeber;
    }


    // 查询某一天的注册人数
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day)
    {
        Integer count=memberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }



}

