package com.jean.msmservice.controller;

import com.jean.commonutils.R;
import com.jean.msmservice.service.MsmService;
import com.jean.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信的方法
    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone){
        //1 从redis中获取验证码，如果获取得到验证码，就直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }

        //2 如果从redis中获取不到验证码，才进行阿里云发送验证码
        //生成随机的一个值，传递给阿里云进行发送
         code= RandomUtil.getFourBitRandom();
        //把这个随机数传给阿里云
        Map<String,Object> param=new HashMap<>();
        param.put("code",code);
        //调用service中的方法发送短信
        boolean isSend=msmService.send(param,phone);
        if(isSend){
            //如果发送成功，再把验证码放到redis中去
            //并设置验证码有效时间  验证码5分钟有效
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信发送失败controller");
        }
    }

}