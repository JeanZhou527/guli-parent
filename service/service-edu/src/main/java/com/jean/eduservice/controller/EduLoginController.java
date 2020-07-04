package com.jean.eduservice.controller;

import com.jean.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
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
