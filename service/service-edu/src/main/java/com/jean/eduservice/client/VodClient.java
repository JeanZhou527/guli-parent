package com.jean.eduservice.client;

import com.jean.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class)  //调用哪个微服务的服务名; 如果调用微服务失败了，执行fallback
@Component
public interface VodClient {

    //定义调用的方法路径
    //根据视频id，删除阿里云上的视频
    //@PathVariable注解一定要指定参数名称，否则会出错@PathVariable("id")
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);

    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoList") List<String> videoIdList);

}
