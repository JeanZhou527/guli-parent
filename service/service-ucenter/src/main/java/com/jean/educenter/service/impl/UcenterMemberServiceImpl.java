package com.jean.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jean.commonutils.JwtUtils;
import com.jean.commonutils.MD5;
import com.jean.educenter.entity.UcenterMember;
import com.jean.educenter.entity.vo.RegisterVo;
import com.jean.educenter.mapper.UcenterMemberMapper;
import com.jean.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-19
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    //用户登录
    @Override
    public String login(UcenterMember member) {
        //获取登录的手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();
        //手机号和密码的非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"登录失败，手机号或密码为空");
        }

        //判断手机号是否正确，去数据库查询下，如果能查询得到数据，说明手机号是存在的
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        if (mobileMember==null){ //没有这个手机号
            throw new GuliException(20001,"登录失败，手机号不存在");
        }
        //判断密码是否正确
        //一般存储到数据库中的密码都是加密的
        //把页面输入的密码进行加密，再和数据库中的密码进行比较
        //加密方式 MD5：只能加密，不能解密  用MD5工具类
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())){
            throw new GuliException(20001,"登录失败，密码不正确");
        }

        //判断这个用户是否被禁用    is_disabled  1:禁用   0：没有被禁用
        if(mobileMember.getIsDisabled()){
            throw new GuliException(20001,"登录失败，你被禁用了，请联系管理员");
        }

        //登录成功
        //生成一个token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    //用户注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();

        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)||StringUtils.isEmpty(nickname)||StringUtils.isEmpty(code)){
            throw new GuliException(20001,"注册失败，数据为空");
        }

        //判断验证码是否正确
        //获取redis中的验证码code
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"注册失败，验证码不对");
        }

        //判断手机是否重复，如果表里面存在了相同的手机号，就不进行添加，注册失败
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"注册失败，手机号已存在");
        }

        //把注册的数据添加到数据库
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setNickname(nickname);
        member.setIsDisabled(false); //用户不禁用
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/MQ7qUmCprK9am16M1Ia1Cs3RK0qiarRrl9y8gsssBjIZeS2GwKSrnq7ZYhmrzuzDwBxSMMAofrXeLic9IBlW4M3Q/132");
        baseMapper.insert(member);
    }

    //根据openid，进行查询
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    // 查询某一天的注册人数
    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegisterDay(day);
    }


}
