package com.jean.eduservice.client;

import com.jean.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient{
    //调用vod微服务失败之后，断路器会指引我们执行下面两个方法

    @Override
    public R removeAlyVideo(String id) {

//        return R.error().message("没有调到vod微服务，删除视频出错了");
        R.error().message("没有调到vod微服务，删除视频出错了");
//        System.out.println( R.error().getCode());
        System.out.println(R.error().message("没有调到vod微服务，删除单个视频出错了"));
        System.out.println(R.error().message("没有调到vod微服务，删除单个视频出错了").getCode());
        return R.error().message("没有调到vod微服务，删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        System.out.println(R.error().message("没有调到vod微服务，删除多个视频出错了"));
        return R.error().message("没有调到vod微服务，删除多个视频出错了");
    }
}
