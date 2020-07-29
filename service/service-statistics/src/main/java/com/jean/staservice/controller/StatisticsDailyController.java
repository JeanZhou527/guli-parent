package com.jean.staservice.controller;


import com.jean.commonutils.R;
import com.jean.staservice.client.UcenterClient;
import com.jean.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-10
 */
@RestController
@RequestMapping("/staservice/sta")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService staService;

    //统计某一天的注册人数,生成注册数据，并写入到数据库中
    @PostMapping("/registerCount/{day}")
    public R registerCount(@PathVariable String day){
        staService.registerCount(day);
        return R.ok();
    }

    // 图表显示，返回两部分数据，日期为json数组，数量为json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,@PathVariable String end)
    {
        Map<String,Object> map=staService.getShowData(type,begin,end);
        return R.ok().data(map);
    }



}

