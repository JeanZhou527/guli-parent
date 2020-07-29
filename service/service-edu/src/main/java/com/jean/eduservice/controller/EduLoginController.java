package com.jean.eduservice.controller;

import com.jean.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
//如果开启了gateway网关服务并做了统一跨域处理，可以不要加上这个注解@CrossOrigin
//@CrossOrigin
@RequestMapping("/eduservice/user")
public class EduLoginController {

    //login
    @PostMapping("/login")
    public R login()
    {
        return R.ok().data("token","admin");
    }
    //info
    @GetMapping("/info")
    public R info()
    {
        return R.ok().data("roles","[admin]").data("name","admin").data("avadar","shanghai.aliyuncs.com/2020/05/22/d4e5b206fae446c29cf6c4aa76deaa0efile.png");
    }
}
