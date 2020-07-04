package com.jean.eduservice.controller;


import com.jean.commonutils.R;
import com.jean.eduservice.client.VodClient;
import com.jean.eduservice.entity.EduChapter;
import com.jean.eduservice.entity.EduVideo;
import com.jean.eduservice.service.EduVideoService;
import com.jean.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/eduservice/edu-video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //注入VodClient  去调用其他微服务做删除视频
    @Autowired
    private VodClient vodClient;


    //添加小节
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo)
    {
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    //todo后面这个方法需要完善：删除小节时，同时也要把里面的视频也删除
    @DeleteMapping("{id}")
    public  R deleteVideo(@PathVariable String id)
    {
        //根据小节id，获取到视频id，调用方法实现视频删除
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();

        //根据视频id，远程调用来实现视频删除功能
        //判断小节里面是否有视频id，有videoSourceId视频id就删除，没有就不要
        if(!StringUtils.isEmpty(videoSourceId)) {
//            vodClient.removeAlyVideo(videoSourceId); //删除视频
           R result= vodClient.removeAlyVideo(videoSourceId); //删除视频
            if(result.getCode()==20001){
                throw new GuliException(20001,"删除视频失败，熔断器...");
            }
        }
        //删除小节
        videoService.removeById(id);
        return R.ok();
    }

    //根据小节id查询
    @GetMapping("/getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId)
    {
        EduVideo eduVideo = videoService.getById(videoId);
        return  R.ok().data("video",eduVideo); //把数据传给前端使用
    }

    //修改小节
   @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo)
   {
       videoService.updateById(eduVideo);
       return R.ok();
   }

}

