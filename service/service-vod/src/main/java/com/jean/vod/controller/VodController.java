package com.jean.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.jean.commonutils.R;
import com.jean.servicebase.exceptionhandler.GuliException;
import com.jean.vod.service.VodService;
import com.jean.vod.utils.ConstantVodUtils;
import com.jean.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("eduvod/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    //1、上传视频到阿里云
    @PostMapping("uploadAliyunVideo")
    public R uploadAliyunVideo(MultipartFile file)
    {
        //返回上传到aliyun上视频的id值
        String videoId=vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }

    //2、根据视频id，删除阿里云上的视频
    @DeleteMapping("removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id){
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);

            //创建一个删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //向request中设置视频id
            request.setVideoIds(id);

            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);

            return R.ok();

        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除阿里云上的视频失败");
        }

    }


    //3、删除多个阿里云视频
    //参数：多个视频id
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoList") List<String> videoIdList)
    {
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

    //4、 根据视频id获取视频播放凭证
    @GetMapping("/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id)
    {
         try{
             //初始化对象
             DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
             //创建获取凭证的request和response对象
             GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
             //向request对象中设置视频id
             request.setVideoId(id);
             //调用方法得到凭证
             GetVideoPlayAuthResponse response = client.getAcsResponse(request);
             //得到凭证
             String playAuth = response.getPlayAuth();
             //还可以设置凭证的有效期。默认时间是100秒，最大为3000秒；也可以不设置这个时间
             request.setAuthInfoTimeout(300L);
             return  R.ok().data("playAuth",playAuth);
         }
         catch(Exception e){
          throw new GuliException(20001,"获取视频播凭证失败");
         }
    }






}
